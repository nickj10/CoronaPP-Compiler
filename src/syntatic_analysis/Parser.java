package syntatic_analysis;

import com.google.gson.Gson;
import lexic_analysis.TokenInfo;
import model.Dictionary;
import model.Word;

import java.io.*;
import java.util.HashMap;

public class Parser {
    private static Boolean DEBUG = true;
    private HashMap<String, Word> dictionary;
    private static FirstAndFollow firstAndFollow;
     private static Grammar grammar;
    private  String grammarFile;
    private  String dictionaryFile;
    private  ASTree asTree;
    private int count;

     public Parser (String grammarFile, String dictionaryFile) {
         this.grammarFile = grammarFile;
         this.dictionaryFile = dictionaryFile;
         dictionary = myDictionary();
         initGrammar();
         startFirstFollow();
         asTree = new ASTree();
     }

     public void initGrammar() {
         // Get Raw Token Array
         String[] rawTokens = getInput(grammarFile).split("\\s+");
         // Init Token Array
         Token[] tokens = new Token[rawTokens.length];

         // Delete output file if exists
         deleteOutput(grammarFile);
         deleteOutput(grammarFile + ".DEBUG");

         // DEBUG Section
         if (DEBUG) {
             // INPUT
             System.out.println("== INPUT ==");
             appendToOutput(grammarFile + ".DEBUG", "== INPUT ==\n");
             System.out.println(getInput(grammarFile));
             appendToOutput(grammarFile + ".DEBUG", getInput(grammarFile) + "\n");
             // TOKEN
             System.out.println("== TOKENS ==");
             appendToOutput(grammarFile + ".DEBUG", "== TOKENS ==\n");
         }
         //Create Tokens Array (For Each Token in Raw Token Array DO:)
         int count = 0;
         for (String arg : rawTokens) {
             // Get Token
             Token token = new Token(arg);
             // Allocate Token Onject to token array
             tokens[count] = token;
             // DEBUG Section
             if (DEBUG) {
                 // Console Output
                 token.oneline();
                 // File Output
                 appendToOutput(grammarFile + ".DEBUG", token.token + " " + token.message + "\n");
             }
             // Increment Count
             count++;
         }

         // DEBUG Section
         if (DEBUG) {
             System.out.println("== GRAMMAR ==");
             appendToOutput(grammarFile + ".DEBUG", "== GRAMMAR ==\n");
         }

         // Init Grammar Array
         grammar = new Grammar(tokens);
         if (grammar.type == "ERROR") {
             System.out.println(grammar.message);
             appendToOutput(grammarFile, grammar.message);
             if (DEBUG) {
                 appendToOutput(grammarFile + ".DEBUG", grammar.message);
             }
             return;
         } else {
             System.out.print(grammar.toString());
             // DEBUG Section
             if (DEBUG) {
                 appendToOutput(grammarFile + ".DEBUG", grammar.toString());
             }
         }
     }

     public void startFirstFollow() {
         if (DEBUG) {
             System.out.println("== FIRST ==");
             appendToOutput(grammarFile + ".DEBUG", "== FIRST ==\n");
         }
         // First Sets
         for (Token nonTerminal : grammar.nonTerminals) {
             if (nonTerminal != null) {
                 // For each Nonterminal get First Sets
                 String firstString = "FIRST(" + nonTerminal.token + ") = {";
                 for (Token token : grammar.first(nonTerminal)) {
                     if (token != null) {
                         firstString += token.token + ", ";
                     }
                 }
                 firstString += "}";
                 firstString = firstString.replace(", }", "}");
                 // DEBUG Section
                 if (DEBUG) {
                     appendToOutput(grammarFile + ".DEBUG", firstString + "\n");
                 }
                 // Output First Set
                 System.out.println(firstString);
                 appendToOutput(grammarFile, firstString + "\n");
             }
         }

         // DEBUG Section
         if (DEBUG) {
             System.out.println("== FOLLOW ==");
             appendToOutput(grammarFile + ".DEBUG", "== FOLLOW ==\n");
         }
         // FOLLOW Sets
         for (Token nonTerminal : grammar.nonTerminals) {
             if (nonTerminal != null) {
                 // For each Nonterminal get First Sets
                 String followString = "FOLLOW(" + nonTerminal.token + ") = {";
                 for (Token token : grammar.follow(nonTerminal)) {
                     if (token != null) {
                         followString += token.token + ", ";
                     }
                 }
                 followString += "}";
                 followString = followString.replace(", }", "}");
                 // DEBUG Section
                 if (DEBUG) {
                     appendToOutput(grammarFile + ".DEBUG", followString + "\n");
                 }
                 // Output First Set
                 System.out.println(followString);
                 appendToOutput(grammarFile, followString + "\n");
             }
         }

     }

     public void buildTree(TokenInfo token) {
         asTree.insert(token);
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

    public String consultDictionary (String key) {
        Word word = dictionary.get(key);
        if (word != null) {
            return word.getToken();
        }
        return null;
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

    // Delete output file
    protected static void deleteOutput(String file) {
        File f = new File("../out/" + file + ".out");
        if (f.exists()) {
            f.delete();
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



}
