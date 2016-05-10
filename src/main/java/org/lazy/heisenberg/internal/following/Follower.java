package org.lazy.heisenberg.internal.following;

import java.lang.reflect.Field;
import java.util.Set;

import org.lazy.heisenberg.Heisenberg;
import org.lazy.heisenberg.internal.heisenbergs.Heisenbergs;
import org.lazy.heisenberg.internal.propagation.PropagationAddress;

public class Follower {

	private final Field field;
	private Object value;

	public Follower(Field field, Object value) {
		this.field = field;
		this.value = value;
	}

	public void update(Object source) {
		try {
			Object newValue = field.get(source);
			if (newValue.equals(value)) return;
			Heisenberg<Object> newHeisenberg = Heisenbergs.makeHeisenberg(newValue);
			Heisenberg<Object> oldHeisenberg = Heisenbergs.makeHeisenberg(value);
			Set<PropagationAddress> addressesToMigrate = oldHeisenberg.propagationsRelatedTo(source);
			if (newHeisenberg != oldHeisenberg) {
				newHeisenberg.addAddresses(addressesToMigrate);
				oldHeisenberg.removeAddresses(addressesToMigrate);
			}
			value = newHeisenberg.getProxy();
			field.set(source, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
