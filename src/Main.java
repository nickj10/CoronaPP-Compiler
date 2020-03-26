import lexic_analysis.Scanner;
import model.CompilerManager;

public class Main {
  public static void main(String[] args) {
      CompilerManager dictionary = new CompilerManager();
      dictionary.showList();
      Scanner scanner = new Scanner();
      String[] words = scanner.getWords();
      for (int i = 0; i < words.length; i++) {
          String token = scanner.generateToken(words[i]);
          System.out.println(token);

      }


  }
}
