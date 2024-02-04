package src;

public record Token(TokenType type, String lexeme) {

    @Override
    public String toString() {
        return this.lexeme + "(" + this.type.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Token)) return false;
        Token token = (Token) o;
        return token.type() == this.type() && token.lexeme().equals(this.lexeme());
    }
}