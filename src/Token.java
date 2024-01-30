package src;

public class Token {
    private TokenType type;
    private String lexeme;
    int LineNumber;

    public Token(TokenType type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }

    public TokenType getType(){
        return this.type;
    }

    public String getLexeme() {
        return this.lexeme;
    }
}