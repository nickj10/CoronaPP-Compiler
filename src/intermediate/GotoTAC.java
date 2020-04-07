package intermediate;

public class GotoTAC extends ThreeAddrCode {
  public GotoTAC(Label result) {
    super(null, null, null, result);
    this.result = result;
  }

  @Override
  public void printTAC() {
    System.out.println(generateStringTAC());
  }

  public String generateStringTAC() {
    return String.format("GOTO %s", result.generateStringLabel());
  }
}
