package org.ck.webapp.classloader;

public class UnloadClassTest {
	public static void main(String[] args) {
		try {
			MyURLClassLoader classLoader = new MyURLClassLoader();
			Class classLoaded = classLoader.loadClass("classloader.MyClass");
			System.out.println(classLoaded.getName());

			classLoaded = null;
			classLoader = null;

			System.out.println("��ʼGC");
			//ж��class����Ҫclassloaderʵ������ж��
			System.gc();
			System.out.println("GC���");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
