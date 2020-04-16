import exceptions.FirstAndFollowException;
import exceptions.GrammarException;
import exceptions.SemanticException;
import model.CompilerManager;

public class Main {
    public static void main(String[] args) {
        String sourceFile = args[0];
        String grammarFile = args[1];
        String dictionaryFile = args[2];

        CompilerManager compilerManager = new CompilerManager(sourceFile, grammarFile, dictionaryFile);
        try {
            compilerManager.compile();
        } catch (FirstAndFollowException | GrammarException | SemanticException e) {
            e.printStackTrace();
        }
    }
}
