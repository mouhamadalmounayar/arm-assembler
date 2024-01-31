package Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.*;
import src.Parser.InstructionNode;
import src.Parser.LabelNode;
import src.Parser.Node;
import src.Parser.OperandNode;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FirstPassTest {

    FirstPass firstPass;
    Token addToken;
    Token subToken;
    Token register1;
    Token register2;
    Token register3;
    Token immediate;
    Token label;


    @BeforeEach
    public void setup() {
        firstPass = new FirstPass("file.txt", new LexicalAnalyzer(), new SymbolTable());
        addToken = new Token(TokenType.MNEMONIC, "ADD");
        subToken = new Token(TokenType.MNEMONIC, "SUB");
        register1 = new Token(TokenType.REGISTER, "R1");
        register2 = new Token(TokenType.REGISTER, "R2");
        register3 = new Token(TokenType.REGISTER, "R3");
        immediate = new Token(TokenType.IMMEDIATE, "#10");
        label = new Token(TokenType.LABEL, "");

    }

    @Test
    public void organizeTokensTestOneLine() {
        List<Token> tokens = new ArrayList<>(Arrays.asList(addToken, register1, register2, immediate, new Token(TokenType.ENDOFLINE, "\n")));
        List<List<Token>> organizedTokens = firstPass.organizeTokens(tokens);
        assertEquals(1, organizedTokens.size());
        tokens.remove(tokens.size() - 1);
        assertEquals(tokens, organizedTokens.get(0));
    }

    @Test
    public void organizeTokensTestMultipleLines() {
        List<Token> tokens = new ArrayList<>(new ArrayList<>(Arrays.asList(addToken, register1, register2, immediate, new Token(TokenType.ENDOFLINE, "\n"), subToken, register1, register2, immediate, new Token(TokenType.ENDOFLINE, "\n"))));
        List<List<Token>> organizedTokens = firstPass.organizeTokens(tokens);
        assertEquals(2, organizedTokens.size());
    }

    @Test
    public void buildTreeTestDefault() {
        List<List<Token>> organizedTokens = Arrays.asList(
                Arrays.asList(addToken, register1, register2, immediate),
                Arrays.asList(subToken, register1, register2, immediate)
        );
        Node tree = new LabelNode(label, Arrays.asList(
                new InstructionNode(addToken, Arrays.asList(new OperandNode(register1), new OperandNode(register2), new OperandNode(immediate))),
                new InstructionNode(subToken, Arrays.asList(new OperandNode(register1), new OperandNode(register2), new OperandNode(immediate)))
        ));
        assertEquals(tree, firstPass.buildTree(organizedTokens));

    }

}