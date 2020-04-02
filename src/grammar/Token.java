package grammar;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Token {

	// Init Vars | Declare Language Alphabet & Token Patterns
	private static final Pattern VALID_TOKEN = Pattern.compile("^[a-zA-Z0-9_\\{\\}\\[\\]\\-\\|]*$");
	private static final Pattern ASSIGNMENT_TOKEN = Pattern.compile("^\\-$");
	private static final Pattern PIPE_TOKEN = Pattern.compile("^\\|$");
	private static final Pattern TERMINAL_TOKEN = Pattern.compile("^(NUMBER|IDENTIFIER|ARITMETIC_MULT|ARITMETIC_MOD|ARITMETIC_DIV|ARITMETIC_SUM|ARITMETIC_RES|ASSIGN_EQ|LOGIC_OR|LOGIC_AND|RELATIONAL_NOTEQ|RELATIONAL_EQ|IF|ELSE|WHILE|INT|BOOL|CONST|CHAR|NULL|RETURN|FLOAT|DOT_COMA|DOT|PARENT_OPEN|PARENT_CLOSED)$");
	private static final Pattern NONTERMINAL_TOKEN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]*$");
	public String token;
	public String type;
	public String message;

	// Token constructor
	public Token(String tok){
		token = tok;
		validate();
		if (type != "ERROR") {
			type();
		}
	}

	// Sets error and erroemessage if token is not valid
	protected void validate() {
		if (!VALID_TOKEN.matcher(token).matches()){
			type = "ERROR";
			message = "ERROR CODE 0: Input not according to format";
		}
	}

	// Type the given token
	protected void type() {
		if (ASSIGNMENT_TOKEN.matcher(token).matches()){
			type = "ASSIGNMENT";
			message = "assignment";
		}else if (PIPE_TOKEN.matcher(token).matches()){
			type = "PIPE";
			message = "pipe";
		}else if (TERMINAL_TOKEN.matcher(token).matches()){
			type = "TERMINAL";
			message = "terminal";
		}else if (NONTERMINAL_TOKEN.matcher(token).matches()){
			type = "NONTERMINAL";
			message = "nonterminal";
		}else{
			type = "ERROR";
			message = "ERROR CODE 0: Input not according to format";
		}
	}

	// Print Oneline View
	public void oneline() {
		System.out.printf("%1$-15s\t%2$-1s\n",token,message);
	}

	// Print all vars for token
	public void debug() {
		System.out.println("Token: "+token);
		System.out.println("Type: "+type);
		System.out.println("Message: "+message);
	}
}