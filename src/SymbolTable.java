package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SymbolTable {
    private final List<Symbol> symbolTable;

    public SymbolTable() {
        this.symbolTable = new ArrayList<>();
    }

    public void addSymbol(Symbol symbol) {
        symbolTable.add(symbol);
    }

    public Optional<Symbol> findSymbol(String symbolName) {
        for (Symbol symbol : this.symbolTable) {
            if (symbol.getName().lexeme().equalsIgnoreCase(symbolName)) {
                return Optional.of(symbol);
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString(){
        return this.symbolTable.toString();
    }

}
