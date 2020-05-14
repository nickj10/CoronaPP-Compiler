package SymbolTable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/*  Class to store all the information for the symbols stored inside the symbol table */
public class Symbol {
    // Parameters
    private String id,
            lexema,
            token,
            type,
            scope;
    private int    declaredAtLine,
            dataSize,
            stackPointer; // Position in memory stack used in MIPS (known as sp).
    private transient Table parentTable;
    private Table  childTable;

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Constructor
    public Symbol() {
        parentTable = null;
        childTable = null;
        this.stackPointer = -1;
    }

    public Symbol(String lexema) {
        this.lexema = lexema;
    }

    public Symbol(String lexema, String token, String type, String scope, int declaredAtLine, int dataSize) {
        this.id = lexema;
        this.lexema = lexema;
        this.token = token;
        this.type = type;
        this.scope = scope;
        this.declaredAtLine = declaredAtLine;
        this.dataSize = dataSize;
        parentTable = null;
        childTable = null;
        this.stackPointer = -1;
    }

    @Override
    public String toString() {
/*
        return "Symbol{\n\t" +
                "id='" + id + '\'' +
                ",type='" + type + '\'' +
                ",scope='" + scope + '\'' +
                ",declaredAtLine=" + declaredAtLine +
                ",dataSize=" + dataSize +
                ",childTable = " +
                (childTable==null? "null": '\n'+childTable.toString()) +
                "\n\t}";*/

        return gson.toJson(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getDeclaredAtLine() {
        return declaredAtLine;
    }

    public void setDeclaredAtLine(int declaredAtLine) {
        this.declaredAtLine = declaredAtLine;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public Table getParentTable() { return parentTable; }

    public void setParentTable() {
        this.parentTable = new Table();
    }

    public void setParentTable(Table parentTable) {
        this.parentTable = parentTable;
    }

    public Table getChildTable() {
        return childTable;
    }

    public void setChildTable() {
        this.childTable = new Table();
    }

    public int getStackPointer() {
        return stackPointer;
    }

    public void setStackPointer(int stackPointer) {
        this.stackPointer = stackPointer;
    }

    public void setChildTable(Table childTable) {
        this.childTable = childTable;
    }

    public Boolean hasParentTable(){ return this.parentTable != null; }

    public Boolean hasChildTable(){ return this.childTable != null; }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}


