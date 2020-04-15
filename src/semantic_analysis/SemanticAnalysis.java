package semantic_analysis;

import lexic_analysis.TokenInfo;
import syntatic_analysis.ASTNode;
import syntatic_analysis.ASTree;

import java.util.ArrayList;

public class SemanticAnalysis {
    private ArrayList<TokenInfo> tokenInfos;

    public SemanticAnalysis () {
        tokenInfos = new ArrayList<>();
    }

    public void analyze (ASTree tree) {
        System.out.println("Semantic Analysis");
        tokenInfos = tree.visitAST(tree.getRoot());
        //TODO: Check semantically
    }



}
