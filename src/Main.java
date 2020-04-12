import model.CompilerManager;
import SymbolTable.SymbolTable;
import SymbolTable.Symbol;

public class Main {
  public static void main(String[] args) {
    String sourceFile = args[0];
    String grammarFile = args[1];
    String dictionaryFile = args[2];

    CompilerManager compilerManager = new CompilerManager(sourceFile, grammarFile, dictionaryFile);
    compilerManager.compile();

    // TEST
    // Creating a new . Remember, it is a Singleton so you need to call the getInstance() method
    SymbolTable mytable = SymbolTable.getInstance();
    // Creating some symbols from the following snipet:
    /*
      int a = 10;   $ line 0
      if(a > 9){    $ line 1
        int b = 8   $ line 2
      }             $ line 3
     */
    Symbol _a = new Symbol("a", "INT", "int", "global", 0, 4);
    Symbol _if1 = new Symbol("if1", "IF", "if-statement", "global", 1, 0);
    _if1.setChildTable();  // Setting a child table to store the scope of the if statement
    Symbol _b = new Symbol("b", "IDENTIFIER", "int", "local", 2, 4);
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
