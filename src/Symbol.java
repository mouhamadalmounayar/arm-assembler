package src;

public class Symbol {
    private int address;
    private Token token;

    public Symbol(int address, Token token) {
        this.address = address;
        this.token = token;
    }

    public Token getName() {
        return this.token;
    }

    @Override
    public String toString(){
        return this.token.toString() + ":" + this.address;
    }
    public int getAddress() {
        return this.address;
    }
}