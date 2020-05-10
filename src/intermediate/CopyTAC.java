package intermediate;

import SymbolTable.Symbol;

public class CopyTAC extends ThreeAddrCode {
  private static final String TAC_FORMAT = "%s := %s";

  public CopyTAC(Label arg1, Label result, Symbol op) {
    super(arg1.getOperand(), null, op, result);
    this.arg1 = arg1.getOperand();
    this.arg2 = null;
    this.op = op;
    this.result = result;
  }

  @Override
  public void printTAC() {
    System.out.println(String.format(TAC_FORMAT, result.getOperand(), arg1.getLexema()));
  }
}
