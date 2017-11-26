package Model;

public class VariableSymbol extends Symbol{
	
	static int id = 0;
	private Object value;
	
	private int variableStoreIndex;
	private boolean globalVariable = false;
	private String mnemonic;
	private String descriptor;
	
	public VariableSymbol(String name, Type type) {
		super(name, type);
		variableStoreIndex = ++id;
	}
	
	public void setInitialValue(Object value) {
		this.value = value;
	}
	
	public Object getInitialValue() {
		return value;
	}
	
	public int getVariableStoreIndex() {
		return variableStoreIndex;
	}
	
	public void setMnemonic(String mnemonic) {
		this.mnemonic = mnemonic;
	}
	
	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}
	
	public String getMnemonic() {
		return mnemonic;
	}
	
	public String getDescriptor() {
		return descriptor;
	}
	
	public void setVariableStoreIndex(int variableStoreIndex) {
		this.variableStoreIndex = variableStoreIndex;
	}
	
	public boolean isGlobalVariable() {
		return globalVariable;
	}
	
	public void setGlobalVariable(boolean globalVariable) {
		this.globalVariable = globalVariable;
	}
}