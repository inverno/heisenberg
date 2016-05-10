package org.lazy.heisenberg.internal.heisenbergs;

import java.util.HashMap;
import java.util.Map;

import org.lazy.heisenberg.Heisenberg;
import org.lazy.heisenberg.internal.propagation.Propagator;
import org.lazy.heisenberg.internal.proxybuilder.ProxyFactory;

public class Heisenbergs {

	private static final Map<Object, Heisenberg<?>> heisenbergs = new HashMap<Object, Heisenberg<?>>();
	private static int working = 0;

	public static <S> Heisenberg<S> makeHeisenberg(S source) {
		Heisenberg<S> heisenberg = retrieveHeisenberg(source);
		if (heisenberg == null) {
			Propagator propagator = new Propagator(source);
			S proxy = ProxyFactory.createProxy(source, propagator);
			heisenberg = new Heisenberg<S>(propagator, proxy, source);
			heisenbergs.put(source, heisenberg);
			heisenbergs.put(proxy, heisenberg);
		}
		heisenberg.registerInternalHeisenbergsIn(source);
		return heisenberg;
	}

	@SuppressWarnings("unchecked")
	private static <S> Heisenberg<S> retrieveHeisenberg(S source) {
		Heisenberg<S> heisenberg = (Heisenberg<S>) heisenbergs.get(source);
		return heisenberg;
	}

	public static boolean hasAnHeisenbergFor(Object candidate) {
      return heisenbergs.containsKey(candidate);
	}

	public static Object getSourceFor(Object candidate) {
		working++;
		if (!hasAnHeisenbergFor(candidate)) {
			working--;
			return candidate;
		}
		Heisenberg<Object> heisenberg = retrieveHeisenberg(candidate);
		Object source = heisenberg.getSource();
		working--;
		return source;
	}
	
	public static boolean isWorking() {
		return working > 0;
	}

}
