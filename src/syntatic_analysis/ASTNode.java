package syntatic_analysis;

import lexic_analysis.TokenInfo;

public class ASTNode {
  ASTNode left;
  TokenInfo token;
  ASTNode right;

  public ASTNode(TokenInfo token) {
    this.token = token;
    this.left = null;
    this.right = null;
  }

  public ASTNode getLeft() {
    return left;
  }

  public void setLeft(ASTNode left) {
    this.left = left;
  }

  public TokenInfo getToken() {
    return token;
  }

  public void setToken(TokenInfo token) {
    this.token = token;
  }

  public ASTNode getRight() {
    return right;
  }

  public void setRight(ASTNode right) {
    this.right = right;
  }
}
