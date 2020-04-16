package model;

import SymbolTable.*;
import com.google.gson.Gson;
import exceptions.FirstAndFollowException;
import exceptions.GrammarException;
import syntatic_analysis.*;
import lexic_analysis.Scanner;
import lexic_analysis.TokenInfo;

import java.io.*;
import java.util.*;

public class CompilerManager {
    private static String sourceFile;
    private static String dictionaryFile;
    private static String grammarFile;
    private static Scanner scanner;
    private static Parser parser;
    private SymbolTable symbolTable;

    // Empty constructor
    public CompilerManager(){
        symbolTable = SymbolTable.getInstance();
    }


    public CompilerManager(String sourceFile, String grammarFile, String dictionaryFile) {
        this.sourceFile = sourceFile;
        this.grammarFile = grammarFile;
        this.dictionaryFile = dictionaryFile;
        scanner = new Scanner(sourceFile);
        parser = new Parser(grammarFile, dictionaryFile);
        symbolTable = SymbolTable.getInstance();
    }

    public void compile() throws FirstAndFollowException, GrammarException {
        TokenInfo tmp = null;
        ArrayList<TokenInfo> tokensInfo = new ArrayList<>();
        int counter;


        while (scanner.getNextToken() != null) {
            counter = 0;
            //Prelectura
            tokensInfo.add(scanner.sendNextToken());
            do {
                if (parser.consultDictionary(tokensInfo.get(counter).getId()) != null) {
                    tokensInfo.get(counter).setToken(parser.consultDictionary(tokensInfo.get(counter).getId()));
                    Token token = new Token(tokensInfo.get(counter).getToken());
                } else {

                    Token token = new Token("", tokensInfo.get(counter).getId());
                    tokensInfo.get(counter).setToken(token.token);

                }

                counter++;
                //Lectura siguiente token
                tokensInfo.add(scanner.sendNextToken());
            } while (!tokensInfo.get(tokensInfo.size() - 1).getId().equals(";"));

            tokensInfo.get(tokensInfo.size() - 1).setToken("DOT_COMA");

            //Si pasa el analisis sintactico se guarda en la tabla de simbolos
            if (parser.checkGrammar(tokensInfo)) {
                for (TokenInfo tokenInfo : tokensInfo) {
                    symbolTable.addSymbol(new Symbol(tokenInfo.getId(), tokenInfo.getToken(), tokenInfo.getType(), tokenInfo.getScope(), tokenInfo.getDeclaredAtLine(), tokenInfo.getDataSize()));
                    //Builds ASTree for the expression, this part only works for 1 expresion - To be modified later if needed
                    if (tokenInfo.getToken().equals("ASSGN_EQ")) {
                        parser.buildTree(tokenInfo);
                        parser.buildTree(tmp);

                    }
                    if (parser.validateTreeConstruction(tokenInfo.getToken())) {
                        parser.buildTree(tokenInfo);
                    }
                    tmp = tokenInfo;
                }
            }
            ASTree tree = parser.getBuiltTree();
            tokensInfo.clear();
        }
    }


    public static String getSourceFile() {
        return sourceFile;
    }

    public static void setSourceFile(String sourceFile) {
        CompilerManager.sourceFile = sourceFile;
    }

    public static String getDictionaryFile() {
        return dictionaryFile;
    }

    public static void setDictionaryFile(String dictionaryFile) {
        CompilerManager.dictionaryFile = dictionaryFile;
    }

    public static String getGrammarFile() {
        return grammarFile;
    }

    public static void setGrammarFile(String grammarFile) {
        CompilerManager.grammarFile = grammarFile;
    }

    public static Scanner getScanner() {
        return scanner;
    }

    public static void setScanner(Scanner scanner) {
        CompilerManager.scanner = scanner;
    }

    public static Parser getParser() {
        return parser;
    }

    public static void setParser(Parser parser) {
        CompilerManager.parser = parser;
    }

    public Table getSymbolTable() {
        return symbolTable;
    }
}
