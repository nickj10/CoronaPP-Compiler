package model;

import SymbolTable.*;
import com.google.gson.Gson;
import syntatic_analysis.FirstAndFollow;
import syntatic_analysis.Parser;
import syntatic_analysis.Token;
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
    private HashMap<String, Word> dictionary;
    private SymbolTable symbolTable;

    public CompilerManager(String sourceFile, String grammarFile, String dictionaryFile) {
        this.sourceFile = sourceFile;
        this.grammarFile = grammarFile;
        this.dictionaryFile = dictionaryFile;
        dictionary = myDictionary();
        scanner = new Scanner(sourceFile);
        parser = new Parser(grammarFile);
        symbolTable = SymbolTable.getInstance();
    }

    public void compile() {
        while (scanner.getNextToken() != null) {
            TokenInfo tokenInfo = scanner.sendNextToken();
            if (consultDictionary(tokenInfo.getId()) != null) {
                tokenInfo.setToken(consultDictionary(tokenInfo.getId()));
                Token token = new Token(tokenInfo.getToken());
                symbolTable.addSymbol(new Symbol(tokenInfo.getId(),tokenInfo.getToken(), tokenInfo.getType(),tokenInfo.getScope(), tokenInfo.getDeclaredAtLine(), tokenInfo.getDataSize()));
            }
            else {
                Token token = new Token("", tokenInfo.getId());
                symbolTable.addSymbol(new Symbol(tokenInfo.getId(), token.token, tokenInfo.getType(), tokenInfo.getScope(), tokenInfo.getDeclaredAtLine(), tokenInfo.getDataSize()));
            }
        }
    }
    /**
     * Reads the .json file and adds words to dictionary
     * @return list of words of the dictionary
     */
    public HashMap<String, Word> myDictionary() {
        Gson gson = new Gson();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(dictionaryFile));
            Dictionary dictionary = gson.fromJson(br, Dictionary.class);
            return addToDictionary(dictionary);
        }catch (FileNotFoundException e) {
            System.out.println("\nError, file can't be found.");
            e.printStackTrace();
        }finally {
            if (br!=null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String consultDictionary (String key) {
        Word word = dictionary.get(key);
        if (word != null) {
            return word.getToken();
        }
        return null;
    }

    /**
     * Proc that adds the words read from the file to the dictionary
     */
    public HashMap<String, Word> addToDictionary(Dictionary dictionary) {
        HashMap<String,Word> list = new HashMap<>();
        for (Word word : dictionary.getWords()) {
            list.put(word.getLexeme(),word);
        }
        return list;
    }

    /**
     * Proc that shows all the words existing in the dictionary
     */
    public void showList() {
        Set set = dictionary.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            System.out.print("Key is: "+ entry.getKey() + " - Token is: ");
            Word value = (Word) entry.getValue();
            System.out.println(value.getToken());
        }
    }

}
