package org.lazy.heisenberg.internal.proxybuilder;

import static org.junit.Assert.assertNotNull;
import net.sf.cglib.proxy.NoOp;

import org.junit.Test;

public class ProxyFactoryBehavior {

	@Test
	public void buildsProxiesWithNoArgumentConstructors() throws Exception {
		NoArgumentsConstructorType proxy = ProxyFactory.createProxy(new NoArgumentsConstructorType(), NoOp.INSTANCE);
		assertNotNull(proxy);
	}

	@Test
	public void buildsProxiesWithImplicitConstructors() throws Exception {
		ImplicitConstructorType proxy = ProxyFactory.createProxy(new ImplicitConstructorType(), NoOp.INSTANCE);
		assertNotNull(proxy);
	}

}
