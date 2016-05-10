package org.lazy.heisenberg.internal.propagation;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.InvocationHandler;
import org.lazy.heisenberg.Heisenberg;
import org.lazy.heisenberg.internal.propagation.InvocationsRecorder;

import java.lang.reflect.Method;

public class QuickRecorder implements InvocationHandler {
    private final InvocationsRecorder invocationsRecorder;
    private final Heisenberg<?> heisenberg;

    public QuickRecorder(InvocationsRecorder invocationsRecorder, Heisenberg<?> heisenberg) {
        this.invocationsRecorder = invocationsRecorder;
        this.heisenberg = heisenberg;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        invocationsRecorder.invoke(proxy, method, args);
        heisenberg.updatePropagator(invocationsRecorder);
        return null;
    }
}
