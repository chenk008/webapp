package org.ck.webapp.classloader;

public class HotswapClassTest {

	public static void main(String[] args) {
		try {
			//ÿ�δ����ض������������ʵ��������ָ�����͵Ĳ�ͬ�汾
			MyURLClassLoader classLoader1 = new MyURLClassLoader();
			Class classLoaded1 = classLoader1.loadClass("classloader.MyClass");
			MyURLClassLoader classLoader2 = new MyURLClassLoader();
			Class classLoaded2 = classLoader2.loadClass("classloader.MyClass");

			// �ж�����Classʵ���Ƿ���ͬ
			System.out.println(classLoaded1 == classLoaded2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}