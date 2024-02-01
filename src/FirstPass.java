package src;

import src.Parser.InstructionNode;
import src.Parser.LabelNode;
import src.Parser.Node;
import src.Parser.OperandNode;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirstPass {
    private final String filePath;
    private LexicalAnalyzer lexo;
    private SymbolTable symbolTable;

    private static final List<String> MNEMONICS = new ArrayList<>(Arrays.asList("LSLS", "LSRS", "ASRS", "ADDS", "SUBS", "MOVS", "CMP", "ANDS", "EORS", "ADCS", "SBCS", "RORS", "TST", "RSBS", "CMN", "ORRS", "MULS", "BICS", "MVNS", "STR", "LDR", "ADD", "SUB", "BEQ", "BNE", "BCS", "BCC", "BMI", "BPL", "BVS", "BVC", "BHI", "BLS", "BGE", "BLT", "BGT", "BLE", "BAL", "B"));
    private static final List<String> REGISTERS = new ArrayList<>(Arrays.asList("R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7"));

    public FirstPass(String filePath, LexicalAnalyzer lexo, SymbolTable symbolTable) {
        this.filePath = filePath;
        this.lexo = lexo;
        this.symbolTable = symbolTable;
    }

    public List<String> getLinesFromFile() throws IOException {
        return Files.readAllLines(Paths.get(this.filePath));
    }

    public boolean execute() {
        // Tokenization
        try {
            List<String> lines = this.getLinesFromFile();
            int labelAddress = 0;
            for (String line : lines) {
                String[] words = line.split("\\s+");
                for (int i = 0 ; i<words.length; i++) {
                    if (MNEMONICS.contains(words[i])) {
                        this.lexo.addToken(new Token(TokenType.MNEMONIC, words[i]));
                    }
                    if (REGISTERS.contains(words[i])) {
                        this.lexo.addToken(new Token(TokenType.REGISTER, words[i]));
                    }
                    if (words[i].startsWith("#")) {
                        this.lexo.addToken(new Token(TokenType.IMMEDIATE, words[i]));
                    }
                    if (i == words.length - 1) {
                        this.lexo.addToken(new Token(TokenType.ENDOFLINE, words[i]));
                    }
                    if ((i != words.length - 1 && words[i+1].equals(":"))){
                        this.lexo.addToken(new Token(TokenType.LABEL, words[i]));
                        this.symbolTable.addSymbol(new Symbol(labelAddress , words[i]));
                        labelAddress+=4;
                    }
                    if ((i != 0 && words[i-1].equals(":"))){
                        this.symbolTable.addSymbol(new Symbol(labelAddress , words[i]));
                        labelAddress+=4;
                    }
                }
            }
            this.lexo.addToken(new Token(TokenType.ENDOFFILE, ""));
            System.out.println(symbolTable.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Node tree = this.buildTree(this.organizeTokens(this.lexo.getTokens()));
        System.out.println(tree);
        return tree.checkSyntax();
    }

    public List<List<Token>> organizeTokens(List<Token> tokens) {
        List<List<Token>> lines = new ArrayList<>();
        List<Token> line = new ArrayList<>();
        int index = 0;
        while (index < tokens.size() && !tokens.get(index).getType().equals(TokenType.ENDOFFILE)) {
            if (tokens.get(index).getType().equals(TokenType.ENDOFLINE)) {
                lines.add(line);
                line = new ArrayList<>();
            } else {
                line.add(tokens.get(index));
            }
            index++;
        }
        if (!line.isEmpty()) {
            lines.add(line);
        }
        return lines;
    }


    public Node buildTree(List<List<Token>> lines) {
        Node root = new LabelNode(new Token(TokenType.LABEL, "root"));
        for(List<Token> line : lines){
            if (isLabel(line)){
                Node label = new LabelNode(line.get(0));
                for (List<Token> innerLine : lines){
                    if (isInstruction(innerLine)){
                        buildTreeHelper(innerLine, label);
                    }
                }
                root.addChild(label);
            }
        }
        return root;
    }

    public void buildTreeHelper(List<Token> line , Node tree){
        Node instruction = new InstructionNode(line.get(0));
        for (int i = 1; i<line.size() ; i++){
            instruction.addChild(new OperandNode(line.get(i)));
        }
        tree.addChild(instruction);
    }

    public boolean isLabel (List<Token> line){
        return !line.isEmpty() && line.get(0).getType().equals(TokenType.LABEL);
    }
    public boolean isInstruction(List<Token> line){
        return !line.isEmpty() && line.get(0).getType().equals(TokenType.MNEMONIC);
    }

    public LexicalAnalyzer getLexo() {
        return this.lexo;
    }


}
