package intermediate;

public class CopyTAC extends ThreeAddrCode {
  private static final String TAC_FORMAT = "%s := %s";

  public CopyTAC(String arg1, String arg2, Label result) {
    super(arg1, arg2, "=", result);
    this.arg1 = arg1;
    this.arg2 = arg2;
    this.op = "=";
    this.result = result;
  }

  @Override
  public void printTAC() {
    System.out.println(String.format(TAC_FORMAT, arg2, arg1));
  }
}
