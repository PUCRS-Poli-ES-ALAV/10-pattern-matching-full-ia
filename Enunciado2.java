public class Enunciado2 {

    private static final int RADIX = 256;
    private static final int PRIME = 1_000_000_007;

    private static final class SearchResult {
        final int position;
        final long iterations;
        final long instructions;
        final long timeNanos;

        SearchResult(int position, long iterations, long instructions, long timeNanos) {
            this.position = position;
            this.iterations = iterations;
            this.instructions = instructions;
            this.timeNanos = timeNanos;
        }

        @Override
        public String toString() {
            return String.format("pos=%d | it=%d | inst=%d | tempo=%.3f ms",
                    position, iterations, instructions, timeNanos / 1_000_000.0);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Enunciado 2 – Rabin-Karp ===");
        runExample();
        runLargeTests();
        runWorstCase();
        System.out.println("Complexidade (pior caso): O(N * M)");
    }

    private static SearchResult rabinKarp(String text, String pattern) {
        long start = System.nanoTime();
        long iterations = 0;
        long instructions = 0;

        int n = text.length();
        int m = pattern.length();
        instructions += 2;

        if (m == 0) {
            instructions++;
            return new SearchResult(0, iterations, instructions, System.nanoTime() - start);
        }

        if (m > n) {
            instructions++;
            return new SearchResult(-1, iterations, instructions, System.nanoTime() - start);
        }

        long h = 1;
        for (int i = 0; i < m - 1; i++) {
            iterations++;
            instructions += 4;
            h = (h * RADIX) % PRIME;
        }

        long patHash = 0;
        long windowHash = 0;
        for (int i = 0; i < m; i++) {
            iterations++;
            instructions += 6;
            patHash = (patHash * RADIX + pattern.charAt(i)) % PRIME;
            windowHash = (windowHash * RADIX + text.charAt(i)) % PRIME;
        }

        for (int i = 0; i <= n - m; i++) {
            iterations++;
            instructions += 2;
            if (patHash == windowHash) {
                instructions++;
                boolean match = true;
                instructions++;
                for (int j = 0; j < m; j++) {
                    iterations++;
                    instructions += 2;
                    instructions += 2;
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        instructions++;
                        match = false;
                        instructions++;
                        break;
                    }
                    instructions++;
                }
                instructions++;
                if (match) {
                    return new SearchResult(i, iterations, instructions, System.nanoTime() - start);
                }
            }

            if (i < n - m) {
                instructions++;
                long leading = text.charAt(i);
                long trailing = text.charAt(i + m);
                instructions += 5;
                windowHash = (RADIX * (windowHash - leading * h) + trailing) % PRIME;
                instructions += 3;
                if (windowHash < 0) {
                    windowHash += PRIME;
                    instructions++;
                }
            }
        }

        return new SearchResult(-1, iterations, instructions, System.nanoTime() - start);
    }

    private static void runExample() {
        String text = "ABCDCBDCBDACBDABDCBADF";
        String pattern = "ADF";
        System.out.println("Exemplo: " + rabinKarp(text, pattern));
    }

    private static void runLargeTests() {
        int[] sizes = {1_000, 50_000, 800_000};
        String pattern = repeatChar(30, 'B');

        for (int size : sizes) {
            String text = repeatChar(size - pattern.length(), 'A') + pattern;
            SearchResult result = rabinKarp(text, pattern);
            System.out.println("Texto=" + format(size) + " | " + result);
        }
    }

    private static void runWorstCase() {
        int[] sizes = {1_000, 50_000, 600_000};
        String pattern = "AAAAAB";

        for (int size : sizes) {
            String text = repeatChar(size, 'A');
            SearchResult result = rabinKarp(text, pattern);
            System.out.println("Pior caso texto=" + format(size) + " | " + result);
        }
    }

    private static String repeatChar(int length, char c) {
        char[] data = new char[length];
        java.util.Arrays.fill(data, c);
        return new String(data);
    }

    private static String format(int value) {
        return String.format("%,d", value);
    }
}
