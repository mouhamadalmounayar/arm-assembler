package src;

public class Main {
    public static void main(String[] args) {
        FirstPass pass = new FirstPass("code.txt", new LexicalAnalyzer(), new SymbolTable());
        if (pass.execute()){
            System.out.println("The code is syntaxically correct.");
            SecondPass secondPass = new SecondPass(pass);
            secondPass.execute();
            System.out.println(secondPass.getInstructions());
        }
    }
}
