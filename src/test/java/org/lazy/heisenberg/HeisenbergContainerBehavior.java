package org.lazy.heisenberg;

import static org.junit.Assert.assertEquals;
import static org.lazy.heisenberg.Heisenberg.let;
import static org.lazy.heisenberg.Heisenberg.propagateChangesOf;
import static org.lazy.heisenberg.Heisenberg.when;

import org.junit.Before;
import org.junit.Test;

public class HeisenbergContainerBehavior {

	private Observee observee;
	private VariableReader observer;

	@Before
	public void initObserverAndObservee() {
		observee = new Observee();
		observer = new VariableReader(observee);
		observee = when(observee).isInvoked(let(observer).update());
		observer = propagateChangesOf(observer);
	}

	@Test
	public void stopsObservingAnHeisenbergWhichItNoLongerContains() throws Exception {
		observee.setVariable("I am here");
		assertEquals("I am here", observer.getVariable());
		observer.setObservee(new Observee());
		observee.setVariable("No propagation");
		assertEquals("I am here", observer.getVariable());
	}

	@Test
	public void observesANewHeisenbergWhichHasReplacedAnOtherOne() throws Exception {
		Observee anotherObservee = propagateChangesOf(new Observee());
		observer.setObservee(anotherObservee);
		observee.setVariable("No propagation");
		anotherObservee.setVariable("New guy in the block");
		assertEquals("New guy in the block", observer.getVariable());
	}

	@Test
	public void followsMultipleObserveesWhichHaveBeenRegisteredInDifferentMoments() throws Exception {
		Observee anotherObservee = propagateChangesOf(new Observee());
		TwoVariablesReader observerOfTwo = new TwoVariablesReader(observee, anotherObservee);
		when(observee).isInvoked(let(observerOfTwo).updateFirst());
		observerOfTwo = propagateChangesOf(observerOfTwo);
		anotherObservee = when(anotherObservee).isInvoked(let(observerOfTwo).updateSecond());
		observerOfTwo = propagateChangesOf(observerOfTwo);
		observerOfTwo.setFirstObservee(anotherObservee);
		observee.setVariable("shouldnotbehere");
		anotherObservee.setVariable("echo");
		assertEquals("echoecho", observerOfTwo.getReading());
	}

}
