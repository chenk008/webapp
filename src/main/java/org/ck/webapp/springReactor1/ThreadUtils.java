package org.ck.webapp.springReactor1;

import java.lang.reflect.Field;

public class ThreadUtils {

	public static void getInheritedAccessControlContext() throws Exception {
		Field field = Thread.class.getDeclaredField("inheritedAccessControlContext");
		field.setAccessible(true);
		Object obj = field.get(Thread.currentThread());
		System.out.println(obj);
	}
}
