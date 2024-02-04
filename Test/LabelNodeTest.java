package Test;

import org.junit.jupiter.api.Test;
import src.Parser.InstructionNode;
import src.Parser.LabelNode;
import src.Parser.Node;
import src.Parser.OperandNode;
import src.Token;
import src.TokenType;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LabelNodeTest {
    @Test
    void checkSyntaxTestFalseSyntax(){
        Node label = new LabelNode(new Token(TokenType.LABEL, "") , List.of(
                new InstructionNode(new Token(TokenType.MNEMONIC, "ADDS"), Arrays.asList(new OperandNode(new Token(TokenType.REGISTER, "R1")), new OperandNode(new Token(TokenType.REGISTER, "R2")))
                )));
        assertFalse(label.checkSyntax());
    }
    @Test
    void checkSyntaxTextCorrectSyntax(){
        Node label = new LabelNode(new Token(TokenType.LABEL, "") , List.of(
                new InstructionNode(new Token(TokenType.MNEMONIC, "ADDS"), Arrays.asList(new OperandNode(new Token(TokenType.REGISTER, "R1")), new OperandNode(new Token(TokenType.REGISTER, "R2")), new OperandNode(new Token(TokenType.REGISTER, "R3"))
                ))));
        assertTrue(label.checkSyntax());
    }
}