package intermediate;

import lexic_analysis.TokenInfo;

public class TokenInfoLabels {
  private Label label;
  private TokenInfo tokenInfo;

  public TokenInfoLabels(Label label, TokenInfo tokenInfo) {
    this.label = label;
    this.tokenInfo = tokenInfo;
  }

  public Label getLabel() {
    return label;
  }

  public void setLabel(Label label) {
    this.label = label;
  }

  public TokenInfo getTokenInfo() {
    return tokenInfo;
  }

  public void setTokenInfo(TokenInfo tokenInfo) {
    this.tokenInfo = tokenInfo;
  }
}
