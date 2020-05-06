package lexic_analysis;

import java.util.ArrayList;

public class TokenInfo {
  private String id;
  private String type;
  private String scope;
  private String token;
  private String tableId;
  private int declaredAtLine;
  private int dataSize;

  public TokenInfo(String scope, String token, int declaredAtLine) {
    this.scope = scope;
    this.token = token;
    this.declaredAtLine = declaredAtLine;
  }

  public TokenInfo (String id, String token, String type, String scope, int declaredAtLine, int dataSize) {
    this.id = id;
    this.token = token;
    this.type = type;
    this.scope = scope;
    this.declaredAtLine = declaredAtLine;
    this.dataSize = dataSize;
  }

  public TokenInfo () { }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getDataSize() {
    return dataSize;
  }

  public void setDataSize(int dataSize) {
    this.dataSize = dataSize;
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

  public String getTableId() {
    return tableId;
  }

  public void setTableId(String tableId) {
    this.tableId = tableId;
  }

  @Override
  public String toString() {
    return id;
  }

  public static String arrayToString(ArrayList<TokenInfo> tokenInfos) {
    StringBuilder str = new StringBuilder();
    for (TokenInfo t: tokenInfos){
      str.append(t.toString()).append(" ");
    }
    return str.toString();
  }
}
