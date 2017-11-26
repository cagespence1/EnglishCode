import Model.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.ArrayList;

public class IdentifierVisitor extends grammarLangBaseVisitor {
	
	/* enter program */
	
	ParseTreeProperty<Scope> scopes = new ParseTreeProperty<>();
	GlobalScope globalScope;
	Scope currentScope;
	Boolean success = true;
	
	@Override
	public Void visitProgram(grammarLangParser.ProgramContext ctx) {
		globalScope = new GlobalScope(null);
		currentScope = globalScope;
		super.visitProgram(ctx);
		return null;
	}
	
	@Override
	public Void visitFunction_define(grammarLangParser.Function_defineContext ctx) {
		String functionIdentifier = ctx.ID().getText();
		if (currentScope.lookup(functionIdentifier) == null) {
			int returnToken = ctx.type().start.getType();
			Symbol.Type returnType = Main.getType(returnToken);
			// push new scope
			FunctionSymbol function = new FunctionSymbol(functionIdentifier, returnType, currentScope);
			
			currentScope.define(function);
			saveScope(ctx, function);
			currentScope = function;
			if (ctx.parameters() != null) {
				ArrayList<grammarLangParser.ParameterContext> parameters = new ArrayList<>(ctx.parameters().parameter());
				for (int i = 0; i < parameters.size(); i++) {
					visitParameter(parameters.get(i));
				}
			}
			visitBlock(ctx.block());
			// close parameter scope
			currentScope = currentScope.getParentScope();
		} else {
			Main.error(ctx.getStart(), "Cannot declare function more than once within the scope");
		}
		return null;
	}
	
	@Override
	public Object visitParameter(grammarLangParser.ParameterContext ctx) {
		defineVariable(ctx.type(), ctx.ID().getSymbol());
		return null;
	}
	
	@Override
	public Object visitVariable_define(grammarLangParser.Variable_defineContext ctx) {
		if (currentScope.lookup(ctx.ID().getText()) == null) {
		
//			System.out.println(ctx.ID().getText() + " = " + visitValue(ctx.value()));
			
			defineVariable(ctx.type(), ctx.ID().getSymbol(), visitValue(ctx.value()));
		} else {
			success = false;
			Main.error(ctx.getStart(), "Cannot declare variable more than once within the scope");
		}
		return null;
	}
	
	@Override
	public Object visitValue(grammarLangParser.ValueContext ctx) {
		ctx.variable();
		ctx.func();
//		System.out.println("	literal val " + visitLiteral(ctx.literal()));
		return super.visitValue(ctx);
	}
	
	@Override
	public String visitLiteral(grammarLangParser.LiteralContext ctx) {
//		System.out.println(ctx.bool());
		if (ctx.bool() != null){
			if (ctx.bool().TRUE() != null){
				return "true";
			} else return "false";
		} else if (ctx.integer() != null){
			return ctx.integer().INT().getText();
		} else return ctx.string().STRING().getText();
//		return "50";
	}
	
	@Override
	public Object visitBlock(grammarLangParser.BlockContext ctx) {
		// open new scope for block
		currentScope = new LocalScope(currentScope);
		saveScope(ctx, currentScope);
		
		// visit statements
		ArrayList<grammarLangParser.StatContext> statements = new ArrayList<>(ctx.stat());
		for (int i = 0; i < statements.size(); i++) {
			visitStat(statements.get(i));
		}
		
		//close scope
		currentScope = currentScope.getParentScope();
		return null;
	}
	
	@Override
	public Object visitIfbody(grammarLangParser.IfbodyContext ctx) {
		currentScope = new LocalScope(currentScope, "IfBody");
		saveScope(ctx.condition(), currentScope);
		visit(ctx.condition());
		currentScope = currentScope.getParentScope();
		visit(ctx.block());
		for (int i = 0; i < ctx.stat().size(); i++) {
			visit(ctx.stat(i));
		}
		return null;
	}
	
	void saveScope(ParserRuleContext ctx, Scope s) {
		scopes.put(ctx, s);
	}
	
	void defineVariable(grammarLangParser.TypeContext typeCtx, Token token) {
		int tokenType = typeCtx.start.getType();
		Symbol.Type variableType = Main.getType(tokenType);
		VariableSymbol variable = new VariableSymbol(token.getText(), variableType);
		currentScope.define(variable);
	}
	
	void defineVariable(grammarLangParser.TypeContext typeCtx, Token token, Object value ) {
		int tokenType = typeCtx.start.getType();
		Symbol.Type variableType = Main.getType(tokenType);
		VariableSymbol variable = new VariableSymbol(token.getText(), variableType);
		variable.setInitialValue(value);
		currentScope.define(variable);
	}
	
//	void defineFunction(grammarLangParser.TypeContext typeCtx, Token token) {
//		int tokenType = typeCtx.start.getType();
//		Symbol.Type variableType = Main.getType(tokenType);
//		VariableSymbol variable = new VariableSymbol(token.getText(), variableType);
//		currentScope.define(variable);
//	}
}