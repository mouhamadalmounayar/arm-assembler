package Test;

import org.junit.Test;
import src.Parser.InstructionNode;
import src.Parser.Node;
import src.Parser.OperandNode;
import src.Token;
import src.TokenType;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class InstructionNodeTest {
   Node register1 = new OperandNode(new Token(TokenType.REGISTER, "Rd"));
   Node register2 = new OperandNode(new Token(TokenType.REGISTER, "Rm"));
   Node register3 = new OperandNode(new Token(TokenType.REGISTER, "Rn"));
   Node immediate = new OperandNode(new Token(TokenType.IMMEDIATE,"imm5"));

   Node instruction = new InstructionNode(new Token(TokenType.MNEMONIC , "LSLS"), new ArrayList<>(Arrays.asList(register1, register2,register3)));
   Node instruction2 = new InstructionNode (new Token(TokenType.MNEMONIC , "LSLS"), new ArrayList<>(Arrays.asList(register1, register2, instruction)));
   Node instruction3 = new InstructionNode(new Token(TokenType.MNEMONIC , "LSLS"), new ArrayList<>(Arrays.asList(register1, register2, immediate)));

   Node instruction4 = new InstructionNode(new Token(TokenType.MNEMONIC , "ADDS") , new ArrayList<>(Arrays.asList(register1 , immediate)));

   Node instruction5 = new InstructionNode(new Token(TokenType.MNEMONIC , "ADDS") , new ArrayList<>(Arrays.asList(register1 , register2 , immediate)));
   Node instruction6 = new InstructionNode(new Token(TokenType.MNEMONIC , "SUBS") , new ArrayList<>(Arrays.asList(register1 , register2 , register3)));
   Node instruction7 = new InstructionNode(new Token(TokenType.MNEMONIC , "SUBS") , new ArrayList<>(Arrays.asList(register1 , register2)));
   @Test
   public void checkSyntaxTestRegisterOperations(){
       assertFalse(instruction.checkSyntax());
       assertFalse(instruction2.checkSyntax());
       assertTrue(instruction3.checkSyntax());
   }
   @Test
   public void checkSyntaxTestArithmeticOperations(){
       assertTrue(instruction4.checkSyntax());
       assertTrue(instruction5.checkSyntax());
       assertTrue(instruction6.checkSyntax());
       assertFalse(instruction7.checkSyntax());
   }
}