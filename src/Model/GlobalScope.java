package Model;

import java.util.Map;

public class GlobalScope extends BaseScope {
	public GlobalScope(Scope parentScope) {
		super(parentScope);
	}
	
	@Override
	public String getScopeName() {
		return "global scope";
	}
	
	@Override
	public Map<String, Symbol> getSymbols() {
		return super.getSymbols();
	}
}