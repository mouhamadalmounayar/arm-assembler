package src;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FirstPass {
    private String filePath;
    private LexicalAnalyzer lexo;
    private SymbolTable symbolTable;

    public FirstPass(String filePath, LexicalAnalyzer lexo, SymbolTable symbolTable) {
        this.filePath = filePath;
        this.lexo = lexo;
        this.symbolTable = symbolTable;
    }

    public void execute() {
        Path inputFilePath = Paths.get(this.filePath);
        // TO DO

    }
}
