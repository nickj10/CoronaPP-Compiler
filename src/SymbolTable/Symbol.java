package SymbolTable;

/*  Class to store all the information for the symbols stored inside the symbol table */
public class Symbol {
    // Parameters
    String  id,
            type,
            scope;
    int     declaredAtLine,
            dataSize;

    // Methods
    public Symbol() {
    }

    public Symbol(String id, String type, String scope, int declaredAtLine, int dataSize) {
        this.id = id;
        this.type = type;
        this.scope = scope;
        this.declaredAtLine = declaredAtLine;
        this.dataSize = dataSize;
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", scope='" + scope + '\'' +
                ", declaredAtLine=" + declaredAtLine +
                ", dataSize=" + dataSize +
                '}';
    }
}


