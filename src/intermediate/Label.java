package intermediate;

public class Label {
  private static int counter = 0;
  private int id;

  public Label(int id) {
    this.id = id;
  }

  public static Label generateNewLabel() {
    return new Label(counter++);
  }

  public String generateStringLabel() {
   return "L" + this.id;
  }
}
