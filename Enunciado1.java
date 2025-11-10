import java.util.Arrays;

public class Enunciado1 {

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
        System.out.println("=== Enunciado 1 – Força Bruta ===");
        runExample();
        runLargeTests();
        runWorstCase();
        System.out.println("Complexidade (pior caso): O(N * M)");
    }

    private static SearchResult bruteForce(String text, String pattern) {
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

        for (int i = 0; i <= n - m; i++) {
            iterations++;
            instructions += 2; // comparação do for + i++
            boolean match = true;
            instructions++; // atribuição match

            for (int j = 0; j < m; j++) {
                iterations++;
                instructions += 2; // comparação do for + j++
                instructions += 2; // acesso aos caracteres
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    instructions++; // comparação falha
                    match = false;
                    instructions++; // atribuição
                    break;
                }
                instructions++; // comparação bem-sucedida
            }

            instructions++; // verificação match
            if (match) {
                return new SearchResult(i, iterations, instructions, System.nanoTime() - start);
            }
        }

        return new SearchResult(-1, iterations, instructions, System.nanoTime() - start);
    }

    private static void runExample() {
        String text = "ABCDCBDCBDACBDABDCBADF";
        String pattern = "ADF";
        System.out.println("Exemplo: " + bruteForce(text, pattern));
    }

    private static void runLargeTests() {
        int[] sizes = {1_000, 50_000, 600_000};
        String pattern = buildString(20, 'B');

        for (int size : sizes) {
            String text = buildString(size - pattern.length(), 'A') + pattern;
            SearchResult result = bruteForce(text, pattern);
            System.out.println("Texto=" + format(size) + " | " + result);
        }
    }

    private static void runWorstCase() {
        int[] sizes = {1_000, 10_000, 50_000};
        String pattern = "AAAAAB";

        for (int size : sizes) {
            String text = buildString(size, 'A');
            SearchResult result = bruteForce(text, pattern);
            System.out.println("Pior caso texto=" + format(size) + " | " + result);
        }
    }

    private static String buildString(int length, char c) {
        char[] data = new char[length];
        Arrays.fill(data, c);
        return new String(data);
    }

    private static String format(int value) {
        return String.format("%,d", value);
    }
}
