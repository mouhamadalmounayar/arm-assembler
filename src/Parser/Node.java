package src.Parser;

import src.Token;
import src.TokenType;

import java.util.List;


public abstract class Node {
    protected Token node;
    protected List<Node> children;

    public abstract boolean checkSyntax();

    public boolean isImmediate(){
        return (this.node.getType().equals(TokenType.IMMEDIATE));
    }

    public boolean isRegister(){
        return (this.node.getType().equals(TokenType.REGISTER));
    }

    public Token getNode(){
        return this.node;
    }

    public boolean checkChildrenSyntax(){
        for (Node child : this.children){
            if (!child.checkSyntax()) return false;
        }
        return true;
    }


}

