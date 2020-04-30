package intermediate;

import java.util.LinkedList;
import java.util.List;

public class BasicBlock {
  private IntermediateCode entryPoint;
  private IntermediateCode exitPoint;
  private LinkedList<IntermediateCode> instructions;

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

  public IntermediateCode getNextInstruction() {
    return instructions.getFirst();
  }

  public IntermediateCode getEntryPoint() {
    return entryPoint;
  }

  public void setEntryPoint(IntermediateCode entryPoint) {
    this.entryPoint = entryPoint;
  }

  public IntermediateCode getExitPoint() {
    return exitPoint;
  }

  public void setExitPoint(IntermediateCode exitPoint) {
    this.exitPoint = exitPoint;
  }
}
