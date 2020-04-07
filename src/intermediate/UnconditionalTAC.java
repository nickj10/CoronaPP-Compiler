package intermediate;

public class UnconditionalTAC extends ThreeAddrCode {
  private static final String TAC_FORMAT = "IF %s %s %s %s ELSE %s";
  private Label altResult;

  public UnconditionalTAC(String arg1, String arg2, String op, Label result, Label altResult) {
    super(arg1, arg2, op, result);
    this.arg1 = arg1;
    this.arg2 = arg2;
    this.op = op;
    this.result = result;
    this.altResult = altResult;
  }

  @Override
  public void printTAC() {
    System.out.println(
        String.format(TAC_FORMAT, arg1, op, arg2, result.generateStringLabel(), altResult.generateStringLabel()));
  }
}
