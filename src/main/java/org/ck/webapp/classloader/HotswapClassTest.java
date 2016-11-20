package org.ck.webapp.classloader;

public class HotswapClassTest {

	public static void main(String[] args) {
		try {
			//每次创建特定类加载器的新实例来加载指定类型的不同版本
			MyURLClassLoader classLoader1 = new MyURLClassLoader();
			Class classLoaded1 = classLoader1.loadClass("classloader.MyClass");
			MyURLClassLoader classLoader2 = new MyURLClassLoader();
			Class classLoaded2 = classLoader2.loadClass("classloader.MyClass");

			// 判断两个Class实例是否相同
			System.out.println(classLoaded1 == classLoaded2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}