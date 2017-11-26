package Model;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseScope implements Scope {
	Scope parentScope;
	Map<String, Symbol> symbols = new LinkedHashMap<>();
	
	public BaseScope(Scope parentScope) {
		this.parentScope = parentScope;
	}
	
	public Symbol lookup(String name){
		Symbol symbol = symbols.get(name);
		// check current scope for symbol
		if (symbol != null) {
			return symbol;
		}
		// check parent scope(s) for symbol
		if (parentScope != null) return parentScope.lookup(name);
		// not found anywhere
		return null;
	}
	
	@Override
	public Scope getParentScope() {
		return parentScope;
	}
	
	@Override
	public void define(Symbol sym) {
		symbols.put(sym.name, sym);
		sym.scope = this;
	}
	
	public Map<String, Symbol> getSymbols() {
		return symbols;
	}
	
	public String toString() { return getScopeName()+":"+symbols.keySet().toString(); }
}
