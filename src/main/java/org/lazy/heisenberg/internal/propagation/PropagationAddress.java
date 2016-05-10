package org.lazy.heisenberg.internal.propagation;

import java.lang.reflect.Method;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lazy.heisenberg.internal.heisenbergs.Heisenbergs;

public class PropagationAddress {

	private final Object target;
	private final Method method;
	private final Object[] args;

	public PropagationAddress(Object target, Method method, Object[] args) {
		this.target = target;
		this.method = method;
		this.args = args;
	}

	public void propagate() throws Throwable {
		method.invoke(target, args);
	}

	public boolean isRelatedTo(Object target) {
		target = Heisenbergs.getSourceFor(target);
		Object thisTarget = Heisenbergs.getSourceFor(this.target);
		return thisTarget.equals(target);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
