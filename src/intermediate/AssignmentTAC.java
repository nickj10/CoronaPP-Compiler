package intermediate;

import SymbolTable.Symbol;

public class AssignmentTAC extends ThreeAddrCode {
  private static final String TAC_FORMAT = "%s := %s %s %s";
  public AssignmentTAC(Symbol arg1, Symbol arg2, Symbol op, Label result) {
    super(arg1, arg2, op, result);
    this.arg1 = arg1;
    this.arg2 = arg2;
    this.op = op;
    this.result = result;
  }

  @Override
  public void printTAC() {
    System.out.println(String.format(TAC_FORMAT, result.generateStringLabel(), arg1.getLexema(), op.getLexema(),
        arg2.getLexema()));
  }
}
