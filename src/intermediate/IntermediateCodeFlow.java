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

  public void addNewBasicBlock(BasicBlock bb) {
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
   * @param trees una lista de listas de AST
   */
  public void syntaxTreeToTAC(ArrayList<ArrayList<ASTree>> trees) {
    int i = 0;
    int numTrees = trees.size();
    while (i < numTrees) {
      ArrayList<ASTree> blockTrees = trees.get(i);
      BasicBlock basicBlock = new BasicBlock();
      for (int j = 0; j < blockTrees.size(); j++) {
        ASTree tree = blockTrees.get(j);

        // Control null roots. Get next tree.
        while (tree.getRoot() == null) {
          tree = blockTrees.get(++j);
        }

        // Check if it's an IF or WHILE
        if (isNodeSpecialCase(tree.getRoot())) {
          ASTree nextTree = blockTrees.get(++j);
          ArrayList<TokenInfoLabels> tokens = new ArrayList<>();

          // Get the condition for IF and WHILE
          if (tree.getRoot().getToken().getToken().equals("IF")) {
            syntaxTreeToTAC_I(nextTree.getRoot(), basicBlock, tokens, "IF");
          } else if (tree.getRoot().getToken().getToken().equals("WHILE")) {
            syntaxTreeToTAC_I(nextTree.getRoot(), basicBlock, tokens, "WHILE");
          }
        } else {
          // Convert the tree directly to TACs
          syntaxTreeToTAC_I(tree.getRoot(), basicBlock, new ArrayList<>(), null);
        }
      }
      this.basicBlocks.add(basicBlock);
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
      syntaxTreeToTAC_I(current.getRight(), basicBlock, tokens, blockType);
    }
    if (current.getLeft() != null) {
      syntaxTreeToTAC_I(current.getLeft(), basicBlock, tokens, blockType);
    }

    // Check if we already have three tokens
    if (tokens.size() == 2) {
      // Add the last token
      tokens.add(new TokenInfoLabels(null, current.getToken()));

      // Create the intermediate code
      IntermediateCode intermediateCode = new IntermediateCode(generateTAC(tokens, blockType));

      // Flush token array
      tokens.clear();
      tokens.add(new TokenInfoLabels(intermediateCode.getLabel(),
          new TokenInfo(intermediateCode.getLabel().generateStringLabel())));

      // If TAC is a special case, it indicates that it's the first instruction
      if (isTACSpecialCase(intermediateCode)) {
        basicBlock.setEntryPoint(intermediateCode);
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
    Label labelArg2 = tokens.get(0).getLabel();
    Label labelArg1 = tokens.get(1).getLabel();

    // Create TACs depending on its type
    if (blockType != null) {
      if (blockType.equals("IF")) {
        tac = new ConditionalTAC(arg1, arg2, op, label);
      } else if (blockType.equals("WHILE")) {
        tac = new WhileLoopTAC(arg1, arg2, op, label);
      }
    } else {
      if (op.getToken().equals("ASSGN_EQ")) { // arg1 op arg2 || arg1 = arg2
        if (labelArg2 != null) { // arg2 is already a label || prev = L1
          labelArg1 = Label.generateNewLabel();
          labelArg1.setOperand(arg1);
          labelArg2.setOperand(arg2);
          tac = new CopyTAC(labelArg1, labelArg2, op);
        } else if (labelArg1 != null) { // result is already a label || L1 = prev
          labelArg2 = Label.generateNewLabel();
          labelArg2.generateStringLabel();
          labelArg2.setOperand(arg1);
          tac = new CopyTAC(labelArg1, labelArg2, op);
        } else { // None of them are labels || prev = 6
          label.setOperand(arg2); // Use previously defined label for result
          labelArg1 = Label.generateNewLabel();
          labelArg1.generateStringLabel();
          labelArg1.setOperand(arg1);
          tac = new CopyTAC(labelArg1, label, op);
        }
      } else { // normal operations || c = a + b
        tac = new AssignmentTAC(arg1, arg2, op, label);
      }
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
