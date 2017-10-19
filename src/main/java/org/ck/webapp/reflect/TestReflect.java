package org.ck.webapp.reflect;

import java.lang.reflect.Method;

public class TestReflect {

	/**
	 * java -XX:+TraceClassLoading TestClassLoad  
		看到GeneratedMethodAccessor1
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Class<?> clz = Class.forName("org.ck.webapp.reflect.A");
		Object o = clz.newInstance();
		Method m = clz.getMethod("foo", String.class);
		for (int i = 0; i < 16; i++) {
			m.invoke(o, Integer.toString(i));
		}
	}
}
