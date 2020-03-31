package syntax;

import exceptions.ParserException;
import model.CompilerManager;
import model.Dictionary;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Token {
  /*public final TokenType tokenType;
  public final String sequence;
  public Token(TokenType tokenType, String sequence) {
    this.tokenType = tokenType;
    this.sequence = sequence;
  } */
  //PATTERNS
  private final String IDENTIFIER = "^([a-zA-Z]*)$";
  private final String NUMBER = "^([0-9]+)$";

  public String existsInDictionary(String str) {
    CompilerManager manager = new CompilerManager();
    if (manager.getWord(str) != null) {
      return manager.getWord(str);
    }
    return null;
  }

  public String checkPattern (String str) {
    Pattern p1 = Pattern.compile(IDENTIFIER);
    Pattern p2 = Pattern.compile(NUMBER);
    Matcher m1 = p1.matcher(str);
    Matcher m2 = p2.matcher(str);
    if (m1.matches()) { return "IDENTIFIER"; }
    if (m2.matches()) { return "NUMBER"; }
    return null;
  }



}
