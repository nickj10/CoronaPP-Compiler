package tester;

import SymbolTable.SymbolTable;
import code_generator.CodeGenerator;
import code_generator.ExporterToFile;

import java.util.ArrayList;

public class GenerateASMTest {
    public static void main(String[] args) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("hola");
        strings.add("que tal");
        strings.add("ADD t1 t2 t3");
        strings.add("ADD SUB , 23");

        ExporterToFile.generateFile("Probando", strings);
    }

}
