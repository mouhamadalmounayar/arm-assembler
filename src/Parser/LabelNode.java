package src.Parser;

import src.Token;
import src.TokenType;

import java.util.ArrayList;
import java.util.List;

public class LabelNode extends Node {
    public LabelNode(Token node) {
        this.node = node;
        this.children = new ArrayList<>();
    }

    public LabelNode(Token node, List<Node> children) {
        this.node = node;
        this.children = children;
    }

    @Override
    public boolean checkSyntax() {
        return this.checkChildrenSyntax() && this.getNode().type().equals(TokenType.LABEL);
    }
}
