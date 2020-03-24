package lexic_analysis;

import model.CompilerManager;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {
  private static final String PATH="data/source_code.txt";
  private CompilerManager compilerManager;
  private String sourceCode;

  public Scanner () {
    compilerManager = new CompilerManager();
  }

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
            this.sourceCode = str;
            String test = "int";
            System.out.println("Token is: " + generateToken(test));
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
  public String removeSpecialCharacters() {
    String result = "";
    for(int i = 0; i < sourceCode.length(); i++) {
      if (sourceCode.charAt(i) != ' ' && sourceCode.charAt(i) != '\n') {
        result = result.concat(String.valueOf(sourceCode.charAt(i)));
      }
    }
    return result;
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

  public String generateToken (String str) {
    String token="";
    if (!str.equals("IDENTIFIER") || !str.equals("NUMBER")) {
      token = compilerManager.getWord(str);
      if (token.equals("")) {
        //Not found
      }
      else {
        return token;
      }
    }
    return str;
  }

}
