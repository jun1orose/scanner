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

    private long currentMatch;
    private List<Long> matchIndexes = Collections.synchronizedList(new ArrayList<>());

    private Future<?> searchTask;

    private char[] pattern;
    private int[] shift;


    
    private SearchEngine(Path filePath, long firstMatch, char[] pattern, int[] shift) {
        this.filePath = filePath;
        this.matchIndexes.add(firstMatch);
        this.pattern = pattern;
        this.shift = shift;
    }

    public static SearchEngine construct(Path filePath, char[] pattern) {

        int[] shift = calculateShift(pattern);
        long index = indexOfFirstMatch(filePath, pattern, shift);

        if (index != -1) {
            return new SearchEngine(filePath, index, pattern, shift);
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
                        return i + 1 - k;
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
}
