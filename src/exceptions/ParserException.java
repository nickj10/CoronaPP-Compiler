package exceptions;

import syntax.Token;

public class ParserException extends Exception {
  private String message;
  private Token token;

  public ParserException(String message, Token token) {
    super(message);
    this.message = message;
    this.token = token;
  }
}
