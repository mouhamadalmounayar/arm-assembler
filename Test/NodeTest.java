package Test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.Parser.Node;
import src.Parser.OperandNode;
import src.Token;
import src.TokenType;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {
    Node register;
    Node immediate;
    @BeforeEach
    void setUp() {
        register = new OperandNode(new Token(TokenType.REGISTER, "Rd"));
        immediate = new OperandNode(new Token(TokenType.IMMEDIATE, "imm5"));
    }

    @Test
    void isImmediate() {
        assertFalse(register.isImmediate());
        assertTrue(immediate.isImmediate());
    }

    @Test
    void isRegister() {
        assertTrue(register.isRegister());
        assertFalse(immediate.isRegister());
    }
}