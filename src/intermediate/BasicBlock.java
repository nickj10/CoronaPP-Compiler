package intermediate;

import java.util.LinkedList;

public class BasicBlock {
  private IntermediateCode entryPoint;
  private IntermediateCode exitPoint;
  private LinkedList<IntermediateCode> instructions;
}
