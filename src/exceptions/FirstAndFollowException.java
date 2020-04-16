package exceptions;

public class FirstAndFollowException extends Exception {
    private String err;

    public FirstAndFollowException(String err) {
        super("Error, rule not found in the first and follow table for the expression: \"" + err + "\"");
        this.err = err;
    }
}
