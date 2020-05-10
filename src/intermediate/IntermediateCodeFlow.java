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
   *
   * @param tree        AST to be transalted
   * @param icFlow      intermediate code flow that contains a list of intermediate codes and TACs
   * @param symbolTable symbol table
   * @deprecated hardcoded way of translating a single instruction to TAC
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
   *
   * @param trees una lista de listas de AST
   */
  public void syntaxTreeToTAC(ArrayList<ArrayList<ASTree>> trees) {
    int i = 0;
    int numTrees = trees.size();
    while (i < numTrees) {
      ArrayList<ASTree> blockTrees = trees.get(i);
      BasicBlock basicBlock = new BasicBlock();
      for (ASTree tree : blockTrees) {
        if (isNodeSpecialCase(tree.getRoot())) {
          // Get the condition for IF and WHILE
          if (tree.getRoot().getToken().getToken().equals("IF")) {
            syntaxTreeToTAC_I(tree.getRoot().getLeft(), basicBlock, new ArrayList<>(), "IF");
          } else if (tree.getRoot().getToken().getToken().equals("WHILE")) {
            syntaxTreeToTAC_I(tree.getRoot().getLeft(), basicBlock, new ArrayList<>(), "WHILE");
          }
          // Convert the rest of the tree to TACs and add it to the Basic Block
          syntaxTreeToTAC_I(tree.getRoot().getRight(), basicBlock, new ArrayList<>(), null);
        } else {
          // Convert the tree directly to TACs
          syntaxTreeToTAC_I(tree.getRoot(), basicBlock, new ArrayList<>(), null);
        }

        // Add this new basic block to the intermediate code flow
        this.basicBlocks.add(basicBlock);
      }
      i++;
    }
  }

  /**
   * Immersive algorithm for tree traversal to visit every node
   *
   * @param current    current node to be evaluated in the AST. Initial valuee is the AST root node.
   * @param basicBlock new basic block to store the intermediate code and TACs
   * @param tokens     list of tokens to be converted into Symbols
   * @param blockType  block type which can either be IF, WHILE or null
   */
  private void syntaxTreeToTAC_I(ASTNode current, BasicBlock basicBlock, ArrayList<TokenInfoLabels> tokens,
      String blockType) {
    // Visits each node in postorder starting from the right side
    if (current.getRight() != null) {
      syntaxTreeToTAC_I(current.getLeft(), basicBlock, tokens, blockType);
    }
    if (current.getLeft() != null) {
      syntaxTreeToTAC_I(current.getRight(), basicBlock, tokens, blockType);
    }

    // Check if we already have three tokens
    if (tokens.size() == 3) {
      IntermediateCode intermediateCode = new IntermediateCode(generateTAC(tokens, blockType));

      // Flush token array
      tokens.clear();
      tokens.add(new TokenInfoLabels(intermediateCode.getLabel(), null));

      if (isTACSpecialCase(intermediateCode)) {
        basicBlock.setEntryPoint(intermediateCode);
        this.basicBlocks.add(basicBlock);
      } else {
        basicBlock.addInstruction(intermediateCode);
      }
    } else {
      tokens.add(new TokenInfoLabels(null, current.getToken()));
    }
  }

  /**
   * Determines if it's a conditional TAC  or a loop TAC
   *
   * @param intermediateCode intermediate code that contains the TAC
   * @return true if it's a conditional TAC or a loop TAC. If not, false.
   */
  private boolean isTACSpecialCase(IntermediateCode intermediateCode) {
    return intermediateCode.getTac() instanceof ConditionalTAC || intermediateCode.getTac() instanceof WhileLoopTAC;
  }

  /**
   * Checks if the node is a special label for IF or WHILE
   *
   * @param node node that contains the label
   * @return true if the node contains IF or WHILE. If not, false.
   */
  private boolean isNodeSpecialCase(ASTNode node) {
    return node.getToken().getToken().equals("IF") || node.getToken().getToken().equals("WHILE");
  }

  /**
   * Generates the Three Address Code according to type
   *
   * @param tokens list of tokens to analyze
   * @return generated TAC
   */
  private ThreeAddrCode generateTAC(ArrayList<TokenInfoLabels> tokens, String blockType) {
    ThreeAddrCode tac = null;

    // Retrieve all the tokens to be added to TAC
    Symbol arg2 = tokenInfoToSymbol(tokens.get(0).getTokenInfo());
    Symbol arg1 = tokenInfoToSymbol(tokens.get(1).getTokenInfo());
    Symbol op = tokenInfoToSymbol(tokens.get(2).getTokenInfo());
    Label label = Label.generateNewLabel();

    // Retrieve labels
    Label labelArg1 = tokens.get(0).getLabel();
    Label labelArg2 = tokens.get(1).getLabel();

    // Create TACs depending on its type
    if (blockType != null) {
      if (blockType.equals("IF")) {
        tac = new ConditionalTAC(arg1, arg2, op, label);
      } else if (blockType.equals("WHILE")) {
        tac = new WhileLoopTAC(arg1, arg2, op, label);
      }
    } else {
      if (op.getToken().equals("ASSGN_EQ")) {
        if (labelArg2 != null) { // arg2 is already a label
          labelArg1.generateStringLabel();
          labelArg1.setOperand(arg1);
          tac = new CopyTAC(labelArg1, labelArg2, op);
        } else if (labelArg1 != null) { // result is already a label
          labelArg2.generateStringLabel();
          labelArg2.setOperand(arg1);
          tac = new CopyTAC(labelArg1, labelArg2, op);
        } else { // None of them are labels
          Label result = Label.generateNewLabel();
          result.setOperand(arg2);
          labelArg1.generateStringLabel();
          labelArg1.setOperand(arg1);
          tac = new CopyTAC(labelArg1, result, op);
        }

      }
      tac = new AssignmentTAC(arg1, arg2, op, label);
    }

    return tac;
  }

  /**
   * Map TokenInfo object to Symbol object
   *
   * @param token TokenInfo object that we want to map
   * @return Symbol object
   */
  private static Symbol tokenInfoToSymbol(TokenInfo token) {
    Symbol symbol = new Symbol();
    symbol.setId(String.format("%s%d", token.getId(), token.getDeclaredAtLine()));
    symbol.setLexema(token.getId());
    symbol.setToken(token.getToken());
    symbol.setType(token.getType());
    symbol.setScope(token.getScope());
    symbol.setDeclaredAtLine(token.getDeclaredAtLine());
    symbol.setDataSize(token.getDataSize());
    return symbol;
  }
}
