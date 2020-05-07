package intermediate;

import java.util.ArrayList;

public class IntermediateCodeFlow {
  private ArrayList<BasicBlock> basicBlocks;

  public IntermediateCodeFlow() {
    this.basicBlocks = new ArrayList<BasicBlock>();
  }

  public void addNewIC(BasicBlock bb) {
    basicBlocks.add(bb);
  }

  public ArrayList<BasicBlock> getBasicBlocks() {
    return basicBlocks;
  }

  @Override
  public String toString() {
    return "IntermediateCodeFlow {" + "\n" +
            "Basic Blocks {" + "\n" +
            "    " + basicBlocks + "\n" +
            '}';
  }
  
}
