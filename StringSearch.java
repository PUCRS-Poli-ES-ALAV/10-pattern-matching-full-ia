public class StringSearch {
    public static class SearchResult {
        public int position;
        public long iterations;
        public long instructions;
        public long timeNanos;

        public SearchResult(int position, long iterations, long instructions, long timeNanos) {
            this.position = position;
            this.iterations = iterations;
            this.instructions = instructions;
            this.timeNanos = timeNanos;
        }

        @Override
        public String toString() {
            return String.format("Position: %d | Iterations: %d | Instructions: %d | Time: %.3f ms",
                    position, iterations, instructions, timeNanos / 1_000_000.0);
        }
    }

    public static SearchResult forcaBruta(String s1, String s2) {
        long startTime = System.nanoTime();
        long iterations = 0;
        long instructions = 0;

        if (s2.length() > s1.length()) {
            return new SearchResult(-1, 0, 0, System.nanoTime() - startTime);
        }

        instructions += 2;

        for (int i = 0; i <= s1.length() - s2.length(); i++) {
            iterations++;
            instructions++;

            boolean encontrado = true;
            for (int j = 0; j < s2.length(); j++) {
                iterations++;
                instructions += 2;

                if (s1.charAt(i + j) != s2.charAt(j)) {
                    instructions++;
                    encontrado = false;
                    break;
                }
                instructions++;
            }

            instructions++;
            if (encontrado) {
                long endTime = System.nanoTime();
                return new SearchResult(i, iterations, instructions, endTime - startTime);
            }
        }

        long endTime = System.nanoTime();
        return new SearchResult(-1, iterations, instructions, endTime - startTime);
    }

    public static SearchResult kmp(String s1, String s2) {
        long startTime = System.nanoTime();
        long iterations = 0;
        long instructions = 0;

        if (s2.length() > s1.length()) {
            return new SearchResult(-1, 0, 0, System.nanoTime() - startTime);
        }

        instructions += 2;

        int[] lps = buildLPSTable(s2);
        instructions += s2.length() * 3;

        int i = 0;
        int j = 0;

        instructions += 2;

        while (i < s1.length()) {
            iterations++;
            instructions++;

            if (s1.charAt(i) == s2.charAt(j)) {
                instructions += 2;
                i++;
                j++;
                instructions += 2;
            } else {
                instructions++;
                if (j != 0) {
                    instructions++;
                    j = lps[j - 1];
                    instructions++;
                } else {
                    instructions++;
                    i++;
                }
            }

            if (j == s2.length()) {
                instructions++;
                long endTime = System.nanoTime();
                return new SearchResult(i - j, iterations, instructions, endTime - startTime);
            }
        }

        long endTime = System.nanoTime();
        return new SearchResult(-1, iterations, instructions, endTime - startTime);
    }

    private static int[] buildLPSTable(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    public static SearchResult javanativa(String s1, String s2) {
        long startTime = System.nanoTime();
        int position = s1.indexOf(s2);
        long endTime = System.nanoTime();

        long iterations = s1.length();
        long instructions = (long) s1.length() * s2.length();

        return new SearchResult(position, iterations, instructions, endTime - startTime);
    }
}
