package src;

public class Main {
    public static void main(String[] args) {
        FirstPass pass = new FirstPass("code.txt", new LexicalAnalyzer(), new SymbolTable());
        if (!pass.execute()) System.out.println("Syntax error!");
        else {
            System.out.println("The code is syntaxically correct");
        }
    }
}
