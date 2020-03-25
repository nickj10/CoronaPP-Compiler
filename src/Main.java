import lexic_analysis.Scanner;
import model.CompilerManager;

public class Main {
  public static void main(String[] args) {
      CompilerManager dictionary = new CompilerManager();
      dictionary.showList();
      Scanner scanner = new Scanner();
      scanner.readSourceCode();
      //scanner.scanSourceCode("a");

      //String text = scanner.removeComments();
      String[] text2 = scanner.removeSpecialCharacters();
      System.out.println(text2);
  }
}
