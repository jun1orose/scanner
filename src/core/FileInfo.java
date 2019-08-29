package core;

import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class FileInfo {

    private static final int CHUNK_SIZE = 1024 * 256;
    private int actualChunk;
    private volatile int chunkLimit = -1;
    private volatile boolean loading;

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
            String str = loadChunk(0);
            doc.insertString(0, str, null);
        } catch (IOException | BadLocationException e) {
            e.printStackTrace();
        } finally {
            loading = false;
        }
    }

    public void loadPrevChunk() {
        if (actualChunk - 1 >= 0) {

            loading = true;

            core.submitTask(() -> {
                try {
                    clearLowerChunk();
                    String str = loadChunk(--actualChunk);
                    doc.insertString(0, str, null);

                } catch (BadLocationException | IOException e) {
                    e.printStackTrace();
                } finally {
                    loading = false;
                }
            });
        }
    }

    public void loadNextChunk() {
        if (actualChunk != chunkLimit) {

            loading = true;

            core.submitTask(() -> {
                try {
                    clearUpperChunk();
                    String str = loadChunk(actualChunk + 1);

                    if (str.length() != 0) {
                        actualChunk++;

                        int endPos = doc.getLength();
                        doc.insertString(endPos, str, null);
                    }
                } catch (BadLocationException | IOException e) {
                    e.printStackTrace();
                } finally {
                    loading = false;
                }
            });
        }
    }

    private String loadChunk(int chunk) throws IOException {

        char[] charBuf = new char[CHUNK_SIZE];

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(filePath.toString(), StandardCharsets.UTF_8))) {

            reader.skip(CHUNK_SIZE * chunk);
            int count = reader.read(charBuf);

            if (count < CHUNK_SIZE) {
                if (count <= 1) {
                    chunkLimit = chunk - 1;
                    return "";
                }
                chunkLimit = chunk;
                return new String(charBuf, 0, count);
            }
        }

        return new String(charBuf);
    }

    private void clearUpperChunk() throws BadLocationException {
        System.out.println(doc.getLength());
        if (actualChunk != 0) {
            doc.remove(0, CHUNK_SIZE);
        }
    }

    private void clearLowerChunk() throws BadLocationException {
        System.out.println(doc.getLength());
        if (actualChunk != chunkLimit) {
            doc.remove(CHUNK_SIZE, doc.getLength() - CHUNK_SIZE);
        }
    }

    public boolean isLoading() {
        return loading;
    }

    public PlainDocument getDoc() {
        return doc;
    }

    public String getFileName() {
        return filePath.toString();
    }
}
