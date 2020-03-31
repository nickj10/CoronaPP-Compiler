package lexic_analysis;

import model.CompilerManager;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {
  private static final String PATH = "data/source_code.txt";
  private CompilerManager compilerManager;
  private String sourceCode = "";
  private String[] words;

  public Scanner() {

    compilerManager = new CompilerManager();
    readSourceCode();
  }

  /**
   * Reads source code file
   */
  public void readSourceCode() {
    File source = new File(PATH);
    StringBuilder strBuilder = new StringBuilder();
    String str = "";
    BufferedReader br = null;

    try {
      br = new BufferedReader(new FileReader(source));
    } catch (FileNotFoundException e) {
      System.out.println("\nError, file can't be found.");
      e.printStackTrace();
    } finally {
      try {
        while ((str = br.readLine()) != null) {
          strBuilder.append(str + "\n");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      this.sourceCode = strBuilder.toString();
    }
  }

  /**
   * Removes whitespaces and ';' and separates elements into an array
   *
   * @return array of string without special characters
   */
  public String[] removeSpecialCharacters() {
    this.sourceCode = this.sourceCode.replaceAll("\\r\\n|\\r|\\n|;", " ");
    return this.sourceCode.split("\\s+");
  }

  /**
   * Removes inline and block comments
   */
  public void removeComments() {
    String result = "";
    int i = 0;
    while (i < sourceCode.length()) {
      // Remove inline comments
      if (sourceCode.charAt(i) == '$') {
        if (sourceCode.charAt(i + 1) == '$') {
          while (sourceCode.charAt(i) != '\n') {
            i++;
          }
          i++;
        }
      }
      result = result.concat(String.valueOf(sourceCode.charAt(i++)));
    }
    // Remove block comments
    this.sourceCode = result.replaceAll("\\/\\$[\\s\\S]*?\\$\\/", "");

  }

  public void scanSourceCode() {
    removeComments();
    words = removeSpecialCharacters();
  }

  public String[] generateTokens() {
    scanSourceCode();
    return words;
  }
}
