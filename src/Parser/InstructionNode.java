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

            return (this.checkChildrenSyntax() && this.children.size() == 3 && this.children.get(0).isRegister() && this.children.get(1).isRegister() && this.children.get(2).isImmediate());
        }
        // arithmetic operation registers
        if (BASIC_ARITHMETIC_OPERATIONS.contains(mnemonic)) {
            if (this.children.size() == 3) {
                return (this.checkChildrenSyntax() && this.children.get(0).isRegister() && this.children.get(1).isRegister());
            }
            if (this.children.size() == 2) {
                return (this.checkChildrenSyntax() && this.children.get(0).isRegister() && this.children.get(1).isImmediate());
            } else {
                return false;
            }
        }
        // arithmetic operations
        if (ARITHMETIC_OPERATIONS.contains(mnemonic)) {
            return (this.checkChildrenSyntax() && this.children.get(0).isRegister() && this.children.get(1).isRegister() && this.children.size() == 2);
        }
        return true;
    }
}


