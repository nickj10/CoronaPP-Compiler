package intermediate;

import SymbolTable.SymbolTable;
import SymbolTable.Symbol;
import java.util.ArrayList;
import lexic_analysis.TokenInfo;
import syntatic_analysis.ASTNode;
import syntatic_analysis.ASTree;

/**
 * Defines the flow of basic blocks
 */
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

  /**
   * Translates AST to TACs and BasicBlocks
   * @deprecated hardcoded way of translating a single instruction to TAC
   * @param tree AST to be transalted
   * @param icFlow intermediate code flow that contains a list of intermediate codes and TACs
   * @param symbolTable symbol table
   */
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
      testLabel.setOperand(tokenInfoToSymbol(node.getLeft().getToken()));

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

  /**
   * Translates AST to TACs and BasicBlocks
   * @param current current node to be evaluated in the AST. Initial valuee is the AST root node.
   * @param basicBlock new basic block to store the intermediate code and TACs
   * @param tokens list of tokens to be converted into Symbols
   */
  public void syntaxTreeToTAC(ASTNode current, BasicBlock basicBlock, ArrayList<TokenInfo> tokens) {
    if(current.getRight() != null) {
      syntaxTreeToTAC(current.getLeft(), basicBlock, tokens);
    }
    if (current.getLeft() != null) {
      syntaxTreeToTAC(current.getRight(), basicBlock, tokens);
    }

    // Check if we already have three tokens
    if (tokens.size() == 3) {
      IntermediateCode intermediateCode = new IntermediateCode(generateTAC(tokens));
      // If it's the first instruction in the block, we set it as entry point
      if (basicBlock.getInstructions().isEmpty()) {
        basicBlock.setEntryPoint(intermediateCode);
      } else {
        basicBlock.addInstruction(intermediateCode);
      }
    } else {
      tokens.add(current.getToken());
    }

    // TODO: Add BasicBlock to IntermediateCodeFlow object
  }

  /**
   * Generates the Three Address Code according to type
   * @param tokens list of tokens to analyze
   * @return generated TAC
   */
  private ThreeAddrCode generateTAC(ArrayList<TokenInfo> tokens) {
    ThreeAddrCode tac;

    // Retrieve all the tokens to be added to TAC
    Symbol op = tokenInfoToSymbol(tokens.get(0));
    Symbol arg2 = tokenInfoToSymbol(tokens.get(1));
    Symbol arg1 = tokenInfoToSymbol(tokens.get(2));
    Label label = Label.generateNewLabel();

    // Create TACs depending on its type
    if (op.getType().equals("IF")) {
      tac = new ConditionalTAC(arg1, arg2, op, label);
    } else if (op.getType().equals("WHILE")) {
      tac = new WhileLoopTAC(arg1, arg2, op, label);
    } else {
      tac = new AssignmentTAC(arg1, arg2, op, label);
    }

    return tac;
  }

  /**
   * Map TokenInfo object to Symbol object
   * @param token TokenInfo object that we want to map
   * @return Symbol object
   */
  private static Symbol tokenInfoToSymbol(TokenInfo token) {
    Symbol symbol = new Symbol();
    symbol.setId(String.format("%s%d", token.getId(), token.getDeclaredAtLine()));
    symbol.setLexema(token.getId());
    symbol.setType(token.getType());
    symbol.setScope(token.getScope());
    symbol.setDeclaredAtLine(token.getDeclaredAtLine());
    symbol.setDataSize(token.getDataSize());
    return symbol;
  }
  
}
