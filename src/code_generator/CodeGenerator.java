package code_generator;

import SymbolTable.Symbol;
import SymbolTable.Table;
import intermediate.AssignmentTAC;
import intermediate.CopyTAC;
import intermediate.GotoTAC;
import intermediate.ThreeAddrCode;

import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

public class CodeGenerator {
    ArrayList<String> main; /* MIPS programs execute  instructions sequentially, where the code under
                               this label will be executed first   */
    ArrayList<String> data; /* Section where data is stored in memory (allocated in RAM), similar to
                               variables in higher-level languages */
    MemManager memManager;
    RegManager regManager;
    Stack<String> blockStack;

    public CodeGenerator() {
        this.main = new ArrayList<String>();
        this.data = new ArrayList<String>();
        memManager = new MemManager();
        regManager = new RegManager();
        blockStack = new Stack<>();
    }

    public void declareVar(Symbol s){
        // Creating instruction to allocate memory in stack
        String instr = String.format("SUB $sp, $sp, %d", s.getDataSize());
        main.add(instr);
        // Updating pointer stack
        memManager.pushVar(s);
    }

    public void beginBlock(Table table){
        // TODO: This code is horrible. Please change it!
        Set<Integer> keys = table.getTable().keySet();
        for(Integer key : keys){
            if(table.getTable().get(key).getToken().equals("IDENTIFIER"))
                declareVar(table.getTable().get(key));
        }
    }

    public void addInstr(ThreeAddrCode tac){
        Symbol arg1 = tac.getArg1();
        Symbol arg2 = tac.getArg2();
        Symbol op = tac.getOp();
        Symbol result = tac.getResult().getOperand();

        if(tac instanceof AssignmentTAC)
            addOperation(arg1, arg2, op, result);   // result = arg1 [op] arg2
        if(tac instanceof CopyTAC)
            addCopy(arg1, arg2);                    // arg2 = arg1
        // TODO: Do the other functions (copy, conditional, goto, etc)
    }

    private void addOperation(Symbol arg1, Symbol arg2, Symbol op, Symbol result){
        // Loading arg1 into a register in regManager
        int reg1 = loadToRegister(arg1);
        // Writing instruction in MIPS to load arg1 (stack) into reg1
        loadWord(reg1, arg1);

        // Loading arg2 into a register in regManager
        int reg2 = loadToRegister(arg2);
        // Writing instruction in MIPS to load arg2 (stack) into reg2
        loadWord(reg2, arg2);

        // Getting a free register to store the result (in reg3)
        int reg3 = regManager.getFreeReg();
        // TODO: Change the following line to a function to determine the respective operation (instead of just ADD)
        String operation = op.getLexema();
        switch(operation){
            case "+":
                addSum(reg1, reg2, reg3); // reg3 = reg1 + reg2;
                break;
            case "-":
                addSub(reg1, reg2, reg3); // reg3 = reg1 - reg2;
                break;
            case "*":
                addMul(reg1, reg2, reg3); // reg3 = reg1 * reg2;
                break;
            case "/":
                addDiv(reg1, reg2, reg3); // reg3 = reg1 / reg2;
                break;
            default:
                break;  // Shouldn't happen
        }

        // Writing instruction in MIPS to save the word in reg3 into the stack (with its respective position)
        saveWord(reg3, result);

    /* TODO: Free registers after usage, maybe by checking if the register is going to be used as result holder or not
        i.e. if reg3 = reg1 + reg2, you can free reg1 and reg2 since these wont be re-used, contrary to reg3 which
        actually holds the result and it hasn't been stored
     */
    }

    private void addCopy(Symbol arg1, Symbol result){
        int reg = loadToRegister(arg1);

        if(arg1.getToken().equals("IDENTIFIER"))
            loadWord(reg, arg1);
        else if(arg1.getToken().equals("NUMBER"))
            loadLiteral(reg, arg1.getLexema());

        saveWord(reg, result);
    }

    private void addIf(Symbol arg1, Symbol op, Symbol arg2, GotoTAC gotoLabel){
        String operation = op.getLexema();
        String instr = "";
        String label = gotoLabel.getResult().generateStringLabel();
        blockStack.push(label);

        switch(operation){
            case "==":
                instr += "bne"; // adds "!="
                break;
            case "!=":
                instr += "beq"; // adds "=="
                break;
            case ">":
                instr += "ble"; // adds "<="
                break;
            case "<":
                instr += "bge"; // adds ">="
                break;
            case "<=":
                instr += "bgt"; // adds ">"
                break;
            case ">=":
                instr += "blt"; // adds "<"
                break;
        }
        // Loading arg1 into a register in regManager
        int reg1 = loadToRegister(arg1);
        // Writing instruction in MIPS to load arg1 (stack) into reg1
        loadWord(reg1, arg1);

        // Loading arg2 into a register in regManager
        int reg2 = loadToRegister(arg2);
        // Writing instruction in MIPS to load arg2 (stack) into reg2
        loadWord(reg2, arg2);
        instr += String.format(" $t%d, $t%d, %s", reg1, reg2, label); // [op] $t[reg1], $t[reg2], [label]

        main.add(instr);
    }

    public void endBlock(){
        String label = blockStack.pop();
        main.add(label);
    }
    // These functions works as above but instead of Symbols they used "labels" or literal numbers
    /*
    public int addInstr(Symbol arg1, Symbol arg2, char instr, String label){
        String instr = String.format("");

        main.add(instr);
    }

    public int addInstr(Symbol arg1, Symbol arg2, char instr, Symbol result){
        String instr = String.format("");

        main.add(instr);
    }*/

    private void saveWord(int reg, Symbol s){
        String instr = String.format("SW $t%d, $d($fp)", reg, s.getStackPointer()); // -4
        main.add(instr);
    }

    private void loadWord(int reg, Symbol s){
        String instr = String.format("LW $t%d, %d($fp)", reg, s.getStackPointer()); // -4
        main.add(instr);
    }

    private void loadLiteral(int reg, String l){
        String instr = String.format("LI $t%d, %s", reg, l);
        main.add(instr);
    }

    public int loadToRegister(Symbol s){
        int reg = regManager.getFreeReg();
        regManager.getByIndex(reg).setName(s.getId());

        return reg;
    }

    public int loadToRegister(String label){
        int reg = regManager.getFreeReg();
        regManager.getByIndex(reg).setName(label);

        return reg;
    }

    // TODO: Create methods for every possible operation (such as assignation [=], subtraction [-], mult [*],...)
    private int addSum(int reg1, int reg2, int result){
        String instr =  String.format("ADD $t%d, $t%d, $t%d", result, reg1, reg2); // result = reg1 + reg2
        main.add(instr);

        return result;
    }

    private int addSub(int reg1, int reg2, int result){
        String instr =  String.format("SUB $t%d, $t%d, $t%d", result, reg1, reg2); // result = reg1 + reg2
        main.add(instr);

        return result;
    }

    private int addMul(int reg1, int reg2, int result){
        String instr =  String.format("MUL $t%d, $t%d, $t%d", result, reg1, reg2); // result = reg1 + reg2
        main.add(instr);

        return result;
    }

    private int addDiv(int reg1, int reg2, int result){
        String instr =  String.format("DIV $t%d, $t%d, $t%d", result, reg1, reg2); // result = reg1 + reg2
        main.add(instr);

        return result;
    }

    // Non-used method
    public void exists(){
        return;
    }

}