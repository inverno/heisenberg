package org.lazy.heisenberg.internal.propagation;

import java.lang.reflect.Method;

import java.util.LinkedHashSet;
import java.util.Set;


import net.sf.cglib.proxy.InvocationHandler;

public class InvocationsRecorder implements InvocationHandler {

	private final Object target;
	private final Set<PropagationAddress> addresses = new LinkedHashSet<PropagationAddress>();

	public InvocationsRecorder(Object target) {
		this.target = target;
	}

    @Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		addresses.add(new PropagationAddress(target, method, args));
		return null;
	}

	public Set<PropagationAddress> getPropagationAddresses() {
		return addresses;
	}
}
