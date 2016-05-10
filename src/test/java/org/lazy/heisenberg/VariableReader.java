package org.lazy.heisenberg;

public class VariableReader implements Updatable, OneVariableObservee {

	private OneVariableObservee observee;
	private String reading;
	
	public VariableReader() {
	}

	public VariableReader(OneVariableObservee observee) {
		this.observee = observee;
	}

	@Propagate
	public void setObservee(OneVariableObservee observee) {
		this.observee = observee;
	}

	@Propagate
	@Override
	public Updatable update() {
		reading = observee.getVariable();
		return this;
	}

	@Override
	public String getVariable() {
		return reading;
	}

	@Propagate
	@Override
	public void setVariable(String variable) {
		this.reading = variable;
	}

}
