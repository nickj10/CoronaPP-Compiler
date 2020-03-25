package lexic_analysis;

import model.CompilerManager;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {
  private static final String PATH = "data/source_code.txt";
  private CompilerManager compilerManager;
  private String sourceCode = "";

  public Scanner() {
    compilerManager = new CompilerManager();
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
   * Removes comments
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
      // Remove block comments
      if (sourceCode.charAt(i) == '/') {
        if (sourceCode.charAt(i + 1) == '$') {
          while (sourceCode.charAt(i) != '$' && sourceCode.charAt(i + 1) != '/') {
            i++;
          }
          i++;
        }
      }
      result = result.concat(String.valueOf(sourceCode.charAt(i++)));
    }
    this.sourceCode = result;
  }

  public void scanSourceCode(String str) {
    //Patterns
    String regexIdentifier = "^([a-zA-Z][a-zA-Z]*)$";
    String regexNumber = "^([0-9]+)$";
    Pattern patternIdentifier = Pattern.compile(regexIdentifier);
    Pattern patternNumber = Pattern.compile(regexNumber);

    Matcher matcherIdentifier = patternIdentifier.matcher(str);
    Matcher matcherNumber = patternNumber.matcher(str);

    if (matcherIdentifier.matches()) {
      generateToken("IDENTIFIER");
      return;
    }
    if (matcherNumber.matches()) {
      generateToken("NUMBER");
      return;
    }
    generateToken(str);
  }

  public String generateToken(String str) {
    String token = "";
    if (!str.equals("IDENTIFIER") || !str.equals("NUMBER")) {
      token = compilerManager.getWord(str);
      if (token.equals("")) {
        //Not found
      } else {
        return token;
      }
    }
    return str;
  }
}
