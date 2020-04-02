package lexic_analysis;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import model.CompilerManager;

import java.io.*;

public class Scanner {
  private static final String PATH = "data/source_code.txt";
  private CompilerManager compilerManager;
  private String sourceCode = "";
  private LinkedList<TokenInfo> preparedTokens;

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
  public LinkedList<TokenInfo> removeSpecialCharacters() {
    String[] lines = this.sourceCode.split("\\r\\n|\\r|\\n");

    LinkedList<TokenInfo> tokens = new LinkedList<>();
    for (int i = 0; i < lines.length; i++) {
      System.out.println(lines[i]);
      String[] lexemes = lines[i].replaceAll("\\r\\n|\\r|\\n|;", " ").split("\\s+|;");
      for (int j = 0; j < lexemes.length; j++) {
        tokens.push(new TokenInfo("global", lexemes[j], i));
      }
    }
    return tokens;
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
    this.sourceCode = result.replaceAll("\\/\\$[\\s\\S]*?\\$\\/", "").trim();

  }

  public void scanSourceCode() {
    removeComments();
    preparedTokens = removeSpecialCharacters();
  }

  public void generateTokens() {
    scanSourceCode();
  }

  public TokenInfo getNextToken() {
    try {
      return preparedTokens.getLast();
    } catch (NoSuchElementException e) {
      return null;
    }
  }

  public TokenInfo sendNextToken() {
    return preparedTokens.removeLast();
  }
}
