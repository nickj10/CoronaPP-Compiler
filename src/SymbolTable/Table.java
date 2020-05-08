package SymbolTable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;
import java.util.regex.Pattern;

public class Table {
    private Hashtable<Integer, Symbol> table;
    private transient int framePointer;
    private transient Stack<Symbol> scopeStack;   // Stack to keep control of the scopes (parent and child tables)
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Pattern EXCLUDE_LEXEMAS = Pattern.compile("(\\+|\\*|\\(|\\)|\\=|\\==|\\!=|\\-|;|\\{|\\}|\\/)");


    public Table(Hashtable<Integer, Symbol> table, int framePointer_) {
        this.table = table;
        framePointer = framePointer_;
        scopeStack = new Stack<Symbol>();
    }

    public Table(){
        this.table = new Hashtable<Integer, Symbol>();
        framePointer = 0;
        scopeStack = new Stack<Symbol>();
    }

    // Methods:
    private void addToScope(Symbol s){
        Symbol innerScope = scopeStack.peek(); // Getting the last scope
        innerScope.getChildTable().addSymbol(s); // Adding the variable(s) inside the internal scope (child scope)
        s.setParentTable(getPreviousScopeTable()); // Setting the parent table
    }

    /**
     * Method to check whether or not the Symbol s needs to be inserted into current scope using the scopeStack.
     * IE: local variables inside if statements.
     * @param s: Symbol to be checked
     * @return True if the symbol has been inserted into current scope
     *         False if the symbol wasn't inserted (global variables or not inner scope available)
     */
    private Boolean checkScope(Symbol s){
        if(s.getScope().equals("global") || scopeStack.empty())
            return false;

        addToScope(s);
        return true;
    }

    /**
     * Method to get the previous scope (not the current one, which can be obtained by using peek() or pop() in scopeStack
     * @return the previous scope. (null if current one is the main table)
     */
    private Table getPreviousScopeTable(){
        int scopeSize = scopeStack.size();
        if(scopeSize == 0){
            return null;
        }else if(scopeSize == 1){
            return this;
        }else{
            return scopeStack.elementAt(scopeSize -1).getChildTable();
        }
    }


    /**
     * Method to check whether or not the symbol opens (create) a new scope (new block). IE: if, while statements
     * @param s: Symbol
     * @return True if a new scope has been created and pushed into the scopeStack
     *         False if not scope has been created
     */
    private Boolean isNewScope(Symbol s){
        if(s.getId().equals("if")){ // Checking keyword to see if inner scope is needed
            s.setId(String.format("%s%d",s.getId(), s.getDeclaredAtLine())); // Setting a properly ID to avoid collisions
            s.setChildTable();
            return true;
        }else if(s.getId().equals("while")){
            s.setId(String.format("%s%d",s.getId(), s.getDeclaredAtLine())); // Setting a properly ID to avoid collisions
            s.setChildTable();
            return true;
        }

        return false;
    }

    private Boolean exists(int id){
        return table.containsKey(id);
    }

    public Boolean checkIfExists(Symbol s){
        return exists(hashf(s.getId()));
    }

    public Boolean checkIfExists(String s){
        return exists(hashf(s));
    }

    public Boolean alreadyExists(String s){
        if(checkIfExists(s))
            return true;

        Table parent = getParentTable();
        if(parent == null)
            return false;

        return parent.alreadyExists(s);
    }

    public Boolean alreadyExists(Symbol symbol){
        String s = symbol.getId();
        if(checkIfExists(s))
            return true;

        Table parent = getParentTable();
        if(parent == null)
            return false;

        return parent.alreadyExists(s);
    }

    public Table getParentTable(){
        Set<Integer> keys = table.keySet();

        for(Integer key : keys)
            if(table.get(key).hasParentTable())
                return table.get(key).getParentTable();

        return null;
    }

    public ArrayList<Table> getChildTables(){
        Set<Integer> keys = table.keySet();
        ArrayList<Table> childTables = new ArrayList<Table>(null);
        for(Integer key : keys)
            if(table.get(key).hasChildTable())
                childTables.add( table.get(key).getChildTable() );

        return childTables;
    }

    private Symbol getCurrentScopeSymbol(){
        if(scopeStack.empty())
            return null;
        return scopeStack.peek();
    }

    /**
     * Adds a symbol into the table and returns the name of the symbol which contains this table
     * (useful for inner scopes)
     * @param s: Symbol to be added to the respective table container
     * @return Name of the symbol which contains the table (also known as "tableId")
     */
    public String addSymbol(Symbol s){
        if (EXCLUDE_LEXEMAS.matcher(s.getLexema()).matches()) {
            if(s.getLexema().equals("}")) {
                scopeStack.pop();
            }
            return null;
        }

        Boolean newScope = isNewScope(s);
        Symbol currentScopeSymbol = getCurrentScopeSymbol();

        if(!checkScope(s))
            table.put(hashf(s.getId()), s);

        if(newScope)
            scopeStack.push(s);

        if(currentScopeSymbol == null)
            return null;

        return currentScopeSymbol.getId();
    }

    public Symbol getSymbol(String id, String tableId){
        if(this.getSymbol(tableId) != null){
            return this.getSymbol(tableId).getChildTable().getSymbol(id);
        }

        ArrayList<Table> childTables = getChildTables();

        if(childTables.isEmpty())
            return null;

        for( Table table : childTables) {
            Symbol found = table.getSymbol(id, tableId);
            if(found != null)
                return found;
        }

        return null;
    }

    public Symbol getSymbol(String id){
        return table.get(hashf(id));
    }

    // Deprecated
    /*
    public Symbol getSymbol(String id, int tableid){
        return table.get(hashf(id));
    }*/

    public Symbol removeSymbol(String id){
        return table.remove(hashf(id));
    }

    private int hashf(String id){
        int hash = 7;
        int length = id.length();
        for(int i=0; i<length; i++)
            hash = hash*31 + id.charAt(i);

        return hash;
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }

    public int getFramePointer() {
        return framePointer;
    }

    public void setFramePointer(int framePointer) {
        this.framePointer = framePointer;
    }
}
