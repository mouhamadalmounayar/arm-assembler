package src;


import java.util.*;

public class SecondPass {
    private final FirstPass firstPass;
    private final BinaryMapping mapping;
    private final List<String> EIGHT_BITS_IMMEDIATES_OPERATIONS = Arrays.asList("MOVS", "STR", "LDR");
    private final List<String> SEVEN_BITS_IMMEDIATES_OPERATIONS = Arrays.asList("ADD", "SUB");
    private final List<String> BRANCHES = Arrays.asList("BEQ", "BNE", "BCS", "BHS", "BCC", "BLO", "BMI", "BPL", "BVS", "BVC", "BHI", "BLS", "BGE", "BLT", "BGT", "BLE", "BAL");

    List<StringBuilder> instructions = new ArrayList<>();
    private int instructionCounter = -1;


    public SecondPass(FirstPass firstPass) {
        this.firstPass = firstPass;
        this.mapping = new BinaryMapping();
    }

    public List<StringBuilder> execute() {
        List<StringBuilder> instructions = this.getInstructionsInBinary();
        List<StringBuilder> result = new ArrayList<>();
        for (StringBuilder instruction : instructions) {
            StringBuilder hexInstruction = convertToHexa(instruction);
            result.add(hexInstruction);
        }
        return result;
    }

    public List<StringBuilder> getInstructionsInBinary() {
        List<List<Token>> lines = this.firstPass.organizeTokens(this.firstPass.getLexo().getTokens());
        StringBuilder currentInstruction = new StringBuilder();
        for (List<Token> line : lines) {
            // duplicates
            if (line.get(0).type().equals(TokenType.LABEL)) {
                continue;
            }
            if (line.get(0).lexeme().equalsIgnoreCase("ADDS") && line.size() == 4 && line.get(line.size() - 1).type().equals(TokenType.REGISTER)) {
                appendInstruction(currentInstruction, "ADDS_REGISTERS", line, 3);
                instructionCounter++;
            } else if (line.get(0).lexeme().equalsIgnoreCase("ADDS") && line.size() - 1 == 3 && line.get(line.size() - 1).type().equals(TokenType.IMMEDIATE)) {
                appendInstruction(currentInstruction, "ADDS_IMMEDIATES", line, 3);
                instructionCounter++;
            } else if (line.get(0).lexeme().equalsIgnoreCase("SUBS") && line.size() == 4 && line.get(line.size() - 1).type().equals(TokenType.REGISTER)) {
                appendInstruction(currentInstruction, "SUBS_REGISTERS", line, 3);
                instructionCounter++;
            } else if (line.get(0).lexeme().equalsIgnoreCase("SUBS") && line.size() - 1 == 3 && line.get(line.size() - 1).type().equals(TokenType.IMMEDIATE)) {
                appendInstruction(currentInstruction, "SUBS_IMMEDIATES", line, 3);
                instructionCounter++;
            } else if (line.get(0).lexeme().equalsIgnoreCase("CMP") && line.get(line.size() - 1).type().equals(TokenType.IMMEDIATE)) {
                appendInstruction(currentInstruction, "CMP_IMMEDIATES", line, 8);
                instructionCounter++;
            } else if (line.get(0).lexeme().equalsIgnoreCase("LSLS") && line.size() == 4) {
                appendInstruction(currentInstruction, "LSLS_3", line, 5);
                instructionCounter++;
            } else if (line.get(0).lexeme().equalsIgnoreCase("LSRS") && line.size() == 4) {
                appendInstruction(currentInstruction, "LSRS_3", line, 5);
                instructionCounter++;
            } else if (line.get(0).lexeme().equalsIgnoreCase("ASRS") && line.size() == 4) {
                instructionCounter++;
                appendInstruction(currentInstruction, "ASRS_3", line, 5);
                // Others
            } else if (EIGHT_BITS_IMMEDIATES_OPERATIONS.stream().anyMatch(word -> word.equalsIgnoreCase(line.get(0).lexeme())) || BRANCHES.stream().anyMatch(word -> word.equalsIgnoreCase(line.get(0).lexeme()))) {
                instructionCounter++;
                appendInstruction(currentInstruction, line.get(0).lexeme(), line, 8);
            } else if (SEVEN_BITS_IMMEDIATES_OPERATIONS.stream().anyMatch(word -> word.equalsIgnoreCase(line.get(0).lexeme()))) {
                instructionCounter++;
                appendInstruction(currentInstruction, line.get(0).lexeme(), line, 7);
            } else if (line.get(0).lexeme().equalsIgnoreCase("B")) {
                instructionCounter++;
                appendInstruction(currentInstruction, line.get(0).lexeme(), line, 11);
            } else {
                instructionCounter++;
                appendInstruction(currentInstruction, line.get(0).lexeme(), line, 0);
            }
            this.instructions.add(currentInstruction);
            currentInstruction = new StringBuilder();
        }
        return this.instructions;
    }

