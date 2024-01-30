package src.Parser;

import src.Token;

import java.util.ArrayList;
import java.util.List;

public class InstructionNode extends Node{
    public InstructionNode(Token node){
        this.node = node;
        this.children = new ArrayList<>();
    }

    public InstructionNode(Token node, List<Node> children){
        this(node);
        this.children = children;
    }
}
