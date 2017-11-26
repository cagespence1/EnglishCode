package Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Scope {
	public String getScopeName();
	
	public Scope getParentScope();
	
	public void define(Symbol sym);
	
	public Symbol lookup(String name);
}