    public List<StringBuilder> getInstructions() {
        return this.instructions;
    }

    public void appendInstruction(StringBuilder instruction, String mnemonic, List<Token> line, int bitsNumber) {
        instruction.append(this.mapping.findMnemonic(mnemonic));
        if (mnemonic.equalsIgnoreCase("RSBS") || mnemonic.equalsIgnoreCase("MULS")) {
            for (int i = line.size() - 2; i >= 1; i--) {
                instruction.append(this.mapping.findMnemonic(line.get(i).lexeme()));
            }
        } else if (mnemonic.startsWith("B") || mnemonic.startsWith("b")) {
            Optional<Symbol> symbol = this.firstPass.getSymbolTable().findSymbol(line.get(1).lexeme());
            if (symbol.isPresent()) {
                int offset = symbol.get().getAddress() - instructionCounter - 3;
                offset &= (1 << bitsNumber) + offset;
                String binary = Integer.toBinaryString(offset);
                String formattedBinary = String.format("%" + bitsNumber + "s", binary).replace(' ', '0');
                instruction.append(formattedBinary);
            } else {
                System.out.println("Unrecognized labels!");
            }
        } else if (line.size() > 2 && line.get(2).type().equals(TokenType.REGISTER)&& !line.get(2).lexeme().equalsIgnoreCase("sp") && !mnemonic.equalsIgnoreCase("str") && !mnemonic.equalsIgnoreCase("ldr")) {
            for (int i = line.size() - 1; i >= 1; i--) {
                if (line.get(i).type().equals(TokenType.IMMEDIATE) && !mnemonic.equalsIgnoreCase("RSBS")) {
                    appendRestOfInstruction(line.get(i), bitsNumber, instruction, mnemonic);
                } else {
                    instruction.append(this.mapping.findMnemonic(line.get(i).lexeme()));
                }
            }
        } else {
            for (int i = 1; i < line.size(); i++) {
                if (line.get(i).type().equals(TokenType.IMMEDIATE)) {
                    appendRestOfInstruction(line.get(i), bitsNumber, instruction, mnemonic);
                } else {
                    instruction.append(this.mapping.findMnemonic(line.get(i).lexeme()));
                }
            }
        }

    }

    private void appendRestOfInstruction(Token token, int bitsNumber, StringBuilder instruction, String mnemonic) {
        String binary;
        if (mnemonic.equalsIgnoreCase("str") || mnemonic.equalsIgnoreCase("ldr") || mnemonic.equalsIgnoreCase("sub") || mnemonic.equalsIgnoreCase("add")) {
            binary = Integer.toBinaryString(Integer.parseInt(token.lexeme().substring(1)) / 4);
        } else {
            binary = Integer.toBinaryString(Integer.parseInt(token.lexeme().substring(1)));
        }
        String formattedBinary = String.format("%" + bitsNumber + "s", binary).replace(' ', '0');
        instruction.append(formattedBinary);
    }

    public StringBuilder convertToHexa(StringBuilder binary) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 4) {
            String fourBits = binary.substring(i, i + 4);
            int decimal = Integer.parseInt(fourBits, 2);
            String hexDigit = Integer.toHexString(decimal).toUpperCase();
            result.append(hexDigit);
        }
        return result;
    }

}
