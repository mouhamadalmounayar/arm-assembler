package src;

import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer {
    private List<Token> tokens;

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
