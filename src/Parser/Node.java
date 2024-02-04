package src.Parser;

import src.Token;
import src.TokenType;

import java.util.List;


public abstract class Node {
    protected Token node;
    protected List<Node> children;

    public abstract boolean checkSyntax();

    public boolean isImmediate() {
        return (this.node.type().equals(TokenType.IMMEDIATE));
    }

    public boolean isRegister() {
        return (this.node.type().equals(TokenType.REGISTER));
    }
    public boolean isStackPointer() {
        return (this.node.type().equals(TokenType.REGISTER) && this.node.lexeme().equalsIgnoreCase("sp"));
    }

    public Token getNode() {
        return this.node;
    }

    public List<Node> getChildren() {
        return this.children;
    }

    public boolean checkChildrenSyntax() {
        for (Node child : this.children) {
            if (!child.checkSyntax()) return false;
        }
        return true;
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        if (!node.getNode().equals(this.getNode())) return false;
        for (int i = 0; i < node.getChildren().size(); i++) {
            if (!node.getChildren().get(i).equals(this.getChildren().get(i))) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return node.toString() + children.toString();
    }


}

