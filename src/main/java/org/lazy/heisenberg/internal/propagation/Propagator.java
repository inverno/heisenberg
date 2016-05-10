package org.lazy.heisenberg.internal.propagation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.proxy.InvocationHandler;

import org.lazy.heisenberg.Heisenberg;
import org.lazy.heisenberg.Propagate;
import org.lazy.heisenberg.internal.following.Follower;
import org.lazy.heisenberg.internal.heisenbergs.Heisenbergs;

public class Propagator implements InvocationHandler {

	private final Set<PropagationAddress> propagationAddresses;
	private final Object source;
	private Boolean currentlyPropagating = false;
	private final Map<Field, Follower> followers = new HashMap<Field, Follower>();

	public Propagator(Object source) {
		this.source = source;
		this.propagationAddresses = new LinkedHashSet<PropagationAddress>();
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = method.invoke(source, args);
		if (method.getAnnotation(Propagate.class) != null) {
			notifyObservers();
		}
		return result;
	}

	private void notifyObservers() throws Throwable {
		synchronized (currentlyPropagating) {
			if (!currentlyPropagating) {
				updateFollowers();
				currentlyPropagating = true;
				Set<PropagationAddress> currentAddresses = new LinkedHashSet<PropagationAddress>(propagationAddresses);
				for (PropagationAddress propagationAddress : currentAddresses) {
					propagationAddress.propagate();
				}
				currentlyPropagating = false;
			}
		}
	}

	private void updateFollowers() {
		for (Follower follower : followers.values()) {
			follower.update(source);
		}
	}

	public void addAddresses(Set<PropagationAddress> propagationAddresses) {
		this.propagationAddresses.addAll(propagationAddresses);
	}

	public void updateInternalHeisenbergsIn(Object source, Set<Field> allfields) {
		for (Field field : allfields) {
			field.setAccessible(true);
			try {
				Object value = field.get(source);
				if (Heisenbergs.hasAnHeisenbergFor(value)) {
					Heisenberg<?> heisenberg = Heisenbergs.makeHeisenberg(value);
					Set<PropagationAddress> addresses = heisenberg.propagationsRelatedTo(source);
					Follower follower = new Follower(field, value);
					addFollower(field, follower);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void addFollower(Field field, Follower follower) {
		Follower oldFollower = followers.get(field);
		if (oldFollower != null) {
			// follower.mergeWith(oldFollower);
		}
		followers.put(field, follower);
	}

	public Set<PropagationAddress> propagationsRelatedTo(Object source) {
		LinkedHashSet<PropagationAddress> relatedAddresses = new LinkedHashSet<PropagationAddress>();
		for (PropagationAddress address : propagationAddresses) {
			if (address.isRelatedTo(source)) {
				relatedAddresses.add(address);
			}
		}
		return relatedAddresses;
	}

	public void removeAddresses(Set<PropagationAddress> addresses) {
		propagationAddresses.removeAll(addresses);
	}
}
