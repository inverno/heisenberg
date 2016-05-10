package org.lazy.heisenberg;

import static org.junit.Assert.*;
import static org.lazy.heisenberg.Heisenberg.let;
import static org.lazy.heisenberg.Heisenberg.when;
import static org.lazy.heisenberg.Heisenberg.propagateChangesOf;

import org.junit.Before;
import org.junit.Test;

public class HeisenbergBehavior {

	private OneVariableObservee observee;
	private VariableReader observer;

	@Before
	public void initObserverAndObservee() {
		observee = new Observee();
		observer = new VariableReader(observee);
	}

	@Test
	public void reportsToObserverWheneverAnObserveeChanges() throws Exception {
		observee = when(observee).isInvoked(let(observer).update());
		observee.setVariable("one");
		assertEquals("one", observer.getVariable());
	}

	@Test
	public void anObserveeReportsToAllObservers() throws Exception {
		OneVariableObservee originalObservee = observee;
		VariableReader otherObserver = new VariableReader(originalObservee);
		observee = when(originalObservee).isInvoked(let(observer).update());
		when(originalObservee).then(otherObserver).update();
		observee.setVariable("multi");
		assertEquals("multi", otherObserver.getVariable());
		assertEquals("multi", observer.getVariable());
	}

	@Test
	public void registersObserverInSplittedInvocation() throws Exception {
		observee = propagateChangesOf(observee);
		when(observee).isInvoked(let(observer).update());
		observee.setVariable("split");
		assertEquals("split", observer.getVariable());
	}
	
	@Test
	public void wrapsDifferentObjectsInDifferentProxies() throws Exception {
		OneVariableObservee proxy1 = propagateChangesOf(observee);
		OneVariableObservee proxy2 = propagateChangesOf(new Observee());
		assertFalse(proxy1 == proxy2);
	}
	
	@Test
	public void doesNotWrapTheSameObjectTwice() throws Exception {
		OneVariableObservee proxy1 = propagateChangesOf(observee);
		OneVariableObservee proxy2 = propagateChangesOf(observee);
		assertTrue(proxy1 == proxy2);
	}
	
	@Test
	public void doesNotRewrapAProxy() throws Exception {
		OneVariableObservee proxy1 = propagateChangesOf(observee);
		OneVariableObservee proxy2 = propagateChangesOf(proxy1);
		assertTrue(proxy1 == proxy2);
	}

}
