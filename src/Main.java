import lexic_analysis.Scanner;
import lexic_analysis.TokenInfo;
import model.CompilerManager;

import SymbolTable.SymbolTable;
import SymbolTable.Symbol;
public class Main {
  public static void main(String[] args) {
      CompilerManager dictionary = new CompilerManager();
      dictionary.showList();
    Scanner scanner = new Scanner();
    //Parser parser = new Parser();

    scanner.generateTokens();
    while (scanner.getNextToken() != null) {
      //parser.getToken(scanner.sendNextToken());
      TokenInfo tokenInfo = scanner.sendNextToken();
      System.out.println(tokenInfo.getToken() + " - " + "Scope: " + tokenInfo.getScope() + " Line number: " + tokenInfo.getDeclaredAtLine());
    }


    // Creating a new . Remember, it is a Singleton so you need to call the getInstance() method
    SymbolTable mytable = SymbolTable.getInstance();
    // Creating some symbols from the following snipet:
    /*
      int a = 10;   $ line 0
      if(a > 9){    $ line 1
        int b = 8   $ line 2
      }             $ line 3
     */
    Symbol _a = new Symbol("a", "int", "global", 0, 4);
    Symbol _if1 = new Symbol("if1", "if-statement", "global", 1, 0);
    _if1.setChildTable();  // Setting a child table to store the scope of the if statement
    Symbol _b = new Symbol("b", "int", "local", 2, 4);
    _if1.getChildTable().addSymbol(_b); // Adding the variable(s) inside the if statement
    _b.setParentTable(mytable); // Setting the parent tables of the variables inside the if statement
                                // in this case is just the root table: 'mytable'
    // Adding the rest of the symbols to 'mytable'
    mytable.addSymbol(_a);
    mytable.addSymbol(_if1);

    // Printing to check everything's perfect
    System.out.println(mytable.toString());
  }
}
