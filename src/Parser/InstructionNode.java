package src.Parser;

import src.Token;
import src.TokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InstructionNode extends Node {

    private static final List<String> REGISTER_OPERATIONS = new ArrayList<>(Arrays.asList("LSLS", "LSRS", "ASRS"));
    private static final List<String> BASIC_ARITHMETIC_OPERATIONS = new ArrayList<>(Arrays.asList("ADDS", "SUBS"));
    private static final List<String> ARITHMETIC_OPERATIONS = new ArrayList<>(Arrays.asList("MOVS", "CMP"));
    private static final List<String> DATA_PROCESSING_OPERATIONS = new ArrayList<>(Arrays.asList("ANDS", "EORS", "ADCS", "SBCS", "RORS", "TST", "RSBS", "CMN", "ORRS", "MULS", "BICS", "MVNS"));
    private static final List<String> STACK_POINTER_STORAGE =  new ArrayList<>(Arrays.asList("STR", "LDR"));
    private static final List<String> STACK_POINTER_OFFSET = new ArrayList<>(Arrays.asList("ADD", "SUB"));
    private static final List<String> BRANCH_OPERATIONS = new ArrayList<>(Arrays.asList("BEQ", "BNE", "BCS", "BCC", "BMI", "BPL", "BVS", "BVC", "BHI", "BLS", "BGE", "BLT", "BGT", "BLE", "BAL", "B"));


    public InstructionNode(Token node) {
        this.node = node;
        this.children = new ArrayList<>();
    }

    public InstructionNode(Token node, List<Node> children) {
        this(node);
        this.children = children;
    }

    @Override
    public boolean checkSyntax() {
        if (!this.node.getType().equals(TokenType.MNEMONIC)) {
            return false;
        }
        String mnemonic = this.node.getLexeme();
        // register operations
        if (REGISTER_OPERATIONS.contains(mnemonic)) {
            if (this.children.size() == 3) {
                return (this.checkChildrenSyntax() && this.children.get(0).isRegister() && this.children.get(1).isRegister() && this.children.get(2).isImmediate() || this.children.get(2).isRegister());
            }
            if (this.children.size() == 2){
                return (this.checkChildrenSyntax() && this.children.get(0).isRegister() && this.children.get(1).isRegister());
            }
        }
        // arithmetic operation registers
        if (BASIC_ARITHMETIC_OPERATIONS.contains(mnemonic)) {
            if (this.children.size() == 3) {
                return (this.checkChildrenSyntax() && this.children.get(0).isRegister() && this.children.get(1).isRegister());
            }
            if (this.children.size() == 2) {
                return (this.checkChildrenSyntax() && this.children.get(0).isRegister() && this.children.get(1).isImmediate());
            }
        }
        // arithmetic operations
        if (ARITHMETIC_OPERATIONS.contains(mnemonic)) {
            if (this.children.size() == 2 && mnemonic.equals("CMP")){
                return (this.checkChildrenSyntax() && this.children.get(0).isRegister() && this.children.get(1).isRegister());
            }
            return (this.checkChildrenSyntax() && this.children.get(0).isRegister() && this.children.get(1).isRegister() && this.children.size() == 2);
        }
        // data processing operations
        if (DATA_PROCESSING_OPERATIONS.contains(mnemonic)){
            if (this.children.size() == 3){
                if (mnemonic.equals("RSBS")) {
                    return this.checkChildrenSyntax() && this.children.get(0).isRegister() && this.children.get(1).isRegister() &&  this.children.get(2).equals(new OperandNode(new Token(TokenType.IMMEDIATE, "#0")));
                }
                if (mnemonic.equals("MULS")){
                    return this.checkChildrenSyntax() && this.children.get(0).isRegister() && this.children.get(1).isRegister() && this.children.get(2).equals(this.children.get(0));
                }
            }
            if (this.children.size() == 2){
                return this.checkChildrenSyntax() && this.children.get(0).isRegister() && this.children.get(1).isRegister();
            }
        }
        // storage operations for stack pointers
        if(STACK_POINTER_STORAGE.contains(mnemonic)){
            return this.checkChildrenSyntax() && this.children.size() == 3 && this.children.get(0).isRegister() && this.children.get(1).isStackPointer() && this.children.get(2).isImmediate();
        }
        // offset operations for stack pointers
        if (STACK_POINTER_OFFSET.contains(mnemonic)) {
            return this.checkChildrenSyntax() && this.children.size() == 2 && this.children.get(1).isStackPointer() && this.children.get(0).isImmediate();
        }
        // branch operations
        if (BRANCH_OPERATIONS.contains(mnemonic)){
            return this.checkChildrenSyntax();
        }

        return false;
    }
}


