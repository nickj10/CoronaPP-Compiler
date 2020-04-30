package intermediate;

import java.util.ArrayList;

public class IntermediateCodeFlow {
  private ArrayList<IntermediateCode> tacCodes;

  public IntermediateCodeFlow() {
    this.tacCodes = new ArrayList<IntermediateCode>();
  }

  public void addNewIC(IntermediateCode ic) {
    tacCodes.add(ic);
  }

  @Override
  public String toString() {
    return "IntermediateCodeFlow {" + "\n" +
            "TAC Codes {" + "\n" +
            "    " + tacCodes + "\n" +
            '}';
  }
  
}
