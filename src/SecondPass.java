package src;

import javax.swing.text.html.Option;
import java.util.*;

public class SecondPass {
    private final FirstPass firstPass;
    private final BinaryMapping mapping;
    private final List<String> EIGHT_BITS_IMMEDIATES_OPERATIONS = Arrays.asList("MOVS", "STR", "LDR");
    private final List<String> SEVEN_BITS_IMMEDIATES_OPERATIONS = Arrays.asList("ADD", "SUB");
    private final List<String> BRANCHES = Arrays.asList("BEQ", "BNE", "BCS", "BHS", "BCC", "BLO", "BMI", "BPL", "BVS", "BVC", "BHI", "BLS", "BGE", "BLT", "BGT", "BLE", "BAL");

    List<StringBuilder> instructions = new ArrayList<>();
    private int instructionCounter = 0;


    public SecondPass(FirstPass firstPass) {
        this.firstPass = firstPass;
        this.mapping = new BinaryMapping();
    }

    public List<StringBuilder> execute() {
        List<StringBuilder> instructions = this.getInstructionsInBinary();
        System.out.println(instructions);
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
            System.out.println(line.get(0));
            // duplicates
            if (line.get(0).getType().equals(TokenType.LABEL)) {
                continue;
            }
            if (line.get(0).getLexeme().equalsIgnoreCase("ADDS") && line.size() == 4 && line.get(line.size() - 1).getType().equals(TokenType.REGISTER)) {
                appendInstruction(currentInstruction, "ADDS_REGISTERS", line, 3);
                instructionCounter++;

            } else if (line.get(0).getLexeme().equalsIgnoreCase("ADDS") && line.size() - 1 == 3 && line.get(line.size() - 1).getType().equals(TokenType.IMMEDIATE)) {
                appendInstruction(currentInstruction, "ADDS_IMMEDIATES", line, 3);
                instructionCounter++;
            } else if (line.get(0).getLexeme().equalsIgnoreCase("SUBS") && line.size() == 4 && line.get(line.size() - 1).getType().equals(TokenType.REGISTER)) {
                appendInstruction(currentInstruction, "SUBS_REGISTERS", line, 3);
                instructionCounter++;
            } else if (line.get(0).getLexeme().equalsIgnoreCase("SUBS") && line.size() - 1 == 3 && line.get(line.size() - 1).getType().equals(TokenType.IMMEDIATE)) {
                appendInstruction(currentInstruction, "SUBS_IMMEDIATES", line, 3);
                instructionCounter++;
            } else if (line.get(0).getLexeme().equalsIgnoreCase("CMP") && line.get(line.size() - 1).getType().equals(TokenType.IMMEDIATE)) {
                appendInstruction(currentInstruction, "CMP_IMMEDIATES", line, 8);
                instructionCounter++;
            } else if (line.get(0).getLexeme().equalsIgnoreCase("LSLS") && line.size() == 4) {
                appendInstruction(currentInstruction, "LSLS_3", line, 5);
                instructionCounter++;
            } else if (line.get(0).getLexeme().equalsIgnoreCase("LSRS") && line.size() == 4) {
                appendInstruction(currentInstruction, "LSRS_3", line, 5);
                instructionCounter++;
            } else if (line.get(0).getLexeme().equalsIgnoreCase("ASRS") && line.size() == 4) {
                instructionCounter++;
                appendInstruction(currentInstruction, "ASRS_3", line, 5);
                // Others
            } else if (EIGHT_BITS_IMMEDIATES_OPERATIONS.stream().anyMatch(word -> word.equalsIgnoreCase(line.get(0).getLexeme()))) {
                instructionCounter++;
                appendInstruction(currentInstruction, line.get(0).getLexeme(), line, 8);
            } else if (SEVEN_BITS_IMMEDIATES_OPERATIONS.stream().anyMatch(word -> word.equalsIgnoreCase(line.get(0).getLexeme()))) {
                instructionCounter++;
                appendInstruction(currentInstruction, line.get(0).getLexeme(), line, 7);
            } else if (BRANCHES.stream().anyMatch(word -> word.equalsIgnoreCase(line.get(0).getLexeme()))) {
                instructionCounter++;
                appendInstruction(currentInstruction, line.get(0).getLexeme(), line, 8);
            } else if (line.get(0).getLexeme().equalsIgnoreCase("B")) {
                instructionCounter++;
                appendInstruction(currentInstruction, line.get(0).getLexeme(), line, 11);
            } else {
                instructionCounter++;
                appendInstruction(currentInstruction, line.get(0).getLexeme(), line, 0);
            }
            this.instructions.add(currentInstruction);
            currentInstruction = new StringBuilder();
        }
        return this.instructions;
    }

    public List<StringBuilder> getInstructions() {
        return this.instructions;
    }

    public void appendInstruction(StringBuilder Instruction, String mnemonic, List<Token> line, int bitsNumber) {
        Instruction.append(this.mapping.findMnemonic(mnemonic));
        System.out.println(Instruction);
        if (mnemonic.equalsIgnoreCase("RSBS") || mnemonic.equalsIgnoreCase("MULS")){
            for (int i = line.size() - 2 ; i>=1 ; i--){
                Instruction.append(this.mapping.findMnemonic(line.get(i).getLexeme()));
            }
        }
        else if (line.get(2).getType().equals(TokenType.REGISTER)) {
            for (int i = line.size() - 1; i>=1 ; i--){
                if (line.get(i).getType().equals(TokenType.IMMEDIATE) && !mnemonic.equalsIgnoreCase("RSBS")) {
                    String binary = Integer.toBinaryString(Integer.parseInt(line.get(i).getLexeme().substring(1)));
                    String formattedBinary = String.format("%" + bitsNumber + "s", binary).replace(' ', '0');
                    Instruction.append(formattedBinary);
                } else {
                    Instruction.append(this.mapping.findMnemonic(line.get(i).getLexeme()));
                }
            }
        } else {
            for (int i = 1; i < line.size(); i++) {
                if (line.get(i).getType().equals(TokenType.IMMEDIATE)) {
                    String binary = Integer.toBinaryString(Integer.parseInt(line.get(i).getLexeme().substring(1)));
                    System.out.println(binary);
                    String formattedBinary = String.format("%" + bitsNumber + "s", binary).replace(' ', '0');
                    Instruction.append(formattedBinary);
                } else {
                    Instruction.append(this.mapping.findMnemonic(line.get(i).getLexeme()));
                }
            }
        }

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
