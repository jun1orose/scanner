package core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

public class SearchEngine {

    private Core core;
    private Path filePath;

    private int currentMatch;
    private List<Long> matchIndexes = Collections.synchronizedList(new ArrayList<>());

    private Future searchTask;

    private char[] pattern;
    private int[] shift;



    private SearchEngine(Core core, Path filePath, long firstMatch, char[] pattern, int[] shift) {
        this.core = core;
        this.filePath = filePath;
        this.matchIndexes.add(firstMatch);
        this.pattern = pattern;
        this.shift = shift;
    }

    static SearchEngine construct(Core core, Path filePath, char[] pattern) {

        int[] shift = calculateShift(pattern);
        long index = indexOfFirstMatch(filePath, pattern, shift);

        if (index != -1) {
            return new SearchEngine(core, filePath, index, pattern, shift);
        }

        return null;
    }

    private static long indexOfFirstMatch(Path filePath, char[] pattern, int[] shift) {

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(filePath.toString(), StandardCharsets.UTF_8))) {

            int k = 0;
            long i = 0;
            int chr;

            while ((chr = reader.read()) > 0) {

                while (pattern[k] != chr && k > 0) {
                    k = shift[k - 1];
                }

                if (pattern[k] == chr) {
                    if (++k == pattern.length) {
                        return i + 1L - k;
                    }
                } else {
                    k = 0;
                }

                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private static int[] calculateShift(char[] pattern) {

        int[] shift = new int[pattern.length];
        shift[0] = 0;

        for (int i = 1; i < pattern.length; i++) {

            int k = shift[i - 1];

            while (pattern[k] != pattern[i] && k > 0) {
                k = shift[k - 1];
            }

            if (pattern[k] == pattern[i]) {
                shift[i] = k + 1;
            } else {
                shift[i] = 0;
            }
        }

        return shift;
    }

    public void fullSearch() {
        searchTask = core.submitTask(() -> {

            try (BufferedReader reader =
                         new BufferedReader(new FileReader(filePath.toString(), StandardCharsets.UTF_8))) {

                int k = 0;
                long i = matchIndexes.get(currentMatch);
                int chr;

                reader.skip(i + 1);

                while (!Thread.currentThread().isInterrupted() && (chr = reader.read()) > 0) {

                    while (pattern[k] != chr && k > 0) {
                        k = shift[k - 1];
                    }

                    if (pattern[k] == chr) {
                        if (++k == pattern.length) {
                            matchIndexes.add(i + 1L - k);
                            k = shift[k - 1];
                        }
                    } else {
                        k = 0;
                    }

                    i++;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void stopSearch() {
        searchTask.cancel(true);
    }
}
