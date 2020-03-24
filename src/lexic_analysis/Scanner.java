package lexic_analysis;

import java.io.*;

public class Scanner {
  private static final String PATH="data/source_code.txt";

  /**
   * Reads source code file
   */
  public void readSourceCode() {
    File source = new File(PATH);
    String str = "";
    BufferedReader br = null;

    try {
      br = new BufferedReader(new FileReader(source));
    } catch (FileNotFoundException e) {
      System.out.println("\nError, file can't be found.");
      e.printStackTrace();
    } finally {
      while (true) {
        try {
          if ((str = br.readLine())!=null) {
            System.out.println(str);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
  public void removeSpecialCharacters(String[] delim) {

  }

}
