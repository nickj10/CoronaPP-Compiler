package intermediate;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a list of instructions to be executed sequentially
 */
public class BasicBlock {
  private static int counter = 0;
  private String basicBlockId;
  private IntermediateCode entryPoint;
  private IntermediateCode exitPoint;
  private LinkedList<IntermediateCode> instructions;

  /**
   *
   * @param entry first instruccion in the basic block
   */
  public BasicBlock(IntermediateCode entry) {
    basicBlockId = String.format("BB%d", counter++);
    entryPoint = entry;
    instructions = new LinkedList<>();
    instructions.add(entry);
  }

  public BasicBlock() {
    basicBlockId = String.format("BB%d", counter++);
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
    this.instructions.add(entryPoint);
  }

  public IntermediateCode getExitPoint() {
    return exitPoint;
  }

  public void setExitPoint(IntermediateCode exitPoint) {
    this.exitPoint = exitPoint;
  }

  public String getBasicBlockId() {
    return basicBlockId;
  }

  public void setBasicBlockId(String basicBlockId) {
    this.basicBlockId = basicBlockId;
  }

  public LinkedList<IntermediateCode> getInstructions() {
    return instructions;
  }
}
