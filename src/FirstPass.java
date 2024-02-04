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
import java.util.stream.Collectors;

public class FirstPass {
    private final String filePath;
    private LexicalAnalyzer lexo;
    private SymbolTable symbolTable;
    private int instructionCounter;

    private static final List<String> MNEMONICS = new ArrayList<>(Arrays.asList("LSLS", "LSRS", "ASRS", "ADDS", "SUBS", "MOVS", "CMP", "ANDS", "EORS", "ADCS", "SBCS", "RORS", "TST", "RSBS", "CMN", "ORRS", "MULS", "BICS", "MVNS", "STR", "LDR", "ADD", "SUB"));
    private static final List<String> REGISTERS = new ArrayList<>(Arrays.asList("R0", "R1", "R2", "R3", "R4", "R5", "R6", "R7", "sp"));

    private static final List<String> BRANCHES = new ArrayList<>(Arrays.asList("BEQ", "BNE", "BCS", "BCC", "BMI", "BPL", "BVS", "BVC", "BHI", "BLS", "BGE", "BLT", "BGT", "BLE", "BAL", "B"));

    public FirstPass(String filePath, LexicalAnalyzer lexo, SymbolTable symbolTable) {
        this.filePath = filePath;
        this.lexo = lexo;
        this.symbolTable = symbolTable;
        this.instructionCounter = -1;
    }

    public SymbolTable getSymbolTable(){
        return this.symbolTable;
    }

    public List<String> getLinesFromFile() throws IOException {
        return Files.readAllLines(Paths.get(this.filePath)).stream().filter(line -> !line.isEmpty()).collect(Collectors.toList());
    }

    public boolean execute() {
        // Tokenization
        try {
            List<String> lines = this.getLinesFromFile();
            for (String line : lines) {
                String[] words = line.split("[\\[\\],\\s]+");
                System.out.println(Arrays.toString(words));
                for (int i = 0; i < words.length; i++) {
                    int finalI = i;
                    if (MNEMONICS.stream().anyMatch(mnemonic -> mnemonic.equalsIgnoreCase(words[finalI]))) {
                        this.instructionCounter++;
                        this.lexo.addToken(new Token(TokenType.MNEMONIC, words[i]));
                    }

                    if (REGISTERS.stream().anyMatch(register -> register.equalsIgnoreCase(words[finalI]))) {
                        this.lexo.addToken(new Token(TokenType.REGISTER, words[i]));
                    }
                    if (words[i].startsWith("#")) {
                        this.lexo.addToken(new Token(TokenType.IMMEDIATE, words[i]));
                    }
                    if (i == words.length - 1) {
                        this.lexo.addToken(new Token(TokenType.ENDOFLINE, "\n"));
                    }
                    if ((i != words.length - 1 && words[i + 1].equals(":"))) {
                        Token token = new Token(TokenType.LABEL, words[i]);

                        this.lexo.addToken(token);
                        this.symbolTable.addSymbol(new Symbol(instructionCounter + 1, token));
                    }
                    if ((i != words.length - 1) && BRANCHES.stream().anyMatch(mnemonic -> mnemonic.equalsIgnoreCase(words[finalI]))){
                        this.instructionCounter++;
                        this.lexo.addToken(new Token(TokenType.MNEMONIC, words[i]));
                        this.lexo.addToken(new Token(TokenType.LABEL, words[i+1]));
                    }

                }
            }
            this.lexo.addToken(new Token(TokenType.ENDOFFILE, ""));
            System.out.println(this.lexo.getTokens());
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

        Node currentLabel = null;

        for (List<Token> line : lines) {
            if (isLabel(line)) {
                currentLabel = new LabelNode(new Token(TokenType.LABEL, line.get(0).getLexeme()));
                root.addChild(currentLabel);
            } else {
                Node instruction = new InstructionNode(line.get(0));

                for (int i = 1; i < line.size(); i++) {
                    instruction.addChild(new OperandNode(line.get(i)));
                }

                if (currentLabel != null) {
                    currentLabel.addChild(instruction);
                } else {
                    root.addChild(instruction);
                }
            }
        }

        return root;
    }

    private Token findLabelToken(List<Token> line) {
        // Assuming the first token is always the label token
        return line.get(0);
    }


    public boolean isLabel(List<Token> line) {
        return line.get(0).getType().equals(TokenType.LABEL);
    }

    public Token findLabel(List<Token> line) {
        for (Token token : line) {
            if (token.getType().equals(TokenType.LABEL)) return token;
        }
        return line.get(0);
    }

    public boolean isInstruction(List<Token> line) {
        return !line.isEmpty() && line.get(0).getType().equals(TokenType.MNEMONIC);
    }

    public LexicalAnalyzer getLexo() {
        return this.lexo;
    }

}
