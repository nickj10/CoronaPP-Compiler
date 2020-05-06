package intermediate;

import SymbolTable.Symbol;

public class ConditionalTAC extends ThreeAddrCode {
  private static final String TAC_FORMAT = "IF %s %s %s GOTO %s";
  private GotoTAC gotoTAC;

  public ConditionalTAC(Symbol arg1, Symbol arg2, Symbol op, Label result) {
    super(arg1, arg2, op, result);
    this.arg1 = arg1;
    this.arg2 = arg2;
    this.op = op;
    this.gotoTAC = new GotoTAC(result);
  }

  @Override
  public void printTAC() {
    System.out.println(String.format(TAC_FORMAT, arg1.getLexema(), op.getLexema(), arg2.getLexema(),
        gotoTAC.generateStringTAC()));
  }
}
