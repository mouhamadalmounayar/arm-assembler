package src;


import java.util.ArrayList;
import java.util.List;

public class BinaryMapping {
    private List<BinaryCodeSymbol> map = new ArrayList<>();

    public BinaryMapping() {
        // duplicates
        map.add(new BinaryCodeSymbol("ADDS_REGISTERS", "0001100"));
        map.add(new BinaryCodeSymbol("ADDS_IMMEDIATES", "0001110"));
        map.add(new BinaryCodeSymbol("SUBS_REGISTERS", "0001101"));
        map.add(new BinaryCodeSymbol("SUBS_IMMEDIATES", "0001111"));
        map.add(new BinaryCodeSymbol("CMP_IMMEDIATES", "00101"));
        map.add(new BinaryCodeSymbol("LSLS_3", "00000"));
        map.add(new BinaryCodeSymbol("LSRS_3", "00001"));
        map.add(new BinaryCodeSymbol("ASRS_3", "00010"));

        map.add(new BinaryCodeSymbol("ADDS", "00110"));
        map.add(new BinaryCodeSymbol("SUBS", "00111"));
        map.add(new BinaryCodeSymbol("LSLS", "0100000010"));
        map.add(new BinaryCodeSymbol("LSRS", "0100000011"));
        map.add(new BinaryCodeSymbol("ASRS", "0100000100"));
        map.add(new BinaryCodeSymbol("CMP", "010001010"));
        map.add(new BinaryCodeSymbol("MOVS", "00100"));
        map.add(new BinaryCodeSymbol("ANDS", "0100000000"));
        map.add(new BinaryCodeSymbol("EORS", "0100000001"));
        map.add(new BinaryCodeSymbol("LSLS", "0100000010"));
        map.add(new BinaryCodeSymbol("LSRS", "0100000011"));
        map.add(new BinaryCodeSymbol("ASRS", "0100000100"));
        map.add(new BinaryCodeSymbol("ADCS", "0100000101"));
        map.add(new BinaryCodeSymbol("SBCS", "0100000110"));
        map.add(new BinaryCodeSymbol("RORS", "0100000111"));
        map.add(new BinaryCodeSymbol("TST", "0100001000"));
        map.add(new BinaryCodeSymbol("RSBS", "0100001001"));
        map.add(new BinaryCodeSymbol("CMN", "0100001011"));
        map.add(new BinaryCodeSymbol("ORRS", "0100001100"));
        map.add(new BinaryCodeSymbol("MULS", "0100001101"));
        map.add(new BinaryCodeSymbol("BICS", "0100001110"));
        map.add(new BinaryCodeSymbol("MVNS", "0100001111"));

        map.add(new BinaryCodeSymbol("STR" , "10010"));
        map.add(new BinaryCodeSymbol("LDR" , "10011"));

        map.add(new BinaryCodeSymbol("ADD" , "101100000"));
        map.add(new BinaryCodeSymbol("SUB" , "101100001"));

        map.add(new BinaryCodeSymbol("R0", "000"));
        map.add(new BinaryCodeSymbol("R1", "001"));
        map.add(new BinaryCodeSymbol("R2", "010"));
        map.add(new BinaryCodeSymbol("R3", "011"));
        map.add(new BinaryCodeSymbol("R4", "100"));
        map.add(new BinaryCodeSymbol("R5", "101"));
        map.add(new BinaryCodeSymbol("R6", "110"));
        map.add(new BinaryCodeSymbol("R7", "111"));
    }

    public List<BinaryCodeSymbol> getMap() {
        return this.map;
    }

    public String findMnemonic(String mnemonic) {
        for (BinaryCodeSymbol symbol : this.map) {
            if (symbol.getMnemonic().equalsIgnoreCase(mnemonic)) {
                return symbol.getBinary();
            }
        }
        return null;
    }

}
