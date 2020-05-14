package intermediate;

import SymbolTable.Symbol;

public class CopyTAC extends ThreeAddrCode {
  private static final String TAC_FORMAT = "%s := %s";

  public CopyTAC(Label arg1, Label arg2, Symbol op) {
    super(arg1.getOperand(), arg2.getOperand(), op, null);
    this.arg1 = arg1.getOperand();
    this.arg2 = arg2.getOperand();
    this.op = op;
    this.result = arg1;
  }

  @Override
  public void printTAC() {
    System.out.println(String.format(TAC_FORMAT, arg1.getLexema(), arg2.getLexema()));
  }
}
