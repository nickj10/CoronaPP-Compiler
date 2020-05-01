package SymbolTable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

public class Table {
    private Hashtable<Integer, Symbol> table;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Table(Hashtable<Integer, Symbol> table) {
        this.table = table;
    }

    public Table(){
        this.table = new Hashtable<Integer, Symbol>();
    }

    // Methods:
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

    public void addSymbol(Symbol s){
        table.put(hashf(s.getId()), s);
    }

    public Symbol getSymbol(String id, int tableId){
        // TODO: Implement tableId for each internal table
        return table.get(hashf(id));
    }

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

        /*Enumeration enu = table.elements();
        String str = new String();
        str = "Table{\n";
        str += "\t" + enu.nextElement().toString();

        while (enu.hasMoreElements()) {
            str += ",\n";
            str += "\t" + enu.nextElement().toString();
        }
        str += "\n}";
        return str;*/

        return gson.toJson(this);
    }
}
