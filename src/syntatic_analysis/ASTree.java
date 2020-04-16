package syntatic_analysis;

import lexic_analysis.TokenInfo;

import java.util.ArrayList;

public class ASTree {
  private ASTNode root;
  private ArrayList<TokenInfo> tokensInfo;

  public ASTree () {
    root = null;
    tokensInfo = new ArrayList();
  }

  public void insert(TokenInfo token) {
    root = insertChild(root, token);
  }

  private ASTNode insertChild (ASTNode root, TokenInfo token) {
    if (root == null || !root.getToken().getToken().equals("ASSGN_EQ")) {
      // If token is = then assign it to the root
      if (token.getToken().equals("ASSGN_EQ") && root != null) {
        return pushToLeftChild(root, token);
      } else if (root != null) {
        if (root.getLeft() == null) {
          return pushToLeftChild(root, token);
        } else {
          root.setRight(new ASTNode(token));
        }
      } else {
        return new ASTNode(token);
      }
    } else if (root.getLeft() == null) {
      root.setLeft(insertChild(root.getLeft(), token));
    } else {
        root.setRight(insertChild(root.getRight(), token));
    }

    return root;
  }

  private ASTNode pushToLeftChild(ASTNode root, TokenInfo newToken) {
    root.setLeft(new ASTNode(root.getToken()));
    root.setToken(newToken);
    return root;
  }

  /**
   * Inorder Trasversal of the tree
   * @param node root node
   */
  public ArrayList<TokenInfo> visitAST (ASTNode node) {
    if (node == null) {
      return null;
    }
    //Inorder
    visitAST(node.getRight());
    tokensInfo.add(node.getToken());
    System.out.println(node.getToken().getId());
    visitAST(node.getLeft());
    return tokensInfo;
  }

  public ASTNode clear () {
    root = null;
    return root;
  }
  public ASTNode getRoot() {
    return root;
  }
}
