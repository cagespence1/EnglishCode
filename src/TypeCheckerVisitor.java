import Model.*;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import java.util.ArrayList;
import java.util.List;

public class TypeCheckerVisitor extends grammarLangBaseVisitor {
	
	GlobalScope globalScope;
	ParseTreeProperty<Scope> scopes;
	Scope currentScope;
	
	public TypeCheckerVisitor(GlobalScope globalScope, ParseTreeProperty<Scope> scopes) {
		this.globalScope = globalScope;
		this.scopes = scopes;
	}
	
	@Override
	public Object visitProgram(grammarLangParser.ProgramContext ctx) {
		currentScope = globalScope;
		super.visitProgram(ctx);
		return null;
	}
	
	@Override
	public Object visitFunction_define(grammarLangParser.Function_defineContext ctx) {
		currentScope = scopes.get(ctx);
		grammarLangParser.ValueContext returnValue = null;
		
		Symbol.Type functionType = Main.getType(ctx.type().getStart().getType());
		Symbol.Type returnType;
		
		//check for return statement
		List<grammarLangParser.StatContext> statements = ctx.block().stat();
		boolean hasReturnStatement = false;
		currentScope = visitBlock(ctx.block());
		for (int i = 0; i < statements.size(); i++) {
			if (ctx.block().stat(i).return_statement() != null) {
				returnValue = ctx.block().stat(i).return_statement().value();
				returnType = visitValue(returnValue);
				hasReturnStatement = true;
				if (returnType != functionType && returnType != null) {
					Main.error(returnValue.getStart(), "Cannot return " + returnType + " from " + functionType + " function");
				}
				break;
			}
		}
		if (!hasReturnStatement) Main.error(ctx.getStart(), "Function definition must contain a return statement");
		currentScope = currentScope.getParentScope();
		return null;
	}
	
	@Override
	public Symbol.Type visitReturn_statement(grammarLangParser.Return_statementContext ctx) {
		return visitValue(ctx.value());
	}
	
	@Override
	public Object visitVariable_define(grammarLangParser.Variable_defineContext ctx) {
		// check that defined types match assigned types
		Symbol.Type variableType = Main.getType(ctx.type().getStart().getType());
		Symbol.Type assignedType = visitValue(ctx.value());
		if (checkVariableTypes(variableType, assignedType)){
//			System.out.println(scopes.get(ctx));
		} else {
			Main.error(ctx.value().getStart(), "Unable to assign " + assignedType + " to a " + variableType + " variable");
		}
		return null;
	}
	
	private boolean checkVariableTypes(Symbol.Type variableType, Symbol.Type assignedType) {
		return variableType == assignedType;
	}
	
	@Override
	public Scope visitBlock(grammarLangParser.BlockContext ctx) {
		currentScope = scopes.get(ctx);
		ArrayList<grammarLangParser.StatContext> statements = new ArrayList<>(ctx.stat());
		for (int i = 0; i < statements.size(); i++) {
			visitStat(statements.get(i));
		}
		currentScope = currentScope.getParentScope();
		return scopes.get(ctx);
	}
	
	@Override
	public Symbol.Type visitVariable(grammarLangParser.VariableContext ctx) {
		String name = ctx.ID().getText();
		Symbol variable = currentScope.lookup(name);
		if (variable == null) {
			Main.error(ctx.ID().getSymbol(), "Variable \"" + name + "\" not found");
		}
		if (variable instanceof FunctionSymbol) {
			Main.error(ctx.ID().getSymbol(), name + " is not a variable");
		}
		if (variable != null) {
			return variable.getType();
		}
		super.visitVariable(ctx);
		return null;
	}
	
	@Override
	public Object visitForstat(grammarLangParser.ForstatContext ctx) {

		if (visit(ctx.startValue) != Symbol.Type.INTEGER){
			Main.error(ctx.startValue.getStart(), "Start value in for-loop must be of type " + Symbol.Type.INTEGER);
		}
		
		if (visit(ctx.iterationsValue) != Symbol.Type.INTEGER){
			Main.error(ctx.iterationsValue.getStart(), "Iteration value in for-loop must be of type " + Symbol.Type.INTEGER);
		}
		
		return super.visitForstat(ctx);
	}
	
	@Override
	public Symbol.Type visitConditionValue(grammarLangParser.ConditionValueContext ctx) {
		Symbol.Type valueType = visitValue(ctx.value());
		if (valueType != Symbol.Type.BOOLEAN){
			Main.error(ctx.value().getStart(), "Condition value must be of type " + Symbol.Type.BOOLEAN);
		}
		return visitValue(ctx.value());
	}
	
	@Override
	public Void visitConditionCompare(grammarLangParser.ConditionCompareContext ctx) {
		String comparator = ctx.COMPARATOR().getSymbol().getText();
		if (comparator.equals("is equal to")) {
			Symbol.Type leftValue = visitValue(ctx.leftValue);
			Symbol.Type rightValue = visitValue(ctx.rightValue);
			if (leftValue != rightValue) {
				Main.error(ctx.getStart(), "Values in equal-to condition must be the same type on both sides");
			}
		} else {
			if (visit(ctx.leftValue) != Symbol.Type.INTEGER || visit(ctx.rightValue) != Symbol.Type.INTEGER) {
				Main.error(ctx.getStart(), "Values in less-than and more-than conditions must be of type " + Symbol.Type.INTEGER);
			}
		}
		return null;
	}
	
