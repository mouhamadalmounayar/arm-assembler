package src.Parser;

import src.Token;

import java.util.List;


public abstract class Node {
    protected Token node;
    protected List<Node> children;
}

