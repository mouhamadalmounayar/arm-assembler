package src;

import java.util.*;

public class SecondPass {
    private final FirstPass firstPass;
    private final BinaryMapping mapping;
    private final List<String> EIGHT_BITS_IMMEDIATES_OPERATIONS = Arrays.asList("MOVS" , "STR" , "LDR");
    private final List<String> SEVEN_BITS_IMMEDIATES_OPERATIONS = Arrays.asList("ADD" , "SUB");
    List<StringBuilder> instructions = new ArrayList<>();

    public SecondPass(FirstPass firstPass) {
        this.firstPass = firstPass;
        this.mapping = new BinaryMapping();
    }

    public void execute() {
        List<List<Token>> lines = this.firstPass.organizeTokens(this.firstPass.getLexo().getTokens());
        StringBuilder currentInstruction = new StringBuilder();
        for (List<Token> line : lines) {
            // duplicates
            if (line.get(0).getType().equals(TokenType.LABEL)){
                continue;
            }
            if (line.get(0).getLexeme().equals("ADDS") && line.size() == 4 && line.get(line.size() - 1).getType().equals(TokenType.REGISTER)) {
                appendInstruction(currentInstruction, "ADDS_REGISTERS", line, 3);
            } else if (line.get(0).getLexeme().equals("ADDS") && line.size() - 1 == 3 && line.get(line.size() - 1).getType().equals(TokenType.IMMEDIATE)) {
                appendInstruction(currentInstruction, "ADDS_IMMEDIATES", line, 3);
            } else if (line.get(0).getLexeme().equals("SUBS") && line.size() == 4 && line.get(line.size() - 1).getType().equals(TokenType.REGISTER)) {
                appendInstruction(currentInstruction, "SUBS_REGISTERS", line, 3);
            } else if (line.get(0).getLexeme().equals("SUBS") && line.size() - 1 == 3 && line.get(line.size() - 1).getType().equals(TokenType.IMMEDIATE)) {
                appendInstruction(currentInstruction, "SUBS_IMMEDIATES", line, 3);
            } else if (line.get(0).getLexeme().equals("CMP") && line.get(line.size() - 1).getType().equals(TokenType.IMMEDIATE)) {
                appendInstruction(currentInstruction, "CMP_IMMEDIATES", line, 8);
            } else if (line.get(0).getLexeme().equals("LSLS") && line.size() == 4){
                appendInstruction(currentInstruction, "LSLS_3" , line , 5);
            } else if (line.get(0).getLexeme().equals("LSRS") && line.size() == 4){
                appendInstruction(currentInstruction , "LSRS_3", line , 5);
            } else if (line.get(0).getLexeme().equals("ASRS") && line.size() == 4){
                appendInstruction(currentInstruction , "ASRS_3" , line , 5);
            // Others
            } else if (EIGHT_BITS_IMMEDIATES_OPERATIONS.contains(line.get(0).getLexeme())){
                appendInstruction(currentInstruction , line.get(0).getLexeme() , line , 8);
            } else if (SEVEN_BITS_IMMEDIATES_OPERATIONS.contains(line.get(0).getLexeme())){
                appendInstruction(currentInstruction, line.get(0).getLexeme(), line, 7);
            }
            else{
                appendInstruction(currentInstruction, line.get(0).getLexeme(), line, 0);
            }


            this.instructions.add(currentInstruction);
            currentInstruction = new StringBuilder();
        }
    }

    public List<StringBuilder> getInstructions() {
        return this.instructions;
    }

    public void appendInstruction(StringBuilder Instruction, String mnemonic, List<Token> line, int bitsNumber) {
        Instruction.append(this.mapping.findMnemonic(mnemonic));
        for (int i = line.size() - 1; i >= 1; i--) {
            if (line.get(i).getType().equals(TokenType.IMMEDIATE)) {
                String binary = Integer.toBinaryString(Integer.parseInt(line.get(i).getLexeme().substring(1)));
                String formattedBinary = String.format("%" + bitsNumber + "s", binary).replace(' ', '0');
                Instruction.append(formattedBinary);
            } else {
                Instruction.append(this.mapping.findMnemonic(line.get(i).getLexeme()));
            }

        }
    }

}
