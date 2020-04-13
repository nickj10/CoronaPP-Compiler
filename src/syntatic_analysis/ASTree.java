package syntatic_analysis;

import lexic_analysis.TokenInfo;

public class ASTree {
  private ASTNode root;

  public ASTree () {
    root = null;
  }

  public void insert(TokenInfo token) {
    root = insertChild(root, token);
  }

  private ASTNode insertChild (ASTNode root, TokenInfo token) {
    if (root == null || !root.token.getToken().equals("ASSGN_EQ")) {
      if (token.getToken().equals("ASSGN_EQ")) {
        root.left = new ASTNode(root.token);
        root.token = token;
      }
      else {
        root = new ASTNode(token);
      }
      return root;
    }
    if (root.left == null) {
      root.left = insertChild(root.left, token);
    }
    else {
      if (root.right == null) {
        root.right = insertChild(root.right, token);
      }
    }
    return root;
  }



}
