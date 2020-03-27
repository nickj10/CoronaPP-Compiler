package syntax;

import exceptions.ParserException;
import java.util.LinkedList;

public class Parser {
  private LinkedList<Token> tokens;
  private Token nextToken;

  public Parser() {
    this.tokens = new LinkedList<Token>();
  }

  public void parse(LinkedList<Token> tokens) throws ParserException {
    this.tokens = (LinkedList<Token>) tokens.clone();
    this.nextToken = this.tokens.getFirst();

    // If there is no other symbol left, the next token is EPSILON
    if(nextToken.tokenType != TokenType.EPSILON) {
      throw new ParserException("Unexpected symbol %s found", nextToken);
    }
  }

  private void nextToken()
  {
    tokens.pop();
    // at the end of input we return an epsilon token
    if (tokens.isEmpty())
      nextToken = new Token(TokenType.EPSILON, "");
    else
      nextToken = tokens.getFirst();
  }
}
