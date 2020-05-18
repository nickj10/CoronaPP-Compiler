package tester;

import exceptions.FirstAndFollowException;
import exceptions.GrammarException;
import exceptions.SemanticException;
import lexic_analysis.Scanner;
import model.CompilerManager;
import syntatic_analysis.Grammar;
import syntatic_analysis.Parser;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TestEnvironment {
    private final static Logger LOGGER = Logger.getLogger("tester");


    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Error: Wrong number of arguments.");
            return;
        }
        String sourceFile = args[0];
        String grammarFile = args[1];
        String dictionaryFile = args[2];

        // Dividing code into steps
        // Scanner:
        Scanner scanner;
        LOGGER.log(Level.INFO, "\n\n-------------------------------------\nBuilding scanner...\n-------------------------------------\n\n");
        try {
            scanner = new Scanner(sourceFile);
            LOGGER.log(Level.INFO, "Scanner built\n");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            return;
        }

        // Parser:
        Parser parser;
        LOGGER.log(Level.INFO, "\n\n-------------------------------------\nBuilding parser...\n-------------------------------------\n\n");
        try {
            parser = new Parser(grammarFile, dictionaryFile);
            LOGGER.log(Level.INFO, "Parser built\n");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            return;
        }

        // Compilation:
        CompilerManager compilerManager = new CompilerManager();
        CompilerManager.setScanner(scanner);
        CompilerManager.setParser(parser);

        LOGGER.log(Level.INFO, "\n\n-------------------------------------\nCompiling source code...\n-------------------------------------\n\n");
        try {
            compilerManager.compile();
            LOGGER.log(Level.INFO, "Compiled!\n");
        } catch (FirstAndFollowException | GrammarException | SemanticException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            return;
        }

        LOGGER.log(Level.INFO, "Symbol Table:\n" + compilerManager.getSymbolTable().toString());
    }
}
