package code_generator;

import SymbolTable.Symbol;
import SymbolTable.Table;

public class MemManager {
    private int framePointer; // fp in MIPS
    private int stackPointer; // sp in MIPS

    public MemManager() {
        this.framePointer = 0;
        this.stackPointer = 0;
    }

    public int pushVar(Symbol symbol){
        int currSP = this.stackPointer;
        symbol.setStackPointer(currSP);
        currSP -= symbol.getDataSize();
        this.stackPointer = currSP;

        return currSP;
    }

    public int pushBlock(Table table){
        // TODO: Push the value of the current block (scope) in framePointer and return the result
        int currFP = this.stackPointer;
        return currFP;
    }

    public int getFramePointer() {
        return framePointer;
    }

    public void setFramePointer(int framePointer) {
        this.framePointer = framePointer;
    }

    public int getStackPointer() {
        return stackPointer;
    }

    public void setStackPointer(int stackPointer) {
        this.stackPointer = stackPointer;
    }
}
