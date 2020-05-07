package model;

import SymbolTable.*;
import exceptions.SemanticException;
import intermediate.BasicBlock;
import intermediate.IntermediateCodeFlow;
import semantic_analysis.SemanticAnalysis;
import exceptions.FirstAndFollowException;
import exceptions.GrammarException;
import exceptions.SemanticException;
import syntatic_analysis.*;
import lexic_analysis.Scanner;
import lexic_analysis.TokenInfo;

import java.util.*;

import static intermediate.ThreeAddrCode.syntaxTreeToTAC;

public class CompilerManager {
    private static String sourceFile;
    private static String dictionaryFile;
    private static String grammarFile;
    private static Scanner scanner;
    private static Parser parser;
    private SymbolTable symbolTable;
    private static SemanticAnalysis semanticAnalysis;

    // Empty constructor
    public CompilerManager(){
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
        TokenInfo tmp = null;
        ArrayList<TokenInfo> tokensInfo = new ArrayList<>();
        int counter;
        IntermediateCodeFlow icFlow = new IntermediateCodeFlow();

        while (scanner.getNextToken() != null) {
            counter = 0;
            String lastCharExpression;
            //Prelectura
            tokensInfo.add(scanner.sendNextToken());
            if (tokensInfo.get(0).getId().equals("if") || tokensInfo.get(0).getId().equals("while")){
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
            if (lastCharExpression.equals(";")){
                tokensInfo.get(tokensInfo.size() - 1).setToken("DOT_COMA");
            }else {
                tokensInfo.get(tokensInfo.size() - 1).setToken("COR_CLOSED");
            }


            //Si pasa el analisis sintactico se guarda en la tabla de simbolos
            if(parser.checkGrammar(tokensInfo)){
                for(TokenInfo tokenInfo : tokensInfo){
                    String type = parser.addTypeToVariable(tokenInfo, tmp, symbolTable);
                    if (type != null) {
                        tokenInfo.setType(type);
                    }
                    symbolTable.addSymbol(new Symbol(tokenInfo.getId(),tokenInfo.getToken(), tokenInfo.getType(),
                        tokenInfo.getScope(), tokenInfo.getDeclaredAtLine(), tokenInfo.getDataSize()));
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
            semanticAnalysis.analyze(tree);
            syntaxTreeToTAC(tree, icFlow, symbolTable);
            System.out.println(icFlow);

            // Iterate over the list of basic blocks -> MIPS
            for(BasicBlock bb : icFlow.getBasicBlocks()) {
                //addInstr(bb.getNextInstruction())
                //endBlock();
            }

            tree.clear();
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
