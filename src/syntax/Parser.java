package syntax;

import exceptions.ParserException;
import syntatic_analysis.Token;

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

  public Token getToken (String lex) throws ParserException {
    if (lex.equals("")){
      return null;
    }
    Token token = new Token();
    token.lexeme = lex;

    //Miramos si existe en nuestro diccionario
    //token.token = token.existsInDictionary();

    //Sino miramos qué patrón sigue
    if (token.token.equals("")) {
      token = new Token(token.token, lex);
      if (token.type.equals("ERROR")) {
        throw new ParserException("This symbol is not found", lex);
      }
    } else {
      token = new Token(token.token);
      token.lexeme = lex;
    }
    //Añadir a la tabla de símbolos
    System.out.println(lex + " - " + token.token);
    return token;
  }

}
