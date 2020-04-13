package exceptions;

public class ParserException extends Exception {
    private String message;
    private String token;

    public ParserException(String message, String token) {
        super(message);
        this.message = message;
        this.token = token;
    }
}
