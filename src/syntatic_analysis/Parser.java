package syntatic_analysis;

import SymbolTable.SymbolTable;
import com.google.gson.Gson;

import java.util.ArrayList;

import exceptions.FirstAndFollowException;
import exceptions.GrammarException;
import lexic_analysis.TokenInfo;
import model.Dictionary;
import model.Word;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

public class Parser {
    private static Boolean DEBUG = true;
    private HashMap<String, Word> dictionary;
    private static Grammar grammar;
    private String grammarFile;
    private String dictionaryFile;
    private ASTree asTree;
    private ArrayList<ASTree> asTrees;

    String[][] table = {
            {"EXPRESSION", "EXPRESSION", null, null, null, null, null, null, null, "SENTENCIA", "SENTENCIA", null, null, null, null, null, null, null}
            ,
            {"D", "DECLARACION", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            ,
            {null, null, null, null, null, null, null, null, null, "TIPO_SENTENCIA PRNTSS_OPEN CONDITIONAL_EXPRESSION PRNTSS_CLOSED COR_OPEN EXPRESSION COR_CLOSED", "TIPO_SENTENCIA PRNTSS_OPEN CONDITIONAL_EXPRESSION PRNTSS_CLOSED COR_OPEN EXPRESSION COR_CLOSED", null, null, null, null, null, null, null}
            ,
            {null, "TIPO F C", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            ,
            {null, "INT", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}


            ,
            {"IDENTIFIER", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            ,
            {null, null, "NUMBER", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            ,
            {"F", null, "VALUE", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            ,
            {null, null, null, null, null, "ASSGN_EQ B", null, null, null, null, null, null, null, null, null, null, null, ""}
            ,
            {"F ASSGN_EQ G", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            ,


            {"B OPERATOR B", null, "B OPERATOR B", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            ,
            {null, null, null, null, null, "ARTMTC_SM", "ARTMTC_RS", "ARTMTC_MLT", "ARTMTC_DV", null, null, null, null, null, null, null, null, null}
            ,
            {null, null, null, null, null, null, null, null, null, "IF", "WHILE", null, null, null, null, null, null, null}
            ,
            {"B RLTNL B", null, "B RLTNL B", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            ,
            {null, null, null, null, null, null, null, null, null, null, null, "RLTNL_EQ", "RLTNL_NTEQ", null, null, null, null, null}



    };

    String[] noTerminals = {"S", "EXPRESSION","SENTENCIA", "DECLARACION","TIPO",
            "F", "VALUE", "B", "C", "D",
            "G", "OPERATOR", "TIPO_SENTENCIA", "CONDITIONAL_EXPRESSION", "RLTNL"};
    String[] terminals = {"IDENTIFIER", "INT", "NUMBER", "DOT_COMA", "ASSGN_EQ", "ARTMTC_SM",
            "ARTMTC_RS","ARTMTC_MLT", "ARTMTC_DV", "IF", "WHILE", "RLTNL_EQ",
            "RLTNL_NTEQ", "PRNTSS_OPEN", "PRNTSS_CLOSED", "COR_OPEN", "COR_CLOSED" ,"#"};

    //Pila
    ArrayList<String> stack = new ArrayList<>();


    public Parser(String grammarFile, String dictionaryFile) {
        this.grammarFile = grammarFile;
        this.dictionaryFile = dictionaryFile;
        dictionary = myDictionary();
        initGrammar();
        startFirstFollow();
        asTree = new ASTree();
        asTrees = new ArrayList<>();
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

    public boolean checkGrammar(ArrayList<TokenInfo> tokenInfos) throws FirstAndFollowException, GrammarException {

        int inputCounter = 0;
        stack.clear();

        stack.add(tokenInfos.get(0).getToken() + "");
        stack.add("S");

        //Cogemos el primer token que nos pasa el scanner
        String token = String.valueOf(tokenInfos.get(inputCounter).getToken());
        //Token actual de la pila
        String top = null;

        do {
            //Primer elemento de la pila
            top = stack.get(stack.size() - 1);
            stack.remove(stack.size() - 1);

            //Terminal, no terminal o error
            if (checkNoTerminal(top)) {
                //Miramos una regla para los no terminales
                int row = getnoTermIndex(top);
                int column = getTermIndex(token);
                String rule = table[row][column];
                if (rule == null) {
                    //TODO: Specify where the error came from more accurately.
                    throw new FirstAndFollowException(TokenInfo.arrayToString(tokenInfos));
                    //error("There is no Rule by this , Non-Terminal("+top+") ,Terminal("+token+") ");
                } else {

                    String[] newRules = rule.split(" ");
                    for (int i = newRules.length - 1; i >= 0; i--) {
                        stack.add(String.valueOf(newRules[i]));
                    }
                }

            } else if (checkTerminal(top)) {
                //Elemento analizado es Terminal

                if (top.equals(token)) {  //Miramos si coincide el terminal analizado con el token del scanner
                    //Coinciden, miramos siguiente token del scanner
                    System.out.println(token);
                    inputCounter++;
                    token = String.valueOf(tokenInfos.get(inputCounter).getToken());
                } else {
                    //No coinciden, error
                    throw new GrammarException(tokenInfos.get(inputCounter).toString());
                }
            } else {
                //No está en la gramática
                throw new GrammarException(top);
                //System.out.println("error, no está contemplado por el parser (no en la gramática)");
            }
            if (token.equals("DOT_COMA")) { //expresión acabada
                break; //cerramos el do while
            }

        } while (true);

        //fin de la expresión
        if (token.equals("DOT_COMA")) {
            //ok expression
            System.out.println("Syntactically validated expression");
            return true;
        }

        //ko expression
        System.out.println("Syntactically wrong expression");
        return false;


    }

    private boolean checkTerminal(String token) {
        for (int i = 0; i < terminals.length; i++) {
            if (token.equals(this.terminals[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean checkNoTerminal(String token) {
        for (int i = 0; i < noTerminals.length; i++) {
            if (token.equals(this.noTerminals[i])) {
                return true;
            }
        }
        return false;
    }


    private int getnoTermIndex(String noTerm) {
        for (int i = 0; i < this.noTerminals.length; i++) {
            if (noTerm.equals(this.noTerminals[i])) {
                return i;
            }
        }
        System.out.println(noTerm + " no es no terminal");
        return -1;
    }

    private int getTermIndex(String term) {
        for (int i = 0; i < terminals.length; i++) {
            if (term.equals(terminals[i])) {
                return i;
            }
        }
        System.out.println(term + " no es terminal");
        return -1;
    }

    private String getTypeOfGlobalVariable (TokenInfo token, TokenInfo tmp, SymbolTable table) {
        //If it's not in the symbol table
        if (table.getSymbol(token.getId()) == null){
            if (consultDictionary(token.getId()) == null) {
                //If it wasn't a terminal, it checks if it's a var and tmp was a terminal, which means it is declared
                Token aux = new Token ("", token.getId());
                if (aux.token.equals("IDENTIFIER") && tmp.getToken().equals("INT") || aux.token.equals("NUMBER")) {
                    return "INT";
                }
            }
        }
        else {
            //If it's in the symbol table, we get its type
            if (token.getToken().equals("IDENTIFIER")) {
                return table.getSymbol(token.getId()).getType();
            }
        }
        return null;
    }

    private String getTypeOfLocalVariable (TokenInfo token, TokenInfo tmp, SymbolTable table, String tableId) {
        Token aux;
        if (table.getSymbol(token.getId(), tableId) == null) {
            if (consultDictionary(token.getId()) == null) {
                aux = new Token("", token.getId());
                if ((aux.token.equals("IDENTIFIER") && tmp.getToken().equals("INT")) || aux.token.equals("NUMBER")) {
                    return "INT";
                }
                //When we have two declared global variables in a condition we have to check their types in the general table
                return getTypeOfGlobalVariable(token, tmp, table);
            }
        }
        else {
            if (token.getToken().equals("IDENTIFIER"))
            {
                return table.getSymbol(token.getId(),tableId).getType();
            }
        }
        return null;
    }

    public String addTypeToVariable (TokenInfo token, TokenInfo tmp, SymbolTable table, String tableId) {
        String type = null;
        if (tableId == null) {
            type = getTypeOfGlobalVariable(token, tmp, table);
        }
        else {
            type = getTypeOfLocalVariable(token, tmp, table, tableId);
        }
        return type;
    }

    public void buildTree(TokenInfo token) {
        asTree.insert(token);
    }

    public ASTree getBuiltTree() {
        return asTree;
    }

    public void buildWhileIfTree (ArrayList<TokenInfo> tokenInfos) {
        TokenInfo tmp = new TokenInfo();
        ASTree tree = new ASTree();
        for (TokenInfo t : tokenInfos) {
            if (t.getToken().equals("ASSGN_EQ") || t.getToken().equals("RLTNL_EQ") || t.getToken().equals("RLTNL_NTEQ")) {
                tree.insert(t);
                tree.insert(tmp);
            }
            if (validateWhileTreeConstruction(t.getToken(), tree)) {
                tree.insert(t);
            }
            if (t.getToken().equals("WHILE") || t.getToken().equals("IF")) {
                tree.insert(t);
                asTrees.add(tree);
                tree = new ASTree();
            }
            if (t.getToken().equals("PRNTSS_CLOSED") || t.getToken().equals("DOT_COMA")) {
                asTrees.add(tree);
                tree = new ASTree();
            }
            tmp = t;
        }
    }

    public ArrayList<ASTree> getBuiltWhileIfTree () {
        return asTrees;
    }

    public boolean validateWhileTreeConstruction (String token, ASTree tree) {
        if (tree.getRoot() == null && (token.equals("ASSGN_EQ"))) {
            return true;
        }
        if (tree.getRoot() != null && !token.equals("ASSGN_EQ")) {
            if (token.equals("IDENTIFIER") || token.equals("NUMBER") || token.contains("ARTMTC")) {
                return true;
            }
        }
        return false;
    }

    public boolean validateTreeConstruction(String token) {
        if (asTree.getRoot() == null && (token.equals("ASSGN_EQ"))) {
            return true;
        }
        if (asTree.getRoot() != null && !token.equals("ASSGN_EQ")) {
            if (token.equals("IDENTIFIER") || token.equals("NUMBER") || token.contains("ARTMTC")) {
                return true;
            }
        }
        return false;
    }

    public void addToAsTrees(ASTree tree) {
        asTrees.add(tree);
    }

    public ArrayList getAsTrees () {
        return this.asTrees;
    }

    /**
     * Reads the .json file and adds words to dictionary
     *
     * @return list of words of the dictionary
     */
    public HashMap<String, Word> myDictionary() {
        Gson gson = new Gson();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(dictionaryFile));
            Dictionary dictionary = gson.fromJson(br, Dictionary.class);
            return addToDictionary(dictionary);
        } catch (FileNotFoundException e) {
            System.out.println("\nError, file can't be found.");
            e.printStackTrace();
        } finally {
            if (br != null) {
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
        HashMap<String, Word> list = new HashMap<>();
        for (Word word : dictionary.getWords()) {
            list.put(word.getLexeme(), word);
        }
        return list;
    }

    public String consultDictionary(String key) {
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
