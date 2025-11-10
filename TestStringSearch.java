public class TestStringSearch {

    public static void main(String[] args) {
        System.out.println("=== TESTE DE ALGORITMOS DE BUSCA DE SUBSTRING ===\n");
        testeExemplo();
        testePerformance();
        testePiorCaso();
    }

    private static void testeExemplo() {
        System.out.println("--- TESTE 1: Exemplo do Enunciado ---");
        String s1 = "ABCDCBDCBDACBDABDCBADF";
        String s2 = "ADF";
        System.out.println("s1 = \"" + s1 + "\"");
        System.out.println("s2 = \"" + s2 + "\"");
        System.out.println();
        System.out.println("Força Bruta: " + StringSearch.forcaBruta(s1, s2));
        System.out.println("KMP:         " + StringSearch.kmp(s1, s2));
        System.out.println("Java Nativo: " + StringSearch.javanativa(s1, s2));
        System.out.println();
    }

    private static void testePerformance() {
        System.out.println("--- TESTE 2: Performance com Strings de Tamanho Crescente ---");
        System.out.println();
        int[] sizes = {1_000, 10_000, 100_000, 500_000, 1_000_000};
        for (int size : sizes) {
            System.out.println(">>> Tamanho: " + formatNumber(size) + " caracteres");
            String s1 = gerarString(size - 100, 'A') + gerarString(100, 'B');
            String s2 = gerarString(100, 'B');
            System.out.println("  Padrão encontrado na posição: " + (size - 100));
            System.out.println();
            if (size <= 100_000) {
                System.out.println("  Força Bruta: " + StringSearch.forcaBruta(s1, s2));
            } else {
                System.out.println("  Força Bruta: [Pulado - muito lento para este tamanho]");
            }
            System.out.println("  KMP:         " + StringSearch.kmp(s1, s2));
            System.out.println("  Java Nativo: " + StringSearch.javanativa(s1, s2));
            System.out.println();
        }
    }

    private static void testePiorCaso() {
        System.out.println("--- TESTE 3: Análise de Pior Caso ---");
        System.out.println("Padrão: Repetição de 'A' com padrão não encontrado\n");
        int[] sizes = {10_000, 50_000, 100_000, 500_000};
        for (int size : sizes) {
            System.out.println(">>> Tamanho: " + formatNumber(size) + " caracteres");
            String s1 = gerarString(size, 'A');
            String s2 = "AAAB";
            System.out.println("  s1 = AAA...AAA (" + size + " A's)");
            System.out.println("  s2 = \"AAAB\" (não encontrado)");
            System.out.println();
            if (size <= 50_000) {
                System.out.println("  Força Bruta: " + StringSearch.forcaBruta(s1, s2));
            } else {
                System.out.println("  Força Bruta: [Pulado - O(n*m) é muito lento]");
            }
            System.out.println("  KMP:         " + StringSearch.kmp(s1, s2));
            System.out.println("  Java Nativo: " + StringSearch.javanativa(s1, s2));
            System.out.println();
        }
    }

    private static String gerarString(int length, char c) {
        char[] arr = new char[length];
        for (int i = 0; i < length; i++) {
            arr[i] = c;
        }
        return new String(arr);
    }

    private static String formatNumber(int num) {
        return String.format("%,d", num);
    }
}
