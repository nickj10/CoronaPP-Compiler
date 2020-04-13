package intermediate;

public class AssignmentTAC extends ThreeAddrCode {
  private static final String TAC_FORMAT = "%s := %s %s %s";
  public AssignmentTAC(String arg1, String arg2, String op, Label result) {
    super(arg1, arg2, op, result);
    this.arg1 = arg1;
    this.arg2 = arg2;
    this.op = op;
    this.result = result;
  }

  @Override
  public void printTAC() {
    System.out.println(String.format(TAC_FORMAT, result.generateStringLabel(), arg1, op, arg2));
  }
}
