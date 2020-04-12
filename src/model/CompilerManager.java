package model;

import SymbolTable.*;
import com.google.gson.Gson;
import grammar.FirstAndFollow;
import grammar.Token;
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
    private static Scanner scanner;
    private static FirstAndFollow grammar;
    private HashMap<String, Word> dictionary;
    private SymbolTable symbolTable;
    private static boolean DEBUG = true;

    public CompilerManager(String sourceFile, String dictionaryFile) {
        this.sourceFile = sourceFile;
        this.dictionaryFile = dictionaryFile;
        dictionary = myDictionary();
        scanner = new Scanner(sourceFile);
        grammar = new FirstAndFollow();
        symbolTable = SymbolTable.getInstance();
    }

    public void compile() {
        deleteOutput(sourceFile);
        deleteOutput(sourceFile + ".DEBUG");
        while (scanner.getNextToken() != null) {
            TokenInfo tokenInfo = scanner.sendNextToken();
            if (consultDictionary(tokenInfo.getId()) != null) {
                tokenInfo.setToken(consultDictionary(tokenInfo.getId()));
                Token token = new Token(tokenInfo.getToken());
                symbolTable.addSymbol(new Symbol(tokenInfo.getId(),tokenInfo.getToken(), tokenInfo.getType(), token.type,tokenInfo.getScope(), tokenInfo.getDeclaredAtLine(), tokenInfo.getDataSize()));
            }
            else {
                Token token = new Token("", tokenInfo.getId());
                symbolTable.addSymbol(new Symbol(tokenInfo.getId(), token.token, tokenInfo.getType(), token.type, tokenInfo.getScope(), tokenInfo.getDeclaredAtLine(), tokenInfo.getDataSize()));
            }
        }

        System.out.println(symbolTable.toString());
        if (DEBUG) {
            System.out.println("== INPUT ==");
            appendToOutput(sourceFile + ".DEBUG", "== INPUT ==\n");
            System.out.println(getInput(sourceFile));
            appendToOutput(sourceFile + ".DEBUG", getInput(sourceFile) + "\n");
            // TOKEN
            System.out.println("== TOKENS ==");
            appendToOutput(sourceFile + ".DEBUG", "== TOKENS ==\n");
        }

    }

    /**
     * Delete output file if exists
     * @param file
     */
    public static void deleteOutput (String file) {
        File f = new File("../out/" + file + ".out");
        if (f.exists()) {
            f.delete();
        }
    }

    // Append to output file
    protected static void appendToOutput(String file, String str) {
        try {
            File f = new File("../out/" + file + ".out");
            if (!f.exists()) {
                f.createNewFile();
            }
            FileWriter fileWritter = new FileWriter("../out/" + file + ".out", true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(str);
            bufferWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // File to String
    protected static String getInput(String file) {
        String result = null;
        DataInputStream in = null;

        try {
            File f = new File(file);
            byte[] buffer = new byte[(int) f.length()];
            in = new DataInputStream(new FileInputStream(f));
            in.readFully(buffer);
            result = new String(buffer);
        } catch (IOException e) {
            throw new RuntimeException("IO Error", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        return result;
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
     * You have been hacked by mushimushibongbong *
     */

    public String getWord(String key) {
        Word word = dictionary.get(key);
        if (word == null) {
            return "";
        }
        return word.getToken();
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
