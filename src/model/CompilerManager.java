package model;

import SymbolTable.*;
import exceptions.SemanticException;
import intermediate.BasicBlock;
import intermediate.IntermediateCodeFlow;
import semantic_analysis.SemanticAnalysis;
import exceptions.FirstAndFollowException;
import exceptions.GrammarException;
import syntatic_analysis.*;
import lexic_analysis.Scanner;
import lexic_analysis.TokenInfo;

import java.util.*;
import java.util.regex.Pattern;

public class CompilerManager {
    private static final Pattern UNWANTED_TOKEN_TYPES = Pattern.compile("^(COR_CLOSED|COR_OPEN|PRNTSS_CLOSED|PRNTSS_OPEN|ASSGN_EQ|RLTNL_NTEQ|RLTNL_EQ|ARTMTC_RS|ARTMTC_SM|ARTMTC_DV|ARTMTC_MLT|DOT_COMA|RLTNL_GT|RLTNL_LS|RLTNL_GTEQ|RLTNL_LSEQ)$");
    private static String sourceFile;
    private static String dictionaryFile;
    private static String grammarFile;
    private static Scanner scanner;
    private static Parser parser;
    private SymbolTable symbolTable;
    private static SemanticAnalysis semanticAnalysis;

    // Empty constructor
    public CompilerManager() {
        symbolTable = SymbolTable.getInstance();
        semanticAnalysis = new SemanticAnalysis();
    }


    public CompilerManager(String sourceFile, String grammarFile, String dictionaryFile) {
        this.sourceFile = sourceFile;
        this.grammarFile = grammarFile;
        this.dictionaryFile = dictionaryFile;
        scanner = new Scanner(sourceFile);
        parser = new Parser(grammarFile, dictionaryFile);
        symbolTable = SymbolTable.getInstance();
        semanticAnalysis = new SemanticAnalysis();
    }

    public void compile() throws FirstAndFollowException, GrammarException, SemanticException {
        String tableId = null;
        TokenInfo tmp = null;
        ArrayList<TokenInfo> tokensInfo = new ArrayList<>();
        int counter;
        IntermediateCodeFlow icFlow = new IntermediateCodeFlow();
        ArrayList<ASTree> tmpSimple = new ArrayList<>();
        ASTree tree = new ASTree();
        ArrayList<ASTree> trees = new ArrayList<>();

        while (scanner.getNextToken() != null) {
            counter = 0;
            String lastCharExpression;
            //Prelectura
            tokensInfo.add(scanner.sendNextToken());
            if (tokensInfo.get(0).getId().equals("if") || tokensInfo.get(0).getId().equals("while")) {
                lastCharExpression = "}";
            } else {
                lastCharExpression = ";";
            }
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
            } while (!tokensInfo.get(tokensInfo.size() - 1).getId().equals(lastCharExpression));

            //Ponemos el token del ultimo caracter ya que no entra en el bucle
            if (lastCharExpression.equals(";")) {
                tokensInfo.get(tokensInfo.size() - 1).setToken("DOT_COMA");
            } else {
                tokensInfo.get(tokensInfo.size() - 1).setToken("COR_CLOSED");
            }
            //Si pasa el analisis sintactico se guarda en la tabla de simbolos
            if (parser.checkGrammar(tokensInfo)) {
                ArrayList<TokenInfo> tmpList = new ArrayList<>();
                ArrayList<TokenInfo> tmpSimpleList = new ArrayList<>();

                boolean flag = false;
                for (TokenInfo tokenInfo : tokensInfo) {
                    if (!UNWANTED_TOKEN_TYPES.matcher(tokenInfo.getToken()).matches()) {
                        String type = parser.addTypeToVariable(tokenInfo, tmp, symbolTable, tableId);
                        if (type != null) {
                            tokenInfo.setType(type);
                        }
                        tableId = symbolTable.addSymbol(new Symbol(tokenInfo.getId(), tokenInfo.getToken(), tokenInfo.getType(),
                                tokenInfo.getScope(), tokenInfo.getDeclaredAtLine(), tokenInfo.getDataSize()));
                        tokenInfo.setTableId(tableId);
                    }
                    //We use this flag to know if we're going to deal with a WHILE/IF block, that's why we're adding the tokens inside of the list
                    if (flag) {
                        tmpList.add(tokenInfo);
                    }
                    if (tokenInfo.getToken().equals("WHILE") || tokenInfo.getToken().equals("IF")) {
                        tmpList.add(tokenInfo);
                        flag = true;
                    }
                    if (tokenInfo.getToken().equals("COR_CLOSED")) {
                        flag = false;
                        parser.buildWhileIfTree(tmpList);
                        //This array returns WHILE block in the form of trees
                        trees = parser.getBuiltWhileIfTree();
                        if (trees.size() > 0) {
                            semanticAnalysis.analyze(null, trees, symbolTable, 2);
                            parser.addToTrees(trees);
                        }
                    }
                    if (!flag && tokenInfo.getToken().equals("DOT_COMA")) {
                        parser.buildSimpleTree(tmpSimpleList);
                        //If there's a constructed tree, analyze it
                        tree = parser.getBuiltTree();
                        if (tree.getRoot() != null) {
                            semanticAnalysis.analyze(tree, null, symbolTable, 1);
                            tmpSimple.add(tree);
                        }
                    }
                    //If its dealing with no loops or ifs, just normal expressions
                    if (!flag) {
                        tmpSimpleList.add(tokenInfo);
                    }

                    tmp = tokenInfo;
                }
            }
            tree.clear();
            tokensInfo.clear();
        }
        if (tmpSimple.size() > 0) {
            parser.addToTrees(tmpSimple);
        }

        icFlow.syntaxTreeToTAC(parser.getTrees());

        System.out.println(icFlow);
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
