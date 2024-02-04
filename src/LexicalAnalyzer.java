package src;

import java.util.ArrayList;
import java.util.List;
import src.Token; 

public class LexicalAnalyzer {
    private final List<Token> tokens;

    public LexicalAnalyzer() {
        this.tokens = new ArrayList<>();
    }

    public void addToken(Token token) {
        this.tokens.add(token);
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
