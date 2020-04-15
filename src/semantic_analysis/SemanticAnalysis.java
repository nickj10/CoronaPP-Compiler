package semantic_analysis;

import lexic_analysis.TokenInfo;
import syntatic_analysis.ASTree;

import java.util.ArrayList;

public class SemanticAnalysis {
    private ArrayList<TokenInfo> tokenInfos;

    public SemanticAnalysis () {
        tokenInfos = new ArrayList<>();
    }

    /**
     * Visits nodes of the AST
     * @param tree the tree
     */
    public void visitAST (ASTree tree) {
        System.out.println("Semantic Analysis");
        tokenInfos = tree.visitAST(tree.getRoot());
    }



}
