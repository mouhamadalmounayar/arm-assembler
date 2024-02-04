package src.Parser;

import src.Token;
import src.TokenType;

import java.util.ArrayList;

public class OperandNode extends Node {
    public OperandNode(Token node) {
        this.node = node;
        this.children = new ArrayList<>();
    }

    @Override
    public boolean checkSyntax() {
        TokenType operand = this.node.type();
        if (!this.children.isEmpty()) return false;
        return (operand.equals(TokenType.REGISTER) || operand.equals(TokenType.IMMEDIATE))  || (operand.equals(TokenType.LABEL));
    }
}
