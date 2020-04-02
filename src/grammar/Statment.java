package grammar;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Statment {

    // Init Vars
    public Token nonTerminal;
    public Token[] definitions = new Token[100];

    // Statment constructor
    public Statment() {

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