package tester;
import SymbolTable.SymbolTable;
import SymbolTable.Symbol;

public class SymbolTableTest {

    public static void main(String[] args) {
        System.out.print("Building Symbol table with the following code snippet: \n" +
                "          int a = 10;   $ line 0\n"+
                "          if(a > 9){    $ line 1\n"+
                "            int b = 8   $ line 2\n"+
                "          }             $ line 3\n");

        // TEST
        // Creating a new SymbolTable. Remember, it is a Singleton so you need to call the getInstance() method
        SymbolTable mytable = SymbolTable.getInstance();
        // Creating some symbols from the following snipet:
        /*
          int a = 10;   $ line 0
          if(a > 9){    $ line 1
            int b = 8   $ line 2
          }             $ line 3
         */
        Symbol _a = new Symbol("a", "INT", "int", "global", 0, 4);
        Symbol _if1 = new Symbol("if", "IF", "if-statement", "global", 1, 0);
        Symbol _b = new Symbol("b", "IDENTIFIER", "int", "local", 2, 4);
        Symbol _if2 = new Symbol("if", "IF", "if-statement", "local", 3, 0);
        Symbol _c = new Symbol("c", "IDENTIFIER", "int", "local", 4, 4);

        mytable.addSymbol(_a);
        mytable.addSymbol(_if1);
        mytable.addSymbol(_b);
        mytable.addSymbol(_if2);
        mytable.addSymbol(_c);

        System.out.println("Done!");
        // Printing to check everything's perfect
        System.out.println(mytable.toString());

        System.out.println("Trying to store variable \"" + _a.getId() + "\"");
        // checking if variable exists to whether or not store it (hint: It already exists in this example)
        if(!mytable.alreadyExists(_a)) {
            System.out.println("Variable \"" + _a.getId() + "\" NOT inside the table. Storing it...");
            mytable.addSymbol(_a);
        }else {
            System.out.println("ERROR: Variable \"" + _a.getId() + "\" ALREADY inside the table, ignoring...");
        }

        System.out.println("Looking for variable 'b' inside the first if-statement (if1)");
        Symbol found_b = mytable.getSymbol("b", "if1");
        found_b.setStackPointer(24);
        System.out.println("Symbol b: " + found_b.toString());
        System.out.println("Changing stack pointer in symbol found b to 24 \n Table with changes in b:");
        System.out.println(mytable.toString());

    }
}
