package intermediate;

import SymbolTable.Symbol;
import SymbolTable.SymbolTable;
import syntatic_analysis.ASTNode;
import syntatic_analysis.ASTree;

public abstract class ThreeAddrCode {
    protected Symbol arg1;
    protected Symbol arg2;
    protected Symbol op;
    protected Label result;

    public ThreeAddrCode(Symbol arg1, Symbol arg2, Symbol op, Label result) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.op = op;
        this.result = result;
    }

    public abstract void printTAC();

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

    @Override
    public String toString() {
        return "TAC: " +
                "arg1='" + arg1.getLexema() + '\'' +
                ", arg2='" + arg2.getLexema() + '\'' +
                ", op='" + op.getLexema() + '\'' +
                ", result=" + result;
    }
}
