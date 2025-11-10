public class Enunciado3 {

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

    private static final class LpsComputation {
        final int[] lps;
        final long iterations;
        final long instructions;

        LpsComputation(int[] lps, long iterations, long instructions) {
            this.lps = lps;
            this.iterations = iterations;
            this.instructions = instructions;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Enunciado 3 – KMP ===");
        runExample();
        runLargeTests();
        runWorstCase();
        System.out.println();
        System.out.println("Complexidade (pior caso): O(N + M)");
    }

    private static SearchResult kmpSearch(String text, String pattern) {
        long start = System.nanoTime();
        int n = text.length();
        int m = pattern.length();
        long iterations = 0;
        long instructions = 2;

        if (m == 0) {
            instructions++;
            return new SearchResult(0, iterations, instructions, System.nanoTime() - start);
        }

        if (m > n) {
            instructions++;
            return new SearchResult(-1, iterations, instructions, System.nanoTime() - start);
        }

        LpsComputation lpsComp = computeLps(pattern);
        iterations += lpsComp.iterations;
        instructions += lpsComp.instructions;
        int[] lps = lpsComp.lps;

        int i = 0;
        int j = 0;
        instructions += 2;

        while (i < n) {
            iterations++;
            instructions++;
            instructions += 2;
            if (pattern.charAt(j) == text.charAt(i)) {
                instructions++;
                i++;
                j++;
                instructions += 2;
                instructions++;
                if (j == m) {
                    return new SearchResult(i - j, iterations, instructions, System.nanoTime() - start);
                }
            } else {
                instructions++;
                if (j != 0) {
                    instructions++;
                    j = lps[j - 1];
                    instructions++;
                } else {
                    i++;
                    instructions++;
                }
            }
        }

        return new SearchResult(-1, iterations, instructions, System.nanoTime() - start);
    }

    private static LpsComputation computeLps(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        long iterations = 0;
        long instructions = 0;

        int len = 0;
        int i = 1;
        instructions += 2;

        while (i < m) {
            iterations++;
            instructions++;
            instructions += 2;
            if (pattern.charAt(i) == pattern.charAt(len)) {
                instructions++;
                len++;
                lps[i] = len;
                instructions += 2;
                i++;
                instructions++;
            } else {
                instructions++;
                if (len != 0) {
                    instructions++;
                    len = lps[len - 1];
                    instructions++;
                } else {
                    lps[i] = 0;
                    instructions++;
                    i++;
                    instructions++;
                }
            }
        }

        return new LpsComputation(lps, iterations, instructions);
    }

    private static void runExample() {
        printSection("Exemplo");
        String text = "ABCDCBDCBDACBDABDCBADF";
        String pattern = "ADF";
        printContext("Texto", "\"" + text + "\"");
        printContext("Padrão", "\"" + pattern + "\"");
        printResult("KMP", kmpSearch(text, pattern));
    }

    private static void runLargeTests() {
        printSection("Tamanhos crescentes");
        int[] sizes = {1_000, 50_000, 800_000};
        String pattern = repeatChar(30, 'B');
        printContext("Tamanho do padrão", format(pattern.length()));
        for (int size : sizes) {
            System.out.println();
            printContext("Tamanho do texto", format(size));
            printContext("Posição esperada", format(size - pattern.length()));
            String text = repeatChar(size - pattern.length(), 'A') + pattern;
            printResult("KMP", kmpSearch(text, pattern));
        }
    }

    private static void runWorstCase() {
        printSection("Pior caso");
        int[] sizes = {1_000, 100_000, 1_000_000};
        String pattern = "AAAAAB";
        printContext("Padrão testado", "\"" + pattern + "\"");
        for (int size : sizes) {
            System.out.println();
            printContext("Tamanho do texto", format(size));
            String text = repeatChar(size, 'A');
            printResult("KMP", kmpSearch(text, pattern));
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

    private static void printSection(String title) {
        System.out.println();
        System.out.println(title);
        System.out.println("-".repeat(title.length()));
    }

    private static void printContext(String label, String value) {
        System.out.printf("%-25s | %s%n", label, value);
    }

    private static void printResult(String label, SearchResult result) {
        System.out.printf("%-25s | pos=%-7d | it=%-10d | inst=%-12d | tempo=%8.3f ms%n",
                label, result.position, result.iterations, result.instructions, result.timeNanos / 1_000_000.0);
    }
}
