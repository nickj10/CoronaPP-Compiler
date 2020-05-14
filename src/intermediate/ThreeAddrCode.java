package intermediate;

import SymbolTable.Symbol;

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

    @Override
    public String toString() {
        return "TAC: " +
                "arg1='" + arg1.getLexema() + '\'' +
                ", arg2='" + arg2.getLexema() + '\'' +
                ", op='" + op.getLexema() + '\'' +
                ", result=" + result;
    }
}
