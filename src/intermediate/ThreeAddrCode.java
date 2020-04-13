package intermediate;

public abstract class ThreeAddrCode {
  protected String arg1;
  protected String arg2;
  protected String op;
  protected Label result;

  public ThreeAddrCode(String arg1, String arg2, String op, Label result) {
    this.arg1 = arg1;
    this.arg2 = arg2;
    this.op = op;
    this.result = result;
  }

  public abstract void printTAC();
}
