package exceptions;

public class GrammarException extends Exception {
    private String err;

    public GrammarException(String err) {
        super("Error, unknown token: \"" + err + "\"");
        this.err = err;
    }
}
