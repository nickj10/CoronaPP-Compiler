package tester;

import exceptions.FirstAndFollowException;
import exceptions.GrammarException;
import exceptions.SemanticException;
import lexic_analysis.Scanner;
import model.CompilerManager;
import syntatic_analysis.Grammar;
import syntatic_analysis.Parser;

public class TestEnvironment {

    public static void main(String[] args) {
        if(args.length != 3) {
            System.out.println("Error: Wrong number of arguments.");
            return;
        }
        String sourceFile = args[0];
        String grammarFile = args[1];
        String dictionaryFile = args[2];
        // Dividing code into steps
        // Scanner:
        Scanner scanner;
        System.out.println("Building scanner");
        try {
            scanner = new Scanner(sourceFile);
            System.out.println("Done!\n\n");
        }catch(Exception e){
            System.out.println("Failed!");
            System.out.println("ERROR:" + e.getMessage());
            return;
        }

        // Parser:
        Parser parser;
        System.out.println("Building parser");
        try {
            parser = new Parser(grammarFile, dictionaryFile);
            System.out.println("Done!\n\n");
        }catch(Exception e){
            System.out.println("Failed!");
            System.out.println("ERROR:" + e.getMessage());
            return;
        }

        // Compilation:
        CompilerManager compilerManager = new CompilerManager();
        CompilerManager.setScanner(scanner);
        CompilerManager.setParser(parser);
        System.out.println("Compiling source code...");
        try {
            compilerManager.compile();
            System.out.println("Compiled!\n\n");
        } catch (FirstAndFollowException | GrammarException | SemanticException e) {
            System.out.println("Compilation Failed!");
            e.printStackTrace();
            return;
        }

        // Printing symbol table result:
        System.out.println("Symbol Table:");
        System.out.println(compilerManager.getSymbolTable().toString());
    }}
