package src;

public class Token {
    private TokenType type;
    private String lexeme;

    public Token(TokenType type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
    }

    public TokenType getType() {
        return this.type;
    }

    public String getLexeme() {
        return this.lexeme;
    }

    @Override
    public String toString() {
        return this.lexeme + "(" + this.type.toString()+ ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Token)) return false;
        Token token = (Token) o;
        return token.getType() == this.getType() && token.getLexeme().equals(this.getLexeme());
    }
}