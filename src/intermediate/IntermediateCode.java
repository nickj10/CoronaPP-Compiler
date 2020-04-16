package intermediate;

public class IntermediateCode {
  private Label label;
  private ThreeAddrCode tac;

  public IntermediateCode(Label label, ThreeAddrCode tac) {
    this.label = label;
    this.tac = tac;
  }

  public IntermediateCode(ThreeAddrCode tac) {
    this.label = Label.generateNewLabel();
    this.tac = tac;
  }
}
