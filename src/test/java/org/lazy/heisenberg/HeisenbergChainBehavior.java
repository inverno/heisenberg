package org.lazy.heisenberg;

import static org.junit.Assert.assertEquals;
import static org.lazy.heisenberg.Heisenberg.let;
import static org.lazy.heisenberg.Heisenberg.propagateChangesOf;
import static org.lazy.heisenberg.Heisenberg.when;

import org.junit.Test;

public class HeisenbergChainBehavior {

	@Test
	public void aChangePropagatesAsLongAsHeisenbergsAreInvolved() throws Exception {
		Observee observee = propagateChangesOf(new Observee());
		VariableReader observerAndObservee = propagateChangesOf(new VariableReader(observee));
		VariableReader finalObserver = new VariableReader(observerAndObservee);
		when(observee).isInvoked(let(observerAndObservee).update());
		when(observerAndObservee).isInvoked(let(finalObserver).update());
		observee.setVariable("a change from far away");
		assertEquals("a change from far away", finalObserver.getVariable());
	}

	@Test
	public void cyclicDependenciesBetweenObserversAndObserveesResultInJustOneCycle() throws Exception {
		VariableReader firstLinkInTheCycle = propagateChangesOf(new VariableReader());
		VariableReader secondLinkInTheCycle = propagateChangesOf(new VariableReader());
		VariableReader thirdLinkInTheCycle = propagateChangesOf(new VariableReader());
		firstLinkInTheCycle.setObservee(secondLinkInTheCycle);
		secondLinkInTheCycle.setObservee(thirdLinkInTheCycle);
		thirdLinkInTheCycle.setObservee(firstLinkInTheCycle);
		when(firstLinkInTheCycle).isInvoked(let(thirdLinkInTheCycle).update());
		when(secondLinkInTheCycle).isInvoked(let(firstLinkInTheCycle).update());
		when(thirdLinkInTheCycle).isInvoked(let(secondLinkInTheCycle).update());
		firstLinkInTheCycle.setVariable("cycle!");
		assertEquals("cycle!", secondLinkInTheCycle.getVariable());
		assertEquals("cycle!", thirdLinkInTheCycle.getVariable());
		thirdLinkInTheCycle.setVariable("cycle backwards!");
		assertEquals("cycle backwards!", firstLinkInTheCycle.getVariable());
		assertEquals("cycle backwards!", secondLinkInTheCycle.getVariable());
	}
}
