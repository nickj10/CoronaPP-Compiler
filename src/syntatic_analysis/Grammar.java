package syntatic_analysis;

import java.util.regex.Pattern;

public class Grammar {

    // Init Vars
    private static final Pattern BASIC_GRAMMAR = Pattern.compile("^((NONTERMINALASSIGNMENT|TERMINALASSIGNMENT)(NONTERMINAL|TERMINAL|PIPE)+)+$");
    private static final Pattern VALID_GRAMMAR = Pattern.compile("^(NONTERMINALASSIGNMENT(NONTERMINAL|TERMINAL|PIPE)+)+$");
    public String type;
    public String message;
    public Statement[] statements = new Statement[100];
    public Token[] nonTerminals = new Token[100];

    // Gramar constructor
    public Grammar(Token[] tokens) {
        // Validata Grammar before Parce
        validate(tokens);
        if (type == "ERROR") {
            // Do Not Parce Grammar
        } else {
            // Indexs set
            int statmentIndex = -1;
            int tokenIndex = 0;
            int definitionIndex = 0;
            int nonTerminalIndex = 0;
            // Create Statments from tokens array
            do {
                // If Nonterminal before ASSIGNMENT
                if (tokens[tokenIndex].type.equals("NONTERMINAL") && tokens[tokenIndex + 1].type.equals("ASSIGNMENT")) {
                    // Increment to next Segment Index
                    statmentIndex++;
                    // Init Statement
                    statements[statmentIndex] = new Statement();
                    // Assign NONTERMINAL
                    statements[statmentIndex].nonTerminal = tokens[tokenIndex];
                    // Reset Definition Index on creation of new statment
                    definitionIndex = 0;
                    // Add to nonTerminals Array
                    nonTerminals[nonTerminalIndex] = tokens[tokenIndex];
                    // Push past ASSIGNMENT
                    tokenIndex++;
                    // Increment to next nonTerminal Index
                    nonTerminalIndex++;
                }
                // ElseIf token is PIPE '|'
                else if (tokens[tokenIndex].type.equals("PIPE")) {
                    // Increment to next Segment Index
                    statmentIndex++;
                    // Init Statement
                    statements[statmentIndex] = new Statement();
                    // Assign NONTERMINAL
                    statements[statmentIndex].nonTerminal = statements[statmentIndex - 1].nonTerminal;
                    // Reset Definition Index on creation of new statment
                    definitionIndex = 0;
                }
                // Else is definition
                else {
                    // Push Token to Statement Definition
                    statements[statmentIndex].definitions[definitionIndex] = tokens[tokenIndex];
                    // Increment to next Definition Index
                    definitionIndex++;
                }
                // Increment to next Token Index
                tokenIndex++;
            } while (tokenIndex < tokens.length);

            nonTerminalCheck();
        }
    }

    // Sets error and erroemessage if grammar is not valid
    protected void validate(Token[] tokens) {
        String tokenString = "";
        for (Token token : tokens) {
            tokenString += token.type;
        }
        if (!BASIC_GRAMMAR.matcher(tokenString).matches()) {
            type = "ERROR";
            message = "ERROR CODE 0: Input not according to format";
            return;
        }
        if (!VALID_GRAMMAR.matcher(tokenString).matches()) {
            type = "ERROR";
            message = "ERROR CODE 2: terminal symbol appearing on the lefthand side of a rule";
            return;
        }
    }

    protected void nonTerminalCheck() {
        for (Statement statement : statements) {
            if (statement != null) {
                // If Devinition nonterminals in in nonTerminals array
                for (Token definition : statement.definitions) {
                    if (definition != null) {
                        // Is nonTerminal and is in array check
                        if (definition.type == "NONTERMINAL") {
                            if (inTokenArray(nonTerminals, definition) == -1) {
                                type = "ERROR";
                                message = "ERROR CODE 1: non-terminal symbol listed in a rule but does not have a description";
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    protected int inTokenArray(Token[] tokenArray, Token matchToken) {
        int index = 0;
        for (Token arrayToken : tokenArray) {
            if (arrayToken != null) {
                if (matchToken.token.equals(arrayToken.token)) {
                    return index;
                }
            }
            index++;
        }
        return -1;
    }

    public Token[] first(Token nonTerminal) {
        // Set Increment Indexes
        int setIndex = 0;
        // Init Set Token Array
        Token[] set = new Token[100];
        // ForEach Statement in grammar
        for (Statement statement : statements) {
            if (statement != null) {
                // If is passed nonTerminal
                if (statement.nonTerminal.token.equals(nonTerminal.token)) {
                    // If is TERMINAL
                    if (statement.definitions[0].type == "TERMINAL" && inTokenArray(set, statement.definitions[0]) == -1) {
                        set[setIndex] = statement.definitions[0];
                        setIndex++;
                    }
                    // If is NonTerminal
                    else if (statement.definitions[0].type == "NONTERMINAL") {
                        for (Token token : first(statement.definitions[0])) {
                            if (token != null && inTokenArray(set, token) == -1) {
                                set[setIndex] = token;
                                setIndex++;
                            }
                        }
                    }
                }
            }
        }
        return set;
    }

    public Token[] follow(Token nonTerminal) {
        // Set Increment Indexes
        int setIndex = 0;
        int definitionIndex = 0;
        int nonTerminalIndex = 0;
        // Init Set Token Array
        Token[] set = new Token[100];
        // ForEach Statement in grammar
        for (Statement statement : statements) {
            if (statement != null) {
                // ForEach Definition in Statement
                for (Token definition : statement.definitions) {
                    if (definition != null) {
                        // If is passed nonTerminal
                        if (definition.token.equals(nonTerminal.token)) {
                            // If +1 is TERMINAL
                            if (statement.definitions[definitionIndex + 1] != null && statement.definitions[definitionIndex + 1].type == "TERMINAL" && inTokenArray(set, statement.definitions[definitionIndex + 1]) == -1) {
                                set[setIndex] = statement.definitions[definitionIndex + 1];
                                setIndex++;
                            }
                            // If is NonTerminal
                            else if (statement.definitions[definitionIndex + 1] != null && statement.definitions[definitionIndex + 1].type == "NONTERMINAL") {
                                for (Token token : first(statement.definitions[definitionIndex + 1])) {
                                    if (token != null && inTokenArray(set, token) == -1) {
                                        set[setIndex] = token;
                                        setIndex++;
                                    }
                                }
                            }
                        }
                        // Increment definition Index
                        definitionIndex++;
                    }
                }
                // Reset definition Index
                definitionIndex = 0;
            }
        }
        // End of String
        if (set[0] == null) {
            set[0] = new Token("$");
        } else if (statements[0].nonTerminal.token.equals(nonTerminal.token)) {
            set[setIndex] = new Token("$");
        }
        return set;
    }

    // ToString
    public String toString() {
        String grammarString = "";
        grammarString += "NonTerminals: \n";
        for (Token nonTerminal : nonTerminals) {
            if (nonTerminal != null) {
                grammarString += nonTerminal.token + "\n";
            }
        }
        grammarString += "Grammar: \n";
        for (Statement statement : statements) {
            if (statement != null) {
                grammarString += statement.toString() + "\n";
            }
        }
        return grammarString;
    }

}