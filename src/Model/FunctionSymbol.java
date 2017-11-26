package Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class FunctionSymbol extends Symbol implements Scope {
    Map<String, Symbol> parameters = new LinkedHashMap<>();
    Scope parentScope;
	
	public FunctionSymbol(String name, Symbol.Type returnType, Scope parentScope) {
		super(name,returnType);
		this.parentScope = parentScope;
	}
	
	@Override
	public String getScopeName() {
		return "function";
	}
	
	@Override
	public Scope getParentScope() {
		return parentScope;
	}
	
	@Override
	public void define(Symbol symbol) {
		parameters.put(symbol.name, symbol);
		symbol.scope = this;
	}
	
	@Override
	public Symbol lookup(String name){
		Symbol symbol = parameters.get(name);
		// check current scope for symbol
		if (symbol != null) {
			return symbol;
		}
		// check parent scope(s) for symbol
		if (parentScope != null) return parentScope.lookup(name);
		// not found anywhere
		return null;
	}
	
	public ArrayList<Symbol> getParameters(){
		return new ArrayList<>(parameters.values());
	}
	
	public String toString() { return "function"+super.toString()+":"+ parameters.values(); }
}
