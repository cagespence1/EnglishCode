package Model;

public class Symbol {
    
    public static enum Type { INTEGER, BOOLEAN, STRING }
    
    String name;
    Type type;
    Scope scope;
    
    public Symbol(String name) {
        this.name = name;
    }
    
    public Symbol(String name, Type type) {
        this(name);
        this.type = type;
    }
    
    public Type getType() {
        return type;
    }
    
    public String getName() {
        return name;
    }
    
    public String toString() {
        return '<'+getName()+":"+type+'>';
    }
}
