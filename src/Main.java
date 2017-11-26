import Model.Symbol;
import com.sun.corba.se.impl.io.TypeMismatchException;
import Model.VariableSymbol;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static Model.Symbol.Type.INTEGER;

public class Main {
	
	static boolean success = true;
	
	public static void main(String[] args) {
		try {
			String file = new String(Files.readAllBytes(Paths.get("method")));
			evaluate(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Integer evaluate(String filename) throws IOException {
		System.out.println("Compiling EnglishCode");
		// Create input stream
		org.antlr.v4.runtime.ANTLRInputStream inputStream = new org.antlr.v4.runtime.ANTLRInputStream(filename);
		
		// Create lexer, run scanner to create stream of tokens
		grammarLangLexer lexer = new grammarLangLexer(inputStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		
		// Create parser and pass tokens
		grammarLangParser parser = new grammarLangParser(tokens);
		ParseTree program = parser.program(); // begin parsing at expression rule
		
		// Identifying phase
		System.out.println("> Identifying variables and scopes");
		IdentifierVisitor typeIdentifier = new IdentifierVisitor();
		typeIdentifier.visit(program);
		
		if (success) {
			// Type checking phase
			System.out.println("> Checking types");
			TypeCheckerVisitor typeChecker = new TypeCheckerVisitor(typeIdentifier.globalScope, typeIdentifier.scopes);
			typeChecker.visit(program);
			
			if (success) {
				// Code generation phase
				System.out.println("> Generating bytecode");
				CodeGenerator generator = new CodeGenerator(typeIdentifier.globalScope, typeIdentifier.scopes);
				generator.visit(program);
			}
		}
		
		if (!success){
			System.out.println("! Compilation aborted");
		} else {
			System.out.println("! Compilation success");
		}
		return null;
	}
	
	public static Symbol.Type getType(int tokenType) {
		switch (tokenType) {
			case grammarLangParser.TYPE_INT:
				return INTEGER;
			case grammarLangParser.TYPE_BOOL:
				return Symbol.Type.BOOLEAN;
			case grammarLangParser.TYPE_STR:
				return Symbol.Type.STRING;
			case grammarLangParser.FALSE:
				return Symbol.Type.BOOLEAN;
			case grammarLangParser.TRUE:
				return Symbol.Type.BOOLEAN;
			case grammarLangParser.INT:
				return INTEGER;
			case grammarLangParser.STRING:
				return Symbol.Type.STRING;
		}
		return null;
	}
	
	public static String getDescriptor(Symbol.Type type){
		switch (type){
			case INTEGER:
				return "I";
			case STRING:
				return "Ljava/lang/String;";
			case BOOLEAN:
				return "Z";
			default:
				return null;
		}
	}
	
	public static String getMnemonic(Symbol.Type type){
		switch (type){
			case INTEGER:
				return "i";
			case BOOLEAN:
				return "i";
			case STRING:
				return "a";
			default:
				return "";
		}
	}
	
	public static void error(Token t, String message) {
		success = false;
		System.out.println("	  Error at "
				+ t.getLine()
				+ ":"
				+ t.getCharPositionInLine()
				+ " "
				+ message);
	}
}
