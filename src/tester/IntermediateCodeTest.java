package tester;

import intermediate.AssignmentTAC;
import intermediate.IntermediateCode;
import intermediate.IntermediateCodeFlow;
import intermediate.Label;
import intermediate.ThreeAddrCode;

public class IntermediateCodeTest {
  public static void main(String[] args) {
    // Test TACs
    Label testLabel = Label.generateNewLabel();
    testLabel.setOperand("c");
    ThreeAddrCode tac1 = new AssignmentTAC("a", "b", "+", testLabel);
    IntermediateCodeFlow icFlow = new IntermediateCodeFlow();
    icFlow.addNewIC(new IntermediateCode(tac1));
  }
}
