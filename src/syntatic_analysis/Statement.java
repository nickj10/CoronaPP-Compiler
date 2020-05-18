package syntatic_analysis;

public class Statement {

    // Init Vars
    public Token nonTerminal;
    public Token[] definitions = new Token[100];

    // Statement constructor
    public Statement() {

    }

    // Print oneline
    public void oneline() {
        String definitionString = "";
        for (Token token : definitions) {
            if (token != null) {
                definitionString += token.token + " ";
            }
        }
        System.out.println(nonTerminal.token + " - " + definitionString);
    }

    // toString
    public String toString() {
        String definitionString = "";
        for (Token token : definitions) {
            if (token != null) {
                definitionString += token.token + " ";
            }
        }
        return nonTerminal.token + " - " + definitionString;
    }
}