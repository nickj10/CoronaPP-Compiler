package syntatic_analysis;

import java.util.regex.Pattern;

public class Token {

    // Init Vars | Declare Language Alphabet & Token Patterns
    private static final Pattern VALID_TOKEN = Pattern.compile("^[a-zA-Z0-9_\\{\\}\\[\\]\\-\\|]*$");
    private static final Pattern ASSIGNMENT_TOKEN = Pattern.compile("^\\-$");
    private static final Pattern PIPE_TOKEN = Pattern.compile("^\\|$");
    private static final Pattern TERMINAL_TOKEN = Pattern.compile("^(EPSILON|NUMBER|IDENTIFIER|COR_OPEN|COR_CLCSED|RLTNL_EQ|RLTNL_NTEQ|ARTMTC_MLT|ARTMTC_MD|ARTMTC_DV|ARTMTC_SM|ARTMTC_RS|ASSGN_EQ|LOGIC_OR|LOGIC_AND|RELATIONAL_NOTEQ|RELATIONAL_EQ|IF|ELSE|WHILE|INT|BOOL|CONST|CHAR|NULL|RETURN|FLOAT|DOT_COMA|DOT|PRNTSS_OPEN|PRNTSS_CLOSED)$");
    private static final Pattern NONTERMINAL_TOKEN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]*$");
    private static final Pattern IDENTIFIER = Pattern.compile("^([a-zA-Z]*)$");
    private static final Pattern NUMBER = Pattern.compile("^([0-9]+)$");
    public String token;
    public String type;
    public String message;
    public String lexeme;

    // Token constructor
    public Token(String tok) {
        token = tok;
        if (tok.equals("")) {
            checkLexemeType();
        }
        validate();
        if (type == null || !type.equals("ERROR")) {
            type();
        }
    }

    public Token(String tok, String lex) {
        lexeme = lex;
        if (tok.equals("")) {
            checkLexemeType();
        }
        validate();
        if (type == null || !type.equals("ERROR")) {
            type();
        }
    }

    public Token() {
    }

    // Sets error and erroemessage if token is not valid
    protected void validate() {
        if (!VALID_TOKEN.matcher(token).matches()) {
            type = "ERROR";
            message = "ERROR CODE 0: Input not according to format";
        }
    }

    // Type the given token
    protected void type() {
        if (ASSIGNMENT_TOKEN.matcher(token).matches()) {
            type = "ASSIGNMENT";
            message = "assignment";
        } else if (PIPE_TOKEN.matcher(token).matches()) {
            type = "PIPE";
            message = "pipe";
        } else if (TERMINAL_TOKEN.matcher(token).matches()) {
            type = "TERMINAL";
            message = "terminal";
        } else if (NONTERMINAL_TOKEN.matcher(token).matches()) {
            type = "NONTERMINAL";
            message = "nonterminal";
        } else {
            type = "ERROR";
            message = "ERROR CODE 0: Input not according to format";
        }
    }

    protected void checkLexemeType() {
        if (NUMBER.matcher(lexeme).matches()) {
            token = "NUMBER";
        } else if (IDENTIFIER.matcher(lexeme).matches()) {
            token = "IDENTIFIER";
        } else {
            type = "ERROR";
            message = "ERROR CODE 0: Input not according to format";
        }
    }

    /*public String existsInDictionary() {
        CompilerManager manager = new CompilerManager();
        if (manager.getWord(lexeme) != null) {
            return manager.getWord(lexeme);
        }
        return null;
    }*/

    // Print Oneline View
    public void oneline() {
        System.out.printf("%1$-15s\t%2$-1s\n", token, message);
    }

    // Print all vars for token
    public void debug() {
        System.out.println("Token: " + token);
        System.out.println("Type: " + type);
        System.out.println("Message: " + message);
    }
}