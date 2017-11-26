package Model;

import java.util.Map;

public class LocalScope extends BaseScope {
	
	private String scopeName = "local scope";
	
	public LocalScope(Scope parentScope) {
		super(parentScope);
	}
	
	public LocalScope(Scope parentScope, String scopeName) {
		super(parentScope);
		this.scopeName = scopeName;
	}
	
	@Override
	public Map<String, Symbol> getSymbols() {
		return super.getSymbols();
	}
	
	@Override
	public String getScopeName() {
		return scopeName;
	}
}
