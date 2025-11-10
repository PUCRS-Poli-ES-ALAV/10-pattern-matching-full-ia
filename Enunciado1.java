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
        System.out.println();
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
        printSection("Exemplo");
        String text = "ABCDCBDCBDACBDABDCBADF";
        String pattern = "ADF";
        printContext("Texto", "\"" + text + "\"");
        printContext("Padrão", "\"" + pattern + "\"");
        printResult("Força Bruta", bruteForce(text, pattern));
    }

    private static void runLargeTests() {
        printSection("Tamanhos crescentes");
        int[] sizes = {1_000, 50_000, 600_000};
        String pattern = buildString(20, 'B');
        printContext("Tamanho do padrão", format(pattern.length()));
        for (int size : sizes) {
            System.out.println();
            printContext("Tamanho do texto", format(size));
            printContext("Posição esperada", format(size - pattern.length()));
            String text = buildString(size - pattern.length(), 'A') + pattern;
            printResult("Força Bruta", bruteForce(text, pattern));
        }
    }

    private static void runWorstCase() {
        printSection("Pior caso");
        int[] sizes = {1_000, 10_000, 50_000};
        String pattern = "AAAAAB";
        printContext("Padrão testado", "\"" + pattern + "\"");
        for (int size : sizes) {
            System.out.println();
            printContext("Tamanho do texto", format(size));
            String text = buildString(size, 'A');
            printResult("Força Bruta", bruteForce(text, pattern));
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
