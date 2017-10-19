package org.ck.webapp.classloader;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * class 要想被unload，它对应的classloader必须也可被垃圾收回掉
 * 
 * -XX:+PrintGCDetails 
 * -verbose 输出类的load和unload信息
 * @author wuhua.ck
 *
 */
public class UnloadClass {

	public static void main(String... args) throws ClassNotFoundException,
			NoSuchFieldException, IllegalAccessException, InterruptedException {
		URL url = UnloadClass.class.getProtectionDomain().getCodeSource()
				.getLocation();
		final String className = UnloadClass.class.getPackage().getName()
				+ ".UtilityClass";
		{
			ClassLoader cl;
			Class clazz;

			for (int i = 0; i < 2; i++) {
				cl = new CustomClassLoader(url);
				clazz = cl.loadClass(className);
				loadClass(clazz);

				cl = new CustomClassLoader(url);
				clazz = cl.loadClass(className);
				loadClass(clazz);
				triggerGC();
			}
		}
		triggerGC();
	}

	private static void triggerGC() throws InterruptedException {
		System.out.println("\n-- Starting GC");
		System.gc();
		Thread.sleep(100);
		System.out.println("-- End of GC\n");
	}

	private static void loadClass(Class clazz) throws NoSuchFieldException,
			IllegalAccessException {
		final Field id = clazz.getDeclaredField("ID");
		id.setAccessible(true);
		id.get(null);
	}

	private static class CustomClassLoader extends URLClassLoader {
		public CustomClassLoader(URL url) {
			super(new URL[] { url }, null);
		}

		@Override
		protected Class<?> loadClass(String name, boolean resolve)
				throws ClassNotFoundException {
			try {
				return super.loadClass(name, resolve);
			} catch (ClassNotFoundException e) {
				return Class.forName(name, resolve,
						UnloadClass.class.getClassLoader());
			}
		}

		@Override
		protected void finalize() throws Throwable {
			super.finalize();
//			System.out.println(this.toString() + " - ClassLoader Finalized.");
		}
	}
}

class UtilityClass {
	static final String ID = Integer.toHexString(System
			.identityHashCode(UtilityClass.class));
	private static final Object FINAL = new Object() {
		@Override
		protected void finalize() throws Throwable {
			super.finalize();
//			System.out.println(ID + " Class static Field Finalized.");
		}
	};

	static {
		System.out.println(ID + " Initialising");
	}
}
