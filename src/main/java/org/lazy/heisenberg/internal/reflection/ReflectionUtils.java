package org.lazy.heisenberg.internal.reflection;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class ReflectionUtils {

	public static <S> Set<Field> extractAllFields(S source) {
		Set<Field> allfields = new LinkedHashSet<Field>();
		for (Class<?> clazz = source.getClass(); !clazz.equals(Object.class); clazz = clazz.getSuperclass()) {
			Field[] declaredFields = clazz.getDeclaredFields();
			allfields.addAll(Arrays.asList(declaredFields));
		}
		return allfields;
	}

}
