package model;

import SymbolTable.*;
import com.google.gson.Gson;
import syntatic_analysis.*;
import lexic_analysis.Scanner;
import lexic_analysis.TokenInfo;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CompilerManager {
    private static String sourceFile;
    private static String dictionaryFile;
    private static String grammarFile;
    private static Scanner scanner;
    private static Parser parser;
    private SymbolTable symbolTable;

    public CompilerManager(String sourceFile, String grammarFile, String dictionaryFile) {
        this.sourceFile = sourceFile;
        this.grammarFile = grammarFile;
        this.dictionaryFile = dictionaryFile;
        scanner = new Scanner(sourceFile);
        parser = new Parser(grammarFile, dictionaryFile);
        symbolTable = SymbolTable.getInstance();
    }

    public void compile() {
        int count = 0;
        while (scanner.getNextToken() != null) {
            TokenInfo tokenInfo = scanner.sendNextToken();
            if (parser.consultDictionary(tokenInfo.getId()) != null) {
                tokenInfo.setToken(parser.consultDictionary(tokenInfo.getId()));
                Token token = new Token(tokenInfo.getToken());
                symbolTable.addSymbol(new Symbol(tokenInfo.getId(),tokenInfo.getToken(), tokenInfo.getType(),tokenInfo.getScope(), tokenInfo.getDeclaredAtLine(), tokenInfo.getDataSize()));
            }
            else {
                Token token = new Token("", tokenInfo.getId());
                tokenInfo.setToken(token.token);
                symbolTable.addSymbol(new Symbol(tokenInfo.getId(), tokenInfo.getToken(), tokenInfo.getType(), tokenInfo.getScope(), tokenInfo.getDeclaredAtLine(), tokenInfo.getDataSize()));
            }
            //Builds ASTree for the expression
            if (!tokenInfo.getToken().equals("DOT_COMA")) {
                parser.buildTree(tokenInfo);
            }
        }
        //TEST
        ASTree tree = parser.getBuiltTree();
        System.out.println(symbolTable.toString());
    }
}
