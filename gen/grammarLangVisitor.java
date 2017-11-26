// Generated from C:/Users/Cage/School/Year2/Quarter3/CompilersOperatingSystems/Assignments/Language/src\grammarLang.g4 by ANTLR 4.7
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link grammarLangParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface grammarLangVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(grammarLangParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStat(grammarLangParser.StatContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#ifstat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfstat(grammarLangParser.IfstatContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#ifbody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfbody(grammarLangParser.IfbodyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code conditionValue}
	 * labeled alternative in {@link grammarLangParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionValue(grammarLangParser.ConditionValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code conditionCompare}
	 * labeled alternative in {@link grammarLangParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionCompare(grammarLangParser.ConditionCompareContext ctx);
	/**
	 * Visit a parse tree produced by the {@code conditionLogExp}
	 * labeled alternative in {@link grammarLangParser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionLogExp(grammarLangParser.ConditionLogExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(grammarLangParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#forstat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForstat(grammarLangParser.ForstatContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#whilestat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhilestat(grammarLangParser.WhilestatContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefinition(grammarLangParser.DefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#variable_define}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable_define(grammarLangParser.Variable_defineContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#function_define}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction_define(grammarLangParser.Function_defineContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#parameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameters(grammarLangParser.ParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#parameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParameter(grammarLangParser.ParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunc(grammarLangParser.FuncContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(grammarLangParser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#assign}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign(grammarLangParser.AssignContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#increment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncrement(grammarLangParser.IncrementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mathExpParenthesis}
	 * labeled alternative in {@link grammarLangParser#math_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMathExpParenthesis(grammarLangParser.MathExpParenthesisContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mathExpRegular}
	 * labeled alternative in {@link grammarLangParser#math_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMathExpRegular(grammarLangParser.MathExpRegularContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mathExpSingleValue}
	 * labeled alternative in {@link grammarLangParser#math_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMathExpSingleValue(grammarLangParser.MathExpSingleValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#variable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariable(grammarLangParser.VariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(grammarLangParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(grammarLangParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#return_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_statement(grammarLangParser.Return_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#bool}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool(grammarLangParser.BoolContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#integer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteger(grammarLangParser.IntegerContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(grammarLangParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(grammarLangParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#print}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrint(grammarLangParser.PrintContext ctx);
	/**
	 * Visit a parse tree produced by {@link grammarLangParser#log_ex}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLog_ex(grammarLangParser.Log_exContext ctx);
}