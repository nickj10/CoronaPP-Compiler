package intermediate;

import syntatic_analysis.ASTNode;
import syntatic_analysis.ASTree;

import java.util.ArrayList;

public abstract class ThreeAddrCode {
    protected String arg1;
    protected String arg2;
    protected String op;
    protected Label result;

    public ThreeAddrCode(String arg1, String arg2, String op, Label result) {
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.op = op;
        this.result = result;
    }

    public abstract void printTAC();

    public static void syntaxTreeToTAC(ASTree tree, IntermediateCodeFlow icFlow) {
        //Empieza en 1 porque empezamos a partir del =
        int nAddress = 1;
        ASTNode node = null;

        if (tree.getRoot() != null) {
            node = tree.getRoot().getRight();
        }

        if (node != null) {
            String tokenNode = node.getToken().getToken();
            while (tokenNode.equals("ARTMTC_SM") || tokenNode.equals("ARTMTC_RS")) {
                //Add left adress
                nAddress++;
                //Add right adress only if is the last operation
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
            if (tokenNode.equals("ARTMTC_SM") || tokenNode.equals("ARTMTC_RS")) {
                node = node.getRight();
                tac1 = new AssignmentTAC(node.getRight().getToken().getId(), node.getLeft().getToken().getId(), node.getToken().getId(), testLabel);
            } else {
                tac1 = new AssignmentTAC(node.getRight().getToken().getId(), null, node.getToken().getId(), testLabel);
            }

            icFlow.addNewIC(new IntermediateCode(tac1));

        }
    }

    @Override
    public String toString() {
        return "TAC: " +
                "arg1='" + arg1 + '\'' +
                ", arg2='" + arg2 + '\'' +
                ", op='" + op + '\'' +
                ", result=" + result;
    }
}
