package intermediate;

import SymbolTable.Symbol;

public class CopyTAC extends ThreeAddrCode {
  private static final String TAC_FORMAT = "%s := %s";

  public CopyTAC(Symbol arg1, Symbol arg2, Label result) {
    super(arg1, arg2, new Symbol("="), result);
    this.arg1 = arg1;
    this.arg2 = arg2;
    this.op = new Symbol("=");
    this.result = result;
  }

  @Override
  public void printTAC() {
    System.out.println(String.format(TAC_FORMAT, arg2.getLexema(), arg1.getLexema()));
  }
}
