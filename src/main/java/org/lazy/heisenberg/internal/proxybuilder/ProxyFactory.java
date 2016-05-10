package org.lazy.heisenberg.internal.proxybuilder;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Constructor;

public class ProxyFactory {

    public static <T> T createProxy(T toEnhance, Callback handler) {
        Class<T> clazz = findFirstMundaneClass(toEnhance);
        Enhancer enhancer = createEnhancer(handler, clazz);
        AvailableConstructor<T> constructor = extractAnAvailableConstructor(clazz);
        return constructor.createEnhancedInstance(enhancer);
    }

    @SuppressWarnings("unchecked")
    private static <T> AvailableConstructor<T> extractAnAvailableConstructor(Class<T> clazz) {
        Constructor<?> constructor = clazz.getConstructors()[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];
        return new AvailableConstructor(constructor, parameterTypes, parameters);
    }

    private static <T> Enhancer createEnhancer(Callback handler, Class<T> clazz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(handler);
        return enhancer;
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> findFirstMundaneClass(T toEnhance) {
        Class clazz = toEnhance.getClass();
        for(; Enhancer.isEnhanced(clazz);) {
           clazz = clazz.getSuperclass();
        }
        return clazz;
    }

    private static class AvailableConstructor<T> {
        private final Constructor<T> constructor;
        private final Class<?>[] parameterTypes;
        private final Object[] parameters;

        public AvailableConstructor(Constructor<T> constructor, Class<?>[] parameterTypes, Object[] parameters) {
            this.constructor = constructor;
            this.parameterTypes = parameterTypes;
            this.parameters = parameters;
        }

        @SuppressWarnings("unchecked")
        public T createEnhancedInstance(Enhancer enhancer) {
            return (T) enhancer.create(parameterTypes, parameters);
        }
    }
}
