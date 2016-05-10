package org.lazy.heisenberg;

import java.lang.reflect.Field;
import java.util.Set;

import net.sf.cglib.proxy.Callback;
import org.lazy.heisenberg.internal.heisenbergs.Heisenbergs;
import org.lazy.heisenberg.internal.propagation.InvocationsRecorder;
import org.lazy.heisenberg.internal.propagation.PropagationAddress;
import org.lazy.heisenberg.internal.propagation.Propagator;
import org.lazy.heisenberg.internal.proxybuilder.ProxyFactory;
import org.lazy.heisenberg.internal.propagation.QuickRecorder;
import org.lazy.heisenberg.internal.reflection.ReflectionUtils;

public class Heisenberg<S> {

	private static final ThreadLocal<InvocationsRecorder> recorder = new ThreadLocal<InvocationsRecorder>();

	public static <T> T let(T target) {
		InvocationsRecorder handler = new InvocationsRecorder(target);
		T recordingTarget = ProxyFactory.createProxy(target, handler);
		recorder.set(handler);
		return recordingTarget;
	}

	private final S proxy;
	private final Propagator propagator;
	private final S source;

	public static <S> Heisenberg<S> when(S source) {
		return Heisenbergs.makeHeisenberg(source);
	}

	public static <S> S propagateChangesOf(S source) {
		return Heisenbergs.makeHeisenberg(source).proxy;
	}

	public Heisenberg(Propagator propagator, S proxy, S source) {
		this.propagator = propagator;
		this.proxy = proxy;
		this.source = source;
	}

	public S isInvoked(Object dummyResultThanksToJava) {
		updatePropagation();
		return proxy;
	}

  public <T> T then(T target) {
    Callback handler = new QuickRecorder(new InvocationsRecorder(target), this);
		return ProxyFactory.createProxy(target, handler);
  }

	private void updatePropagation() {
		InvocationsRecorder invocationsRecorder = recorder.get();
		if (invocationsRecorder == null) throw new RuntimeException(
				"Please, use the idiom when(a).isInvoked(let(b).doSomething())");
    updatePropagator(invocationsRecorder);
		recorder.set(null);
	}

  public final void updatePropagator(InvocationsRecorder invocationsRecorder) {
    Set<PropagationAddress> propagationAddresses = invocationsRecorder.getPropagationAddresses();
    propagator.addAddresses(propagationAddresses);
  }

  public void registerInternalHeisenbergsIn(S source) {
		Set<Field> allFields = ReflectionUtils.extractAllFields(source);
		propagator.updateInternalHeisenbergsIn(source, allFields);
	}

	public Set<PropagationAddress> propagationsRelatedTo(Object source) {
		return propagator.propagationsRelatedTo(source);
	}

	public void addAddresses(Set<PropagationAddress> addresses) {
		propagator.addAddresses(addresses);
	}

	public void removeAddresses(Set<PropagationAddress> addresses) {
		propagator.removeAddresses(addresses);
	}

	public S getProxy() {
		return this.proxy;
	}

	public S getSource() {
		return this.source;
	}

}
