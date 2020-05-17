package code_generator;

import SymbolTable.Symbol;
import SymbolTable.Table;
import intermediate.*;

import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.locks.Condition;

public class CodeGenerator {
    ArrayList<String> main; /* MIPS programs execute  instructions sequentially, where the code under
                               this label will be executed first   */
    ArrayList<String> data; /* Section where data is stored in memory (allocated in RAM), similar to
                               variables in higher-level languages */
    MemManager memManager;
    RegManager regManager;
    Stack<String> blockStack;
    private static int ifCounter = 0;
    private static int whileCounter = 0;

    public CodeGenerator() {
        this.main = new ArrayList<String>();
        this.data = new ArrayList<String>();
        memManager = new MemManager();
        regManager = new RegManager();
        blockStack = new Stack<>();
    }

    public void startProgram(){
        main.add(new String("MOVE $fp, $sp"));
    }

    public void declareVar(Symbol s){
        // Creating instruction to allocate memory in stack
        String instr = String.format("SUB $sp, $sp, %d #Reserve memory for <%s>", s.getDataSize(), s.getLexema());
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

    public String getTabsPerBlock(){
        String tabs = "";
        if(blockStack.empty())
            return "";

        int blockStackSize = blockStack.size();

        for(int i=0; i<blockStackSize; i++)
            tabs += "   ";

        return tabs;
    }

    public void addInstr(ThreeAddrCode tac){
        Symbol arg1 = tac.getArg1();
        Symbol arg2 = tac.getArg2();
        Symbol op = tac.getOp();
        Label result = tac.getResult();

        if(tac instanceof AssignmentTAC)
            addOperation(arg1, arg2, op, result);   // result = arg1 [op] arg2
        else if(tac instanceof CopyTAC)
            addCopy(arg1, arg2);                    // arg2 = arg1
        else if(tac instanceof ConditionalTAC)
            addIf(arg1, arg2, op, result);
        else if(tac instanceof WhileLoopTAC)
            addWhile(arg1, arg2, op, result);
        // TODO: Do the other functions (copy, conditional, goto, etc)
    }

    private void addOperation(Symbol arg1, Symbol arg2, Symbol op, Label result){
        // Loading arg1 into a register in regManager
        int reg1 = loadToRegister(arg1);
        // Writing instruction in MIPS to load arg1 (stack) into reg1
        loadVar(arg1, reg1);

        // Loading arg2 into a register in regManager
        int reg2 = loadToRegister(arg2);
        // Writing instruction in MIPS to load arg2 (stack) into reg2
        loadVar(arg2, reg2);

        // Getting a free register to store the result (a Label)
        int reg3 = loadToRegister(result.generateStringLabel());

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
        //saveWord(reg3, result);

        regManager.freeReg(reg1);
        regManager.freeReg(reg2);
    }

    private void addCopy(Symbol arg1, Symbol result){
        int reg;
        if(result.getLexema() == null){
            reg = regManager.getBusyReg();
        }else {
            reg = loadToRegister(result);

            if (result.getToken().equals("IDENTIFIER"))
                loadWord(reg, result);
            else if (result.getToken().equals("NUMBER"))
                loadLiteral(reg, result.getLexema());
        }
        saveWord(reg, arg1);
        regManager.freeReg(reg);
    }

    private void addIf(Symbol arg1, Symbol arg2, Symbol op, Label gotoLabel){
        String operation = op.getLexema();
        String instr = "";
        String label = String.format("L%d", ifCounter++);

        instr += getConditional(arg1, arg2, op, label, null);
        main.add(instr);
        blockStack.push(label);
    }

    private void addWhile(Symbol arg1, Symbol arg2, Symbol op, Label gotoLabel){
        String operation = op.getLexema();
        String instr = "";
        String endWhileLabel = String.format("end_while%d", whileCounter);
        String beginWhileLabel = String.format("while%d", whileCounter++);

        instr += getConditional(arg1, arg2, op, endWhileLabel, beginWhileLabel);
        main.add(instr);
        blockStack.push(endWhileLabel);
    }

    private String getConditional(Symbol arg1, Symbol arg2, Symbol op, String gotoLabel, String beginLabel){
        String operation = op.getLexema();
        String instr = "";

        instr = parseConditional(operation);

        // Loading arg1 into a register in regManager
        int reg1 = loadToRegister(arg1);
        // Writing instruction in MIPS to load arg1 (stack) into reg1
        loadVar(arg1, reg1);

        // Loading arg2 into a register in regManager
        int reg2 = loadToRegister(arg2);
        // Writing instruction in MIPS to load arg2 (stack) into reg2
        loadVar(arg2, reg2);
        if(beginLabel != null)
            main.add(beginLabel+":");
        instr += String.format(" $t%d, $t%d, %s", reg1, reg2, gotoLabel); // [op] $t[reg1], $t[reg2], [label]

        return instr;
    }

    private String parseConditional(String operation){
        String instr = "";

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

        return instr;
    }

    public void endBlock(){
        if(blockStack.empty())
            return;

        String label = blockStack.peek();

        /* TODO: The following code is horrible! Please refactor it. Current solution use a hacky way to obtain the
            current block index. You can obtain it by storing it inside the blockStack or something like that.
            Think about it!
         */
        if(label.contains("end_while")){
            String instr = getTabsPerBlock();
            instr += "j ";
            instr += label.substring(4, label.length());
            main.add(instr);
        }
        main.add(label+":");
        blockStack.pop();
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

    /**
     * Check whether or not the variable is a literal or a Word (an actual variable)
     * @param arg: Symbol that holds needed information
     * @param reg: Register in which this variable is going to be loaded
     */
    private void loadVar(Symbol arg, int reg){
        if(arg.getToken().equals("IDENTIFIER"))
            loadWord(reg, arg);
        else if(arg.getToken().equals("NUMBER"))
            loadLiteral(reg, arg.getLexema());
    }
    private void saveWord(int reg, Symbol s){
        String instr = "";
        instr += getTabsPerBlock();
        instr += String.format("SW $t%d, %d($fp) #Save var <%s>", reg, s.getStackPointer(), s.getLexema()); // -4
        main.add(instr);
    }

    private void loadWord(int reg, Symbol s){
        String instr = "";
        instr += getTabsPerBlock();
        instr += String.format("LW $t%d, %d($fp) #Load var <%s>", reg, s.getStackPointer(), s.getLexema()); // -4
        main.add(instr);
    }

    private void loadLiteral(int reg, String l){
        String instr = "";
        instr += getTabsPerBlock();
        instr += String.format("LI $t%d, %s", reg, l);
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
        String instr = "";
        instr += getTabsPerBlock();
        instr += String.format("ADD $t%d, $t%d, $t%d", result, reg1, reg2); // result = reg1 + reg2
        main.add(instr);

        return result;
    }

    private int addSub(int reg1, int reg2, int result){
        String instr = "";
        instr += getTabsPerBlock();
        instr += String.format("SUB $t%d, $t%d, $t%d", result, reg1, reg2); // result = reg1 + reg2
        main.add(instr);

        return result;
    }

    private int addMul(int reg1, int reg2, int result){
        String instr = "";
        instr += getTabsPerBlock();
        instr += String.format("MUL $t%d, $t%d, $t%d", result, reg1, reg2); // result = reg1 + reg2
        main.add(instr);

        return result;
    }

    private int addDiv(int reg1, int reg2, int result){
        String instr = "";
        instr += getTabsPerBlock();
        instr += String.format("DIV $t%d, $t%d, $t%d", result, reg1, reg2); // result = reg1 + reg2
        main.add(instr);

        return result;
    }

    public ArrayList<String> getMain() {
        return main;
    }

    // Non-used method
    public void exists(){
        return;
    }

}