	//todo
	@Override
	public Object visitConditionLogExp(grammarLangParser.ConditionLogExpContext ctx) {
		return super.visitConditionLogExp(ctx);
	}
	
	@Override
	public Symbol.Type visitValue(grammarLangParser.ValueContext ctx) {
		if (ctx.variable() != null) {
			return visitVariable(ctx.variable());
		} else if (ctx.func() != null) {
			return visitFunc(ctx.func());
		} else if (ctx.literal() != null) {
			return visitLiteral(ctx.literal());
		}
		return null;
	}
	
	@Override
	public Symbol.Type visitLiteral(grammarLangParser.LiteralContext ctx) {
		return Main.getType(ctx.getStart().getType());
	}
	
	@Override
	public Symbol.Type visitFunc(grammarLangParser.FuncContext ctx) {
		String name = ctx.ID().getText();
		Symbol function = currentScope.lookup(name);
		if (function == null) {
			Main.error(ctx.ID().getSymbol(), "Function " + ctx.ID().getText() + " not found");
		}
		if (function instanceof VariableSymbol) {
			Main.error(ctx.ID().getSymbol(), name + " is not a function");
		}
		
		// get list of argument types
		ArrayList<Symbol.Type> argumentTypes = visitArguments(ctx.arguments());
		ArrayList<Symbol.Type> parameterTypes = new ArrayList<>();
		FunctionSymbol sym = (FunctionSymbol) function;
		ArrayList<Symbol> parameters = sym.getParameters();
		
		for (int i = 0; i < parameters.size(); i++) {
			parameterTypes.add(parameters.get(i).getType());
		}
		
		if (argumentTypes == null) {
			argumentTypes = new ArrayList<>();
		}
		
		//check parameter vs arguments
		if (parameterTypes.size() != argumentTypes.size()) {
			Main.error(ctx.arguments().getStart(), "Wrong number of arguments (" + argumentTypes.size() + ") in function call, expecting (" + parameterTypes.size() + ")");
		} else {
			for (int i = 0; i < argumentTypes.size(); i++) {
				if (!argumentTypes.get(i).equals(parameterTypes.get(i))) {
					Main.error(ctx.arguments().value(i).getStart(), "Argument [" + i + "] type " + argumentTypes.get(i) + " does not match parameter type " + parameters.get(i).getType());
				}
			}
		}
		
		return function.getType();
	}
	
	@Override
	public ArrayList<Symbol.Type> visitArguments(grammarLangParser.ArgumentsContext ctx) {
		try {
			if (ctx.value() != null) {
				ArrayList<grammarLangParser.ValueContext> arguments = (ArrayList) ctx.value();
				ArrayList<Symbol.Type> argumentTypes = new ArrayList<>();
				for (int i = 0; i < arguments.size(); i++) {
					argumentTypes.add(visitValue(arguments.get(i)));
				}
				return argumentTypes;
			}
		} catch (NullPointerException nx) {
		}
		return null;
	}
	
	@Override
	public Object visitIncrement(grammarLangParser.IncrementContext ctx) {
		super.visitIncrement(ctx);
		Symbol.Type variabletype = visitVariable(ctx.variable());
		if (variabletype != Symbol.Type.INTEGER){
			Main.error(ctx.getStart(), "Increment can only be performed on " + Symbol.Type.INTEGER + " variables");
		}
		return null;
	}
	
	@Override
	public ArrayList<Symbol.Type> visitParameters(grammarLangParser.ParametersContext ctx) {
		ArrayList<grammarLangParser.ParameterContext> parameters = (ArrayList) ctx.parameter();
		ArrayList<Symbol.Type> parameterTypes = new ArrayList<>();
		for (int i = 0; i < parameters.size(); i++) {
			parameterTypes.add(Main.getType(parameters.get(i).type().start.getType()));
		}
		return parameterTypes;
	}
	
	@Override
	public Object visitAssign(grammarLangParser.AssignContext ctx) {
		Symbol variable = currentScope.lookup(ctx.variable().ID().getText());
		Symbol.Type variableType = variable.getType();
		Symbol.Type assignedType = visitValue(ctx.value());
		
		checkVariableTypes(variableType, assignedType);
//		if (ctx.value().func() != null) {
//			Symbol.Type assignedType = Main.getType(ctx.value().func().getStart().getType());
//			checkVariableTypes(ctx.value().getStart(), variableType, assignedType);
//		} else if (ctx.value().variable() != null) {
//			String name = ctx.value().variable().ID().getSymbol().getText();
//			System.out.println(name);
//			Symbol assignedVariable = currentScope.lookup(name);
//			Symbol.Type assignedType = assignedVariable.getType();
//			checkVariableTypes(ctx.value().getStart(), variableType, assignedType);
//		} else if (ctx.value().literal() != null) {
//			Symbol.Type assignedType = Main.getType(ctx.value().literal().getStart().getType());
//			checkVariableTypes(ctx.value().getStart(), variableType, assignedType);
//		}
		return null;
	}
	
	@Override
	public Object visitMathExpSingleValue(grammarLangParser.MathExpSingleValueContext ctx) {
		super.visitMathExpSingleValue(ctx);
		Symbol.Type type = (Symbol.Type) visit(ctx.value());
		if (type != Symbol.Type.INTEGER) {
			Main.error(ctx.value().getStart(), "Value in math expression must be of type " + Symbol.Type.INTEGER);
		}
		return null;
	}
}