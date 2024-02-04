package src;

public class BinaryCodeSymbol {
    private String mnemonic;
    private String binary;

    public BinaryCodeSymbol(String mnemonic, String binary) {
        this.mnemonic = mnemonic;
        this.binary = binary;
    }

    public String getBinary() {
        return this.binary;
    }

    public String getMnemonic() {
        return this.mnemonic;
    }
}
