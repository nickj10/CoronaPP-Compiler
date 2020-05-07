package intermediate;

import SymbolTable.SymbolTable;
import SymbolTable.Symbol;
import java.util.ArrayList;
import lexic_analysis.TokenInfo;
import syntatic_analysis.ASTNode;
import syntatic_analysis.ASTree;

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

  public static void syntaxTreeToTAC(ASTree tree, IntermediateCodeFlow icFlow, SymbolTable symbolTable) {
    //Empieza en 1 porque empezamos a partir del =
    int nAddress = 1;
    ASTNode node = null;

    if (tree.getRoot() != null) {
      node = tree.getRoot().getRight();
    }

    if (node != null) {
      String tokenNode = node.getToken().getToken();
      while (tokenNode.equals("ARTMTC_SM") || tokenNode.equals("ARTMTC_RS")) {
        //Add left address
        nAddress++;
        //Add right address only if is the last operation
        String rightNodeToken = node.getRight().getToken().getToken();
        if (rightNodeToken.equals("IDENTIFIER") || rightNodeToken.equals("NUMBER")) {
          nAddress++;
        }

        node = node.getRight();
        tokenNode = node.getToken().getToken();

      }
    }

    int nTacs = (int) Math.ceil(nAddress / 3);
    if (tree.getRoot() != null) {
      node = tree.getRoot();

      Label testLabel = Label.generateNewLabel();
      testLabel.setOperand(node.getLeft().getToken().getId());

      ThreeAddrCode tac1;

      String tokenNode = node.getRight().getToken().getToken();
      node = node.getRight();
      Symbol arg1 = symbolTable.getSymbol(node.getRight().getToken().getId(), node.getRight().getToken().getTableId());
      Symbol arg2 = symbolTable.getSymbol(node.getLeft().getToken().getId(), node.getLeft().getToken().getTableId());
      Symbol op = symbolTable.getSymbol(node.getToken().getId(), node.getToken().getTableId());
      if (tokenNode.equals("ARTMTC_SM") || tokenNode.equals("ARTMTC_RS")) {
        tac1 = new AssignmentTAC(arg1, arg2, op, testLabel);
      } else {
        tac1 = new AssignmentTAC(arg1, null, op, testLabel);
      }

      icFlow.addNewIC(new BasicBlock(new IntermediateCode(tac1)));

    }
  }

  public void syntaxTreeToTAC(ASTNode current) {
    if(current.getRight() != null) {
      syntaxTreeToTAC(current.getLeft());
    }
    if (current.getLeft() != null) {
      syntaxTreeToTAC(current.getRight());
    }
    TokenInfo currentNode = current.getToken();
    if (currentNode.getToken().equals("IDENTIFIER") || currentNode.getToken().equals("NUMBER")) {

    }
  }
  
}
