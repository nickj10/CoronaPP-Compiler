package model;

public class Word {
    private String token;
    private String lexeme;
    private String description;

    public Word(String token, String lexeme, String description) {
        this.token = token;
        this.lexeme = lexeme;
        this.description = description;
    }

    public String getToken() {
        return token;
    }

    public String getLexeme() {
        return lexeme;
    }

    public String getDescription() {
        return description;
    }
}
