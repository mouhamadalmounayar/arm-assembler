package src;

public class Symbol {
    private int address;
    private String name;

    public Symbol(int address, String name) {
        this.address = address;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString(){
        return this.name + ":" + this.address;
    }
    public int getAddress() {
        return this.address;
    }
}