package syntax;

public class Token {

  // immutable object
  public final TokenType tokenType;
  public final String sequence;

  public Token(TokenType tokenType, String sequence) {
    this.tokenType = tokenType;
    this.sequence = sequence;
  }
}
