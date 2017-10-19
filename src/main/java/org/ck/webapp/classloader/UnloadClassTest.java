package org.ck.webapp.classloader;

public class UnloadClassTest {
	public static void main(String[] args) {
		try {
			MyURLClassLoader classLoader = new MyURLClassLoader();
			Class classLoaded = classLoader.loadClass("classloader.MyClass");
			System.out.println(classLoaded.getName());

			classLoaded = null;
			classLoader = null;

			System.out.println("开始GC");
			//卸载class，需要classloader实例可以卸载
			System.gc();
			System.out.println("GC完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
