package syntax;

import exceptions.ParserException;
import java.util.LinkedList;

public class Parser {
  private String[] tokens;
  private Token nextToken;

  public Parser() {
    this.nextToken = new Token();

  }

  public void parse(String token) throws ParserException {
    /* If there is no other symbol left, the next token is EPSILON
    if (nextToken.tokenType != TokenType.EPSILON) {
      throw new ParserException("Unexpected symbol %s found", nextToken);
    }*/
  }

  public String getToken (String str) throws ParserException {
    String token;
    //Miramos si existe en nuestro diccionario
    token = nextToken.existsInDictionary(str);
    //Sino miramos qué patrón sigue
    if (token.equals("")) {
      token = nextToken.checkPattern(str);
      if (token == null) {
        throw new ParserException("This symbol is not found", str);
      }
    }
    //Añadir a la tabla de símbolos
    System.out.println(str + " - " + token);
    return token;
  }



}
