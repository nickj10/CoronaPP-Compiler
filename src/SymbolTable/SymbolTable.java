package SymbolTable;

import java.util.Hashtable;

public class SymbolTable extends Table {

    private static SymbolTable ourInstance = new SymbolTable();

    public static SymbolTable getInstance() {
        return ourInstance;
    }

    private SymbolTable() {}

}
