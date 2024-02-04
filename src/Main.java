package src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a file name : ");
        String fileName = scanner.nextLine();
        FirstPass pass = new FirstPass(fileName , new LexicalAnalyzer(), new SymbolTable());
        if (pass.execute()){
            SecondPass secondPass = new SecondPass(pass);
            List<StringBuilder> instructions = secondPass.execute();
            createFile(fileName + ".bin" , instructions);
        }
        else {
            System.out.println("Failed to assemble!");
        }
    }
    public static void createFile (String outputFile , List<StringBuilder> instructions){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write("v2.0 raw");
            writer.newLine();
            for (StringBuilder instruction : instructions) {
                writer.write(instruction.toString() + "  ");
            }
            System.out.println("Assembled to " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
