import lexic_analysis.Scanner;
import model.Dictionary;
import model.DictionaryManager;

import java.io.FileNotFoundException;

public class Main {
  public static void main(String[] args) {
      DictionaryManager dictionaryManager = new DictionaryManager();
      dictionaryManager.showList();
      Scanner scanner = new Scanner();
      scanner.readSourceCode();

  }
}
