package org.ck.webapp.reflect;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class TestMethodHandle {

	public static void main(String[] args) throws Throwable {
		MethodType type = MethodType.methodType(void.class, String.class);
		MethodHandle method = MethodHandles.lookup().findVirtual(A.class, "foo", type);
		Class<?> clz = Class.forName("org.ck.webapp.reflect.A");
		Object o = clz.newInstance();
		for (int i = 0; i < 16; i++) {
			method.invoke(o, Integer.toString(i));
		}
	}
}
