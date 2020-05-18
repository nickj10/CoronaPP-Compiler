package tester;

import SymbolTable.SymbolTable;
import code_generator.CodeGenerator;

public class CodeGeneratorTest {


    public static void main(String[] args) {
        /* Trying to generate the following code snippet in MIPS:
            int a;
            a = 0;
            int b;
            b = 2;
            int c;
            c = a + b;
         */
        // Testing code generation in mips
        CodeGenerator codeGenerator = new CodeGenerator();
        // We will need a symbol table, so we create one with some variables to proceed
        SymbolTable symbolTable = SymbolTable.getInstance();


    }


}
