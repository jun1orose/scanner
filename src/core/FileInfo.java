package core;

import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FileInfo {

    private static final int CHUNK_SIZE = 1024 * 32;

    private final int upperBound = 0;
    private int lowerBound = -1;
    private TreeSet<Integer> cachedChunks = new TreeSet<>();

    private volatile boolean loading;
    private Future executingTask;

    private Core core;
    private Path filePath;
    private PlainDocument doc;

    FileInfo(Core core, Path filePath) {
        this.core = core;
        this.filePath = filePath;
        this.doc = new PlainDocument();

        initDocument();
    }

    private void initDocument() {

        loading = true;

        try {

            String str = loadChunk(upperBound);
            doc.insertString(0, str, null);

        } catch (IOException | BadLocationException e) {
            e.printStackTrace();
        } finally {
            loading = false;
        }
    }

    public int rollToMatchAndGetActualPos(long match) {
        int chunk = (int) (match / CHUNK_SIZE);
        int actualPos = (int) (match - CHUNK_SIZE * chunk);

        if (chunk <= cachedChunks.first()) {
            while (chunk < cachedChunks.first()) {
                if (!isLoading()) {
                    loadUpperChunk();
                }
            }
        }

        if (chunk >= cachedChunks.last()) {
            while (chunk > cachedChunks.last()) {
                if (!isLoading()) {
                    loadLowerChunk();
                }
            }
            actualPos = (int) (match - CHUNK_SIZE * (chunk - 1 < 0 ? chunk : chunk -1));
        }

        try {
            if (executingTask != null) {
                executingTask.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return actualPos;
    }

    public void loadUpperChunk() {
        if (!cachedChunks.contains(upperBound)) {

            loading = true;

            executingTask = core.submitTask(() -> {

                try {
                    removeLowerChunk();
                    String str = loadChunk(cachedChunks.first() - 1);
                    doc.insertString(0, str, null);

                } catch (IOException | BadLocationException e) {
                    e.printStackTrace();
                } finally {
                    loading = false;
                }
            });
        }
    }

    public void loadLowerChunk() {
        if (!cachedChunks.contains(lowerBound)) {

            loading = true;

            executingTask = core.submitTask(() -> {

                try {
                    removeUpperChunk();
                    String str = loadChunk(cachedChunks.last() + 1);
                    doc.insertString(doc.getLength(), str, null);

                } catch (BadLocationException | IOException e) {
                    e.printStackTrace();
                } finally {
                    loading = false;
                }
            });
        }
    }

    private String loadChunk(int chunk) throws IOException {

        cachedChunks.add(chunk);
        char[] charBuf = new char[CHUNK_SIZE];

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(filePath.toString(), StandardCharsets.UTF_8))) {

            reader.skip(CHUNK_SIZE * chunk);
            int count = reader.read(charBuf);

            if (count < CHUNK_SIZE) {
                lowerBound = chunk;
                return new String(charBuf, 0, count);
            }
        }

        return new String(charBuf);
    }

    private void removeUpperChunk() throws BadLocationException {
        if (!cachedChunks.last().equals(upperBound)) {
            cachedChunks.remove(cachedChunks.first());
            doc.remove(0, CHUNK_SIZE);
        }
    }

    private void removeLowerChunk() throws BadLocationException {
        cachedChunks.remove(cachedChunks.last());
        doc.remove(CHUNK_SIZE, doc.getLength() - CHUNK_SIZE);
    }

    public boolean isLoading() {
        return loading;
    }

    public PlainDocument getDoc() {
        return doc;
    }

    public String getFileName() {
        return filePath.getFileName().toString();
    }
}
