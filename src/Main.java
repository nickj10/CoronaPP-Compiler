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

      scanner.generateTokens();
      while (scanner.getNextToken() != null) {
        parser.getToken(scanner.sendNextToken());
      }
  }
}
