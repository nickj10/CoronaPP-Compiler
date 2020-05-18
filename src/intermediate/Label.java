package intermediate;

import SymbolTable.Symbol;

public class Label {
  private static int counter = 0;
  private int id;
  private Symbol operand;

  public Label(int id) {
    this.id = id;
  }

  public static Label generateNewLabel() {
    return new Label(counter++);
  }

  public String generateStringLabel() {
    return "L" + this.id;
  }

  public Symbol getOperand() {
    return operand;
  }

  public void setOperand(Symbol operand) {
    this.operand = operand;
  }

  @Override
  public String toString() {
    return "ID: " + id + " OP: " + operand;
  }
}
