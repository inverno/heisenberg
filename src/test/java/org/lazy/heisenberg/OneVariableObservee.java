package org.lazy.heisenberg;

public interface OneVariableObservee {

	@Propagate
	void setVariable(String variable);

	String getVariable();

}