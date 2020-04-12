package syntatic_analysis;

import java.io.*;

public class Parser {
    private static Boolean DEBUG = true;
    private static FirstAndFollow firstAndFollow;
     private static Grammar grammar;
     private static String file;

     public Parser (String file) {
         this.file = file;
         initGrammar();
         startFirstFollow();
     }

     public void initGrammar() {
         // Get Raw Token Array
         String[] rawTokens = getInput(file).split("\\s+");
         // Init Token Array
         Token[] tokens = new Token[rawTokens.length];

         // Delete output file if exists
         deleteOutput(file);
         deleteOutput(file + ".DEBUG");

         // DEBUG Section
         if (DEBUG) {
             // INPUT
             System.out.println("== INPUT ==");
             appendToOutput(file + ".DEBUG", "== INPUT ==\n");
             System.out.println(getInput(file));
             appendToOutput(file + ".DEBUG", getInput(file) + "\n");
             // TOKEN
             System.out.println("== TOKENS ==");
             appendToOutput(file + ".DEBUG", "== TOKENS ==\n");
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
                 appendToOutput(file + ".DEBUG", token.token + " " + token.message + "\n");
             }
             // Increment Count
             count++;
         }

         // DEBUG Section
         if (DEBUG) {
             System.out.println("== GRAMMAR ==");
             appendToOutput(file + ".DEBUG", "== GRAMMAR ==\n");
         }

         // Init Grammar Array
         grammar = new Grammar(tokens);
         if (grammar.type == "ERROR") {
             System.out.println(grammar.message);
             appendToOutput(file, grammar.message);
             if (DEBUG) {
                 appendToOutput(file + ".DEBUG", grammar.message);
             }
             return;
         } else {
             System.out.print(grammar.toString());
             // DEBUG Section
             if (DEBUG) {
                 appendToOutput(file + ".DEBUG", grammar.toString());
             }
         }
     }

     public void startFirstFollow() {
         if (DEBUG) {
             System.out.println("== FIRST ==");
             appendToOutput(file + ".DEBUG", "== FIRST ==\n");
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
                     appendToOutput(file + ".DEBUG", firstString + "\n");
                 }
                 // Output First Set
                 System.out.println(firstString);
                 appendToOutput(file, firstString + "\n");
             }
         }

         // DEBUG Section
         if (DEBUG) {
             System.out.println("== FOLLOW ==");
             appendToOutput(file + ".DEBUG", "== FOLLOW ==\n");
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
                     appendToOutput(file + ".DEBUG", followString + "\n");
                 }
                 // Output First Set
                 System.out.println(followString);
                 appendToOutput(file, followString + "\n");
             }
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
