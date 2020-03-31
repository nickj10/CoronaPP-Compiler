import exceptions.ParserException;
import lexic_analysis.Scanner;
import model.CompilerManager;
import syntax.Parser;

public class Main {
  public static void main(String[] args) throws ParserException {
      CompilerManager dictionary = new CompilerManager();
      dictionary.showList();
      Scanner scanner = new Scanner();
      Parser parser = new Parser();

      String[] words = scanner.generateTokens();
      //El parser pide uno por uno los tokens
      for (int i = 0; i < words.length; i++) {
          parser.getToken(words[i]);
      }
  }
}
