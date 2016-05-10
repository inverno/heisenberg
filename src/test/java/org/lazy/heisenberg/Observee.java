package org.lazy.heisenberg;

public class Observee implements OneVariableObservee {

	private String variable;

	@Propagate
	@Override
	public void setVariable(String variable) {
		this.variable = variable;
	}

	@Override
	public String getVariable() {
		return variable;
	}

}
