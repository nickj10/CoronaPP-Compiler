package exceptions;

public class SemanticException extends Exception {
    private String error;

    public SemanticException (String error) {
        super("Error. " + error);
        this.error = error;
    }
}
