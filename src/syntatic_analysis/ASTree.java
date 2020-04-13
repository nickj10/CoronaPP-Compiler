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
      // If token is = then assign it to the root
      if (token.getToken().equals("ASSGN_EQ") && root != null) {
        return pushToLeftChild(root, token);
      } else if (root != null) {
        if (root.left == null) {
          return pushToLeftChild(root, token);
        } else {
          root.right = new ASTNode(token);
        }
      } else {
        return new ASTNode(token);
      }
    } else if (root.left == null) {
      root.left = insertChild(root.left, token);
    } else {
        root.right = insertChild(root.right, token);
    }

    return root;
  }

  private ASTNode pushToLeftChild(ASTNode root, TokenInfo newToken) {
    root.left = new ASTNode(root.token);
    root.token = newToken;
    return root;
  }


}
