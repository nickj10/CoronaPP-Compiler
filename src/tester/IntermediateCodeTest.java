package tester;

import SymbolTable.Symbol;
import intermediate.AssignmentTAC;
import intermediate.BasicBlock;
import intermediate.IntermediateCode;
import intermediate.IntermediateCodeFlow;
import intermediate.Label;
import intermediate.ThreeAddrCode;

public class IntermediateCodeTest {
  public static void main(String[] args) {
    // Test TACs
    Label testLabel = Label.generateNewLabel();
    testLabel.setOperand("c");
    ThreeAddrCode tac1 = new AssignmentTAC(new Symbol("a"), new Symbol("b"), new Symbol("+"), testLabel);
    IntermediateCodeFlow icFlow = new IntermediateCodeFlow();
    BasicBlock basicBlock = new BasicBlock(new IntermediateCode(tac1));
    icFlow.addNewBasicBlock(basicBlock);
  }
}
