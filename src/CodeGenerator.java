import Model.*;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CodeGenerator extends grammarLangBaseVisitor {
	int labelCount;
	GlobalScope globalScope;
	ParseTreeProperty<Scope> scopes;
	FunctionSymbol currentFunction = null;
	static int currentLocalCount = 0;
	Scope currentScope;
	
	public CodeGenerator(GlobalScope globalScope, ParseTreeProperty<Scope> scopes) {
		this.globalScope = globalScope;
		this.scopes = scopes;
	}
	
	//todo check if this works
	@Override
	public ArrayList<String> visitProgram(grammarLangParser.ProgramContext ctx) {
		
		currentScope = globalScope;
		
		ArrayList<String> code = new ArrayList<>();
		code.add(".class public Main");
		code.add(".super java/lang/Object");
		code.add(".method public static <init>()V");
		code.add("aload_0");
		code.add("invokenonvirtual java/lang/Object/<init>()V");
		code.add("return");
		code.add(".end method");

//		code.add(".method public static main([Ljava/lang/String;)V");
//		code.add(".limit stack 100");
//		code.add(".limit locals 100");
		
		//add methods here
		
		ArrayList<grammarLangParser.StatContext> statements = (ArrayList<grammarLangParser.StatContext>) ctx.stat();
		for (int i = 0; i < statements.size(); i++) {
			code.addAll(visitStat(statements.get(i)));
		}

//		code.add("return");
//		code.add(".end method");
		
		for (int i = 0; i < code.size(); i++) {
			System.out.println("	" + code.get(i));
		}
		
		return code;
	}
	
	;
	
	@Override
	public ArrayList<String> visitStat(grammarLangParser.StatContext ctx) {
		ArrayList<String> code = new ArrayList<>();
		
		code.addAll((Collection<? extends String>) super.visitStat(ctx));
		
		return code;
	}
	
	/**
	 * Adds a conditional if-then statement to the stack
	 *
	 * @return code stack
	 */
	@Override
	public ArrayList<String> visitIfstat(grammarLangParser.IfstatContext ctx) {
		ArrayList<String> code = new ArrayList<>();
		
		//increase label count for this if statement
		int count = ++labelCount;
		
		visit(ctx.ifbody());
		code.addAll((ArrayList<String>) visit(ctx.ifbody().condition()));
		
		//then label
		code.add("then" + count + ":");
		
		code.addAll(visitBlock(ctx.ifbody().block()));
		
		//endif label
		code.add("endif_" + count + ":");
		return code;
	}
	
	// todo test
	
	/**
	 * Adds a conditional statement within
	 *
	 * @param ctx
	 * @return
	 */
	@Override
	public ArrayList<String> visitConditionCompare(grammarLangParser.ConditionCompareContext ctx) {
		ArrayList<String> code = new ArrayList<>();
		
		Symbol.Type comparisonType = getValueType(ctx.leftValue);
		String mnemonic = Main.getMnemonic(comparisonType);
		String compareStatement = "if_" + mnemonic + "cmp";
		
		switch (ctx.COMPARATOR().getText()) {
			case "is equal to":
				compareStatement += "eq";
				break;
			case "is more than":
				compareStatement += "gt";
				break;
			case "is less than":
				compareStatement += "lt";
				break;
			case "is more than equal to":
				compareStatement += "le";
				break;
			case "is less than equal to":
				compareStatement += "ge";
				break;
		}
		
		compareStatement += " then" + labelCount + ":";
		
		code.add(loadConstantFromValue(ctx.leftValue));
		code.add(loadConstantFromValue(ctx.rightValue));
		code.add(compareStatement);
		
		code.add("goto endif_" + labelCount);
		
		return code;
	}
	
	// todo
	@Override
	public ArrayList<String> visitConditionLogExp(grammarLangParser.ConditionLogExpContext ctx) {
		return null;
	}
	
	private Symbol.Type getValueType(grammarLangParser.ValueContext ctx) {
		int type = 0;
		if (ctx.literal() != null) {
			if (ctx.literal().bool() != null) {
				type = ctx.literal().bool().getStart().getType();
			} else if (ctx.literal().integer() != null) {
				type = ctx.literal().integer().getStart().getType();
			} else if (ctx.literal().string() != null) {
				type = ctx.literal().string().getStart().getType();
			}
		} else if (ctx.variable() != null) {
			return scopes
					.get(ctx.getParent())
					.lookup(ctx.variable().ID().getText())
					.getType();
		} else if (ctx.func() != null) {
			return scopes
					.get(ctx.getParent())
					.lookup(ctx.func().ID().getText())
					.getType();
		}
		
		return Main.getType(type);
	}
	
	/**
	 * Loads a literal value onto the stack
	 * <p>
	 * If the value is a variable, it gets the variables literal value
	 * If the value is a function, it runs the function, and gets the return value
	 *
	 * @param ctx
	 * @return
	 */
	@Override
	@Deprecated
	public String visitValue(grammarLangParser.ValueContext ctx) {

//		ArrayList<String> code = new ArrayList<>();
		
		String code = "";
		
		if (ctx.variable() != null) {
//			code = visitVariable(ctx.variable());
		} else if (ctx.func() != null) {
//			code.addAll(visitFunc(ctx.func()));
		} else if (ctx.literal() != null) {
			code = visitLiteral(ctx.literal());
		}
		
		return code;
	}
	
	//todo test this
	@Override
	@Deprecated
	public String visitLiteral(grammarLangParser.LiteralContext ctx) {
		
		String value = "";

//		if (ctx.string() != null) {
//			value += ctx.string().STRING().getText();
//		} else if (ctx.integer() != null) {
//			value += ctx.integer().INT().getText();
//		} else if (ctx.bool() != null) {
//			if (ctx.bool().FALSE() != null) {
//				value += ctx.bool().FALSE().getText();
//			} else {
//				value += ctx.bool().TRUE().getText();
//			}
//		}
		
		return value;
	}
	
	//todo test this
	@Override
	@Deprecated
	public ArrayList<String> visitVariable(grammarLangParser.VariableContext ctx) {
		ArrayList<String> code = new ArrayList<>();
		
		VariableSymbol variable = (VariableSymbol) currentScope.lookup(ctx.ID().getText());
		
		// Check whether this is in global scope or function scope
		int identifier = variable.getVariableStoreIndex();
		Symbol.Type type = variable.getType();
		String mnemonic = Main.getMnemonic(type);
		
		if (isGlobalScope()) {
			code.add(mnemonic + "load " + identifier);
		}
		
		return code;
	}
	
	//todo
	@Override
	public ArrayList<String> visitForstat(grammarLangParser.ForstatContext ctx) {
		return null;
	}
	
	//todo
	@Override
	public ArrayList<String> visitWhilestat(grammarLangParser.WhilestatContext ctx) {
		return null;
	}
	
	//todo
	@Override
	public ArrayList<String> visitVariable_define(grammarLangParser.Variable_defineContext ctx) {
		ArrayList<String> code = new ArrayList<>();
		
		// Check whether the variable is being define inside a method or in the global scope
		// global scope: declare as field
		
		// inside method: as local
		// if local, get number of parameters, and start the identifier # after that
		
		VariableSymbol variable = (VariableSymbol) currentScope.lookup(ctx.ID().getText());

//		Symbol.Type type = variable.getType();
//		String mnemonic = Main.getMnemonic(type);
//		String descriptor = Main.getDescriptor(type);
		
		// Global
		if (isGlobalScope()) {
			code.add(defineStaticVariable(variable));
		} else {
			// Local
			code.add(loadConstantFromValue(ctx.value()));
			code.add(defineLocalVariable(variable));
		}
		
		return code;
	}

//	String setStaticVariable(VariableSymbol variable){
//
//	}
	
	String defineStaticVariable(VariableSymbol variable) {
		variable.setGlobalVariable(true);
		Symbol.Type type = variable.getType();
		String name = variable.getName();
		String descriptor = Main.getDescriptor(type);
		return ".field public static " + name + " " + descriptor + " = " + variable.getInitialValue();
	}
	
	String defineLocalVariable(VariableSymbol variable) {
		Symbol.Type type = variable.getType();
		String mnemonic = Main.getMnemonic(type);
		int parameterCount = currentFunction.getParameters().size();
		int identifier = ++currentLocalCount + parameterCount;
		return mnemonic + "store " + identifier;
	}
	
	//todo test
	@Override
	public ArrayList<String> visitFunction_define(grammarLangParser.Function_defineContext ctx) {
		
//		currentScope = scopes.get(ctx);
		currentFunction = (FunctionSymbol) currentScope.lookup(ctx.ID().getText());
		currentScope = currentFunction;
		currentLocalCount = 0;
		
		ArrayList<String> code = new ArrayList<>();
		
		String function = ".method public " + ctx.ID().getText() + "(";
		
		List<String> parameters = visitParameters(ctx.parameters());
		
		for (int i = 0; i < parameters.size(); i++) {
			function += parameters.get(i);
		}
		
		String returnTypeDescriptor = Main.getDescriptor(Main.getType(ctx.type().getStart().getType()));
		
		function += ")" + returnTypeDescriptor;
		
		code.add(function);
		
		//todo calculate this
		code.add(".limit stack 100");
		code.add(".limit locals 100");
		
		//todo
		code.addAll(visitBlock(ctx.block()));
		
		code.add(".end method");
		
		currentFunction = null;
		
		currentScope = currentScope.getParentScope();
		
		return code;
	}
	
	//todo
	@Override
	public ArrayList<String> visitAssign(grammarLangParser.AssignContext ctx) {
		return null;
	}
	
	//todo
	@Override
	public ArrayList<String> visitIncrement(grammarLangParser.IncrementContext ctx) {
		return null;
	}
	
	//todo test this
	@Override
	public ArrayList<String> visitReturn_statement(grammarLangParser.Return_statementContext ctx) {
		ArrayList<String> code = new ArrayList<>();
		code.add(loadConstantFromValue(ctx.value()));
		code.add("return");
		return code;
	}
	
	//todo
	@Override
	public ArrayList<String> visitBool(grammarLangParser.BoolContext ctx) {
		return null;
	}
	
	//todo
	@Override
	public ArrayList<String> visitInteger(grammarLangParser.IntegerContext ctx) {
		return null;
	}
	
	//todo
	@Override
	public ArrayList<String> visitString(grammarLangParser.StringContext ctx) {
		return null;
	}
	
	//todo check if this is working
	@Override
	public ArrayList<String> visitBlock(grammarLangParser.BlockContext ctx) {
		
		currentScope = scopes.get(ctx);
		
		ArrayList<String> code = new ArrayList<>();
		
		List<grammarLangParser.StatContext> statements = ctx.stat();
		for (int i = 0; i < statements.size(); i++) {
			code.addAll(visitStat(statements.get(i)));
		}
		
		currentScope = currentScope.getParentScope();
		
		return code;
	}
	
	//todo
	@Override
	public ArrayList<String> visitIfbody(grammarLangParser.IfbodyContext ctx) {
		
		currentScope = scopes.get(ctx);
		
		ArrayList<String> code = new ArrayList<>();
		
		// visit condition
		
		currentScope = currentScope.getParentScope();
		
		// visit block
		
		return code;
	}
	
	//todo
	@Override
	public ArrayList<String> visitConditionValue(grammarLangParser.ConditionValueContext ctx) {
		ArrayList<String> code = new ArrayList<>();
		super.visitConditionValue(ctx);
		return code;
	}
	
	//todo
	@Override
	public ArrayList<String> visitDefinition(grammarLangParser.DefinitionContext ctx) {
		ArrayList<String> code = new ArrayList<>();
		if (ctx.function_define() != null) {
			code.addAll(visitFunction_define(ctx.function_define()));
		} else {
			code.addAll(visitVariable_define(ctx.variable_define()));
		}
//		super.visitDefinition(ctx);
		return code;
	}
	
	//todo
	@Override
	public ArrayList<String> visitParameters(grammarLangParser.ParametersContext ctx) {
		ArrayList<String> code = new ArrayList<>();
		
		// get parameter types
		for (int i = 0; i < ctx.parameter().size(); i++) {
			code.addAll(visitParameter(ctx.parameter(i)));
		}

//		super.visitParameters(ctx);
		return code;
	}
	
	//todo
	@Override
	public ArrayList<String> visitParameter(grammarLangParser.ParameterContext ctx) {
		ArrayList<String> code = new ArrayList<>();
		//get parameter type descriptor
		
		String typeDescriptor = Main.getDescriptor(Main.getType(ctx.start.getType()));
//		System.out.println(typeDescriptor);
		code.add(typeDescriptor);

//		super.visitParameter(ctx);
		return code;
	}
	
	//todo
	@Override
	public ArrayList<String> visitFunc(grammarLangParser.FuncContext ctx) {
		ArrayList<String> code = new ArrayList<>();
		super.visitFunc(ctx);
		return code;
	}
	
	//todo
	@Override
	public ArrayList<String> visitArguments(grammarLangParser.ArgumentsContext ctx) {
		ArrayList<String> code = new ArrayList<>();
		super.visitArguments(ctx);
		return code;
	}
	
	//todo
	@Override
	public ArrayList<String> visitMathExpParenthesis(grammarLangParser.MathExpParenthesisContext ctx) {
		ArrayList<String> code = new ArrayList<>();
		super.visitMathExpParenthesis(ctx);
		return code;
	}
	
	//todo
	@Override
	public ArrayList<String> visitMathExpRegular(grammarLangParser.MathExpRegularContext ctx) {
		ArrayList<String> code = new ArrayList<>();
		super.visitMathExpRegular(ctx);
		return code;
	}
	
	//todo
	@Override
	public ArrayList<String> visitMathExpSingleValue(grammarLangParser.MathExpSingleValueContext ctx) {
		ArrayList<String> code = new ArrayList<>();
		super.visitMathExpSingleValue(ctx);
		return code;
	}
	
	//todo
	@Override
	public ArrayList<String> visitType(grammarLangParser.TypeContext ctx) {
		ArrayList<String> code = new ArrayList<>();
		super.visitType(ctx);
		return code;
	}
	
	//todo
	@Override
	public ArrayList<String> visitPrint(grammarLangParser.PrintContext ctx) {
		ArrayList<String> code = new ArrayList<>();
		super.visitPrint(ctx);
		return code;
	}
	
	//todo
	@Override
	public ArrayList<String> visitLog_ex(grammarLangParser.Log_exContext ctx) {
		ArrayList<String> code = new ArrayList<>();
		super.visitLog_ex(ctx);
		return code;
	}
	
	String loadConstantFromValue(grammarLangParser.ValueContext ctx) {
		if (ctx.variable() != null) {
			return loadVariable(ctx.variable());
		} else if (ctx.func() != null) {
			// todo run static method
		} else if (ctx.literal() != null) {
			return loadLiteral(ctx.literal());
		}
		return "";
	}
	
	String loadLiteral(grammarLangParser.LiteralContext ctx) {
		return getLiteralValue(ctx);
	}
	
	String getLiteralValue(grammarLangParser.LiteralContext ctx) {
		String value = "";
		
		if (ctx.string() != null) {
			value += ctx.string().STRING().getText();
		} else if (ctx.integer() != null) {
			value += ctx.integer().INT().getText();
		} else if (ctx.bool() != null) {
			if (ctx.bool().FALSE() != null) {
				value += ctx.bool().FALSE().getText();
			} else {
				value += ctx.bool().TRUE().getText();
			}
		}
		
		return value;
	}
	
	String loadVariable(grammarLangParser.VariableContext ctx) {
		String variableID = ctx.ID().getText();
		VariableSymbol variable = (VariableSymbol) currentScope.lookup(variableID);
		
//		System.out.println(currentScope.getScopeName());
//		System.out.println(currentScope == currentFunction);
//		if (currentScope.getScopeName().equals("function")){
//			System.out.println("function scope");
//		}
		
		if (variable.isGlobalVariable()) {
			return loadVariableFromStatic(variable);
		} else {
			return loadVariableFromLocals(variable);
		}
	}
	
	String loadVariableFromLocals(VariableSymbol variable) {
		//check if the variable comes from the function
		if (currentFunction != null){
			ArrayList<Symbol> variables = currentFunction.getParameters();
			
			for (int i = 0; i < variables.size(); i++) {
				if (variable.getName().equals(variables.get(i).getName())){
					variable.setVariableStoreIndex(i + 1);
				}
			}
		}
		
		String mnemonic = Main.getMnemonic(variable.getType());
		int localIdentifier = variable.getVariableStoreIndex();
		return mnemonic + "load " + localIdentifier;
	}
	
	String loadVariableFromStatic(VariableSymbol variable) {
		String variableName = variable.getName();
		String descriptor = Main.getDescriptor(variable.getType());
		return "getstatic Main/" + variableName + " " + descriptor;
	}
	
	boolean isGlobalScope() {
		return currentScope == globalScope;
	}
}