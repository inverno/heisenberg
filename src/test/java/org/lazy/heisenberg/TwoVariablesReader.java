package org.lazy.heisenberg;

public class TwoVariablesReader {

	private Observee observee;
	private Observee anotherObservee;
	private String firstReading = "";
	private String secondReading = "";

	public TwoVariablesReader(Observee observee, Observee anotherObservee) {
		this.observee = observee;
		this.anotherObservee = anotherObservee;
	}

	public TwoVariablesReader updateFirst() {
		this.firstReading = observee.getVariable();
		return this;
	}

	public TwoVariablesReader updateSecond() {
		this.secondReading = anotherObservee.getVariable();
		return this;
	}

	@Propagate
	public void setFirstObservee(Observee anotherObservee) {
		this.observee = anotherObservee;
	}

	public String getReading() {
		return this.firstReading + this.secondReading;
	}

}
