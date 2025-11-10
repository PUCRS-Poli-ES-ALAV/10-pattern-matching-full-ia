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
    
    // Força Bruta - Simples mas ineficiente em pior caso
    public static SearchResult forcaBruta(String s1, String s2) {
        long startTime = System.nanoTime();
        long iterations = 0;
        long instructions = 0;
        
        if (s2.length() > s1.length()) {
            return new SearchResult(-1, 0, 0, System.nanoTime() - startTime);
        }
        
        instructions += 2; // comparações iniciais
        
        for (int i = 0; i <= s1.length() - s2.length(); i++) {
            iterations++;
            instructions++; // incremento i
            
            boolean encontrado = true;
            for (int j = 0; j < s2.length(); j++) {
                iterations++;
                instructions += 2; // comparação + incremento j
                
                if (s1.charAt(i + j) != s2.charAt(j)) {
                    instructions++; // comparação de caracteres
                    encontrado = false;
                    break;
                }
                instructions++; // comparação bem-sucedida
            }
            
            instructions++; // verificação se encontrado
            if (encontrado) {
                long endTime = System.nanoTime();
                return new SearchResult(i, iterations, instructions, endTime - startTime);
            }
        }
        
        long endTime = System.nanoTime();
        return new SearchResult(-1, iterations, instructions, endTime - startTime);
    }
    
    // Algoritmo KMP (Knuth-Morris-Pratt) - O(n + m)
    public static SearchResult kmp(String s1, String s2) {
        long startTime = System.nanoTime();
        long iterations = 0;
        long instructions = 0;
        
        if (s2.length() > s1.length()) {
            return new SearchResult(-1, 0, 0, System.nanoTime() - startTime);
        }
        
        instructions += 2;
        
        // Construir tabela KMP
        int[] lps = buildLPSTable(s2);
        instructions += s2.length() * 3; // estimativa de construção
        
        int i = 0; // índice em s1
        int j = 0; // índice em s2
        
        instructions += 2; // inicializações
        
        while (i < s1.length()) {
            iterations++;
            instructions++; // comparação i < s1.length()
            
            if (s1.charAt(i) == s2.charAt(j)) {
                instructions += 2; // comparação + acesso
                i++;
                j++;
                instructions += 2; // incrementos
            } else {
                instructions++; // comparação falhou
                if (j != 0) {
                    instructions++; // verificação j != 0
                    j = lps[j - 1];
                    instructions++; // atribuição
                } else {
                    instructions++; // incremento de i
                    i++;
                }
            }
            
            if (j == s2.length()) {
                instructions++; // verificação final
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
    
    // Método nativo Java (indexOf)
    public static SearchResult javanativa(String s1, String s2) {
        long startTime = System.nanoTime();
        int position = s1.indexOf(s2);
        long endTime = System.nanoTime();
        
        // Estimativa: indexOf é otimizado, geralmente O(n*m)
        long iterations = s1.length();
        long instructions = s1.length() * s2.length();
        
        return new SearchResult(position, iterations, instructions, endTime - startTime);
    }
}
