package intermediate;

import java.util.LinkedList;
import java.util.List;

public class BasicBlock {
  private IntermediateCode entryPoint;
  private IntermediateCode exitPoint;
  private List<IntermediateCode> instructions;

  public BasicBlock(IntermediateCode entry, IntermediateCode exit) {
    entryPoint = entry;
    exitPoint = exit;
    instructions = new LinkedList<>();
  }

  public void addInstruction(IntermediateCode intermediateCode) {
    instructions.add(intermediateCode);
  }

  public void addInstructions(LinkedList<IntermediateCode> instructions) {
    this.instructions = instructions;
  }

}
