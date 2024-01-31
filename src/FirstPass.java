package src;

import src.Parser.InstructionNode;
import src.Parser.LabelNode;
import src.Parser.Node;
import src.Parser.OperandNode;

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
            for (String line : lines) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    if (MNEMONICS.contains(word)) {
                        this.lexo.addToken(new Token(TokenType.MNEMONIC, word));
                    }
                    if (REGISTERS.contains(word)) {
                        this.lexo.addToken(new Token(TokenType.REGISTER, word));
                    }
                    if (word.startsWith("#")) {
                        this.lexo.addToken(new Token(TokenType.IMMEDIATE, word));
                    }
                    if (word.equals("\n")) {
                        this.lexo.addToken(new Token(TokenType.ENDOFLINE, word));
                    }
                }
            }
            this.lexo.addToken(new Token(TokenType.ENDOFFILE, ""));

        } catch (IOException e) {
            e.printStackTrace();
        }
        Node tree = this.buildTree(this.organizeTokens(this.lexo.getTokens()));
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
        Node tree = new LabelNode(new Token(TokenType.LABEL, ""));
        for (List<Token> line : lines) {
            Node instruction = new InstructionNode(line.get(0));
            for (int i = 1; i < line.size(); i++) {
                instruction.addChild(new OperandNode(line.get(i)));
            }
            tree.addChild(instruction);
        }
        return tree;
    }

    public LexicalAnalyzer getLexo() {
        return this.lexo;
    }


}
