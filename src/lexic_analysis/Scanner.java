package lexic_analysis;

import java.io.*;

public class Scanner {
  private static final String PATH="data/source_code.txt";
  private String sourceCode = "";
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
      //x (true) {
        try {
          while ((str = br.readLine())!=null) {
            System.out.println(str);
            this.sourceCode = this.sourceCode.concat(str);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      //}
    }
  }

  /**
   * Removes special characters like ' ' and '\n'
   * @return string without special characters
   */
  public String removeSpecialCharacters() {
    String result = "";
    for(int i = 0; i < sourceCode.length(); i++) {
      if (sourceCode.charAt(i) != ' ' && sourceCode.charAt(i) != '\n') {
        result = result.concat(String.valueOf(sourceCode.charAt(i)));
      }
    }
    return result;
  }
}
