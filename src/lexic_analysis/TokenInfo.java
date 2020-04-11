package lexic_analysis;

public class TokenInfo {
  private String scope;
  private String token;
  private int declaredAtLine;

  public TokenInfo(String scope, String token, int declaredAtLine) {
    this.scope = scope;
    this.token = token;
    this.declaredAtLine = declaredAtLine;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public int getDeclaredAtLine() {
    return declaredAtLine;
  }

  public void setDeclaredAtLine(int declaredAtLine) {
    this.declaredAtLine = declaredAtLine;
  }
}
