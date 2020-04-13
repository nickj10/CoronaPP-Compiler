package intermediate;

public class ConditionalTAC extends ThreeAddrCode {
  private static final String TAC_FORMAT = "IF %s %s %s GOTO %s";
  private GotoTAC gotoTAC;

  public ConditionalTAC(String arg1, String arg2, String op, Label result) {
    super(arg1, arg2, op, result);
    this.arg1 = arg1;
    this.arg2 = arg2;
    this.op = op;
    this.gotoTAC = new GotoTAC(result);
  }

  @Override
  public void printTAC() {
    System.out.println(String.format(TAC_FORMAT, arg1, op, arg2, gotoTAC.generateStringTAC()));
  }
}
