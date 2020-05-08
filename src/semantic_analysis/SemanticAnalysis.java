package semantic_analysis;

import SymbolTable.SymbolTable;
import exceptions.SemanticException;
import lexic_analysis.TokenInfo;
import syntatic_analysis.ASTNode;
import syntatic_analysis.ASTree;
import syntatic_analysis.Token;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class SemanticAnalysis {
    private ArrayList<TokenInfo> tokenInfos;
    private static final Pattern OPERATORS = Pattern.compile("^(ARTMTC_MLT|ARTMTC_MD|ARTMTC_DV|ARTMTC_SM|ARTMTC_RS|ASSGN_EQ|RLTNL_EQ|RLTNL_NTEQ)$");
    private SymbolTable symbolTable;

    public SemanticAnalysis () {
        tokenInfos = new ArrayList<>();
    }

    public void analyze (ASTree tree, ArrayList<ASTree> trees, SymbolTable table, int type) throws SemanticException {
        this.symbolTable = table;
        boolean declarationCorrect = true;
        System.out.println("ASTree:");
        switch (type) {
             case 1:
                 //Case for normal expressions
                 tokenInfos = tree.visitAST(tree.getRoot());
                 //Check if all the variables were declared before,
                 for (int i = 0; i < tokenInfos.size(); i++) {
                     if (isATerminal(tokenInfos.get(i).getToken())) {
                         if (!isDeclared(tokenInfos.get(i))) {
                             declarationCorrect = false;
                             throw new SemanticException("Semantic Error:" + "'" + tokenInfos.get(i).getId() + "'" + " was not declared at line " + tokenInfos.get(i).getDeclaredAtLine());
                         }
                     }
                     //Check if expression types are correct.
                     if (OPERATORS.matcher(tokenInfos.get(i).getToken()).matches()) {
                         checkLeftRightOperandTypes(tokenInfos.get(i-1), tokenInfos.get(i+1));
                     }
                 }
                 tokenInfos.clear();
                 System.out.println("Semantically Validated");
                 break;
                 case 2:
                     //Case for whiles and ifs
                    //Iterate the arrayList of ASTree
                    for (int h = 0; h < trees.size(); h++) {
                        tokenInfos = trees.get(h).visitAST(trees.get(h).getRoot());
                        if (tokenInfos != null) {
                            for (int i = 0; i < tokenInfos.size(); i++) {
                                if (!tokenInfos.get(i).getToken().equals("WHILE") && !tokenInfos.get(i).getToken().equals("IF")) {
                                    if (isATerminal(tokenInfos.get(i).getToken())) {
                                        if (!isDeclared(tokenInfos.get(i))) {
                                            declarationCorrect = false;
                                            throw new SemanticException("Semantic Error:" + "'" + tokenInfos.get(i).getId() + "'" + " was not declared at line " + tokenInfos.get(i).getDeclaredAtLine());
                                        }
                                    }
                                    //Check if expression types are correct.
                                    if (OPERATORS.matcher(tokenInfos.get(i).getToken()).matches()) {
                                        checkLeftRightOperandTypes(tokenInfos.get(i-1), tokenInfos.get(i+1));
                                    }
                                }
                            }
                        }

                    }
                    tokenInfos.clear();
                    System.out.println("Semantically Validated");
                    break;
            }
    }

    /**
     * Checks left operand and right operand type (if they have a type only)
     * @param tokenLeft
     * @param tokenRight
     * @throws SemanticException
     */
    private void checkLeftRightOperandTypes (TokenInfo tokenLeft, TokenInfo tokenRight) throws SemanticException {
        if (!tokenLeft.getType().equals("") && !tokenRight.getType().equals("")) {
            if (!tokenLeft.getType().equals(tokenRight.getType())) {
                throw new SemanticException("Semantic Error: Variables don't match in types. " + tokenLeft.getId() + " is " + tokenLeft.getType() + " and " + tokenRight.getId() + " is " + tokenRight.getType());
            }
        }
    }

    /**
     * Checks if a variable was declared in global scopes
     * @param tokenInfo
     * @return true if it was declared
     */
    private boolean isDeclared(TokenInfo tokenInfo) {
        if (!tokenInfo.getType().equals("")) {
            if (tokenInfo.getTableId() == null) {
                //Checks global declarations
                if (symbolTable.getSymbol(tokenInfo.getId()) != null) {
                    return true;
                }
            }
            if (symbolTable.getSymbol(tokenInfo.getId(), tokenInfo.getTableId()) != null) {
                return true;
            }
        }
        else {
            if (tokenInfo.getTableId() != null) {
                if (symbolTable.getSymbol(tokenInfo.getId()) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if it's a terminal
     * @param token
     * @return true if it's a terminal
     */
    private boolean isATerminal (String token) {
        if (token.equals("IDENTIFIER") || token.equals("WHILE") || token.equals("IF")) {
            return true;
        }
        return false;
    }
}
