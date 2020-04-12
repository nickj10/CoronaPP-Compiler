package lexic_analysis;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {
  private String file;
  private String sourceCode = "";
  private LinkedList<TokenInfo> preparedTokens;
  private String[] words;

  public Scanner (String file) {
    this.file = file;
    readSourceCode();
    generateTokens();
  }

  /**
   * Reads source code file
   */
  public void readSourceCode() {
    File source = new File(file);
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
    String scope = "global";
    int scope_level = 0;
    LinkedList<TokenInfo> tokens = new LinkedList<>();
    for (int i = 0; i < lines.length; i++) {
      System.out.println(lines[i]);
      String[] lexemes = lines[i].replaceAll("\\r\\n|\\r|\\n", " ").trim().split("\\s+");
      // Check if a new scope is opened
      if (lines[i].contains("{")) {
        scope_level++;
        scope = "local";
      }
      // Closing the scope level
      if (lines[i].contains("}")) {
        scope_level--;
        if (scope_level == 0) {
          scope = "global";
        }
      }
      for (String lexema : lexemes) {
        if (lexema.contains("(")) {
          lexema = lexema.substring(1);
          tokens.push(new TokenInfo("(", "", "",scope, i+1, 0));
          tokens.push(new TokenInfo(lexema, "", "", scope, i + 1,0));
          //tokens.push(new TokenInfo(scope, "(", i + 1));
          //tokens.push(new TokenInfo(scope, lexema, i + 1));
        } else if (lexema.contains(")")) {
            lexema = lexema.substring(0, lexema.length() - 1);
          tokens.push(new TokenInfo(lexema, "", "", scope, i + 1,0));
          tokens.push(new TokenInfo(")", "",  "",scope, i+1, 0));
          //tokens.push(new TokenInfo(scope, lexema, i + 1));
          //tokens.push(new TokenInfo(scope, ")", i + 1));
        } else if (lexema.contains(";")) {
          lexema = lexema.substring(0, lexema.length() - 1);
          tokens.push(new TokenInfo(lexema, "", "", scope, i + 1,0));
          tokens.push(new TokenInfo(";", "", "",scope, i+1, 0));
          //tokens.push(new TokenInfo(scope, lexema, i + 1));
          //tokens.push(new TokenInfo(scope, ";", i + 1));
        } else {
          tokens.push(new TokenInfo(lexema, "",  "", scope, i + 1,0));
          //tokens.push(new TokenInfo(scope, lexema, i + 1));
        }
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
