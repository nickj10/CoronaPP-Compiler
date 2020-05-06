package intermediate;

public class Label {
  private static int counter = 0;
  private int id;
  private String operand;

  public Label(int id) {
    this.id = id;
  }

  public static Label generateNewLabel() {
    return new Label(counter++);
  }

  public String generateStringLabel() {
    return "L" + this.id;
  }

  public String getOperand() {
    return operand;
  }

  public void setOperand(String operand) {
    this.operand = operand;
  }

  @Override
  public String toString() {
    return "ID: " + id + " OP: " + operand;
  }
}
