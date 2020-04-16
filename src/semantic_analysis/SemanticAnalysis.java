package semantic_analysis;

import exceptions.SemanticException;
import lexic_analysis.TokenInfo;
import syntatic_analysis.ASTNode;
import syntatic_analysis.ASTree;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class SemanticAnalysis {
    private ArrayList<TokenInfo> tokenInfos;
    private static final Pattern OPERATORS = Pattern.compile("^(ARITMETIC_MULT|ARITMETIC_MOD|ARITMETIC_DIV|ARITMETIC_SUM|ARITMETIC_RES|ASSGN_EQ)$");


    public SemanticAnalysis () {
        tokenInfos = new ArrayList<>();
    }

    public void analyze (ASTree tree) throws SemanticException {
        System.out.println("Semantic Analysis");
        tokenInfos = tree.visitAST(tree.getRoot());
        //TODO: Check semantically
        for (int i = 0; i < tokenInfos.size(); i++) {
            //If it's an operator
            if (OPERATORS.matcher(tokenInfos.get(i).getToken()).matches()) {
                //Check left operand and right operand type
                if (tokenInfos.get(i - 1).getType().equals(tokenInfos.get(i + 1).getType())) {
                    throw new SemanticException("Variables don't match in types. " + tokenInfos.get(i-1).getId() + " is " + tokenInfos.get(i-1).getType() + " and " + tokenInfos.get(i+1).getId() + " is " + tokenInfos.get(i+1).getType());
                }
            }
        }
    }



}
