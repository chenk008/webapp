package org.ck.webapp.jdk;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

public class StringTest {
	static final Unsafe unsafe = getUnsafe();
	static final boolean is64bit = true; // auto detect if possible.

	public static void main(String[] args) throws Exception {
		String s3 = new String("1") + new String("1");
		printAddresses("string", s3);
		System.out.println(System.identityHashCode(s3));
		s3.intern();
		String s4 = "11";
		System.out.println(System.identityHashCode(s4));
		printAddresses("string", s4);
		System.out.println(s3 == s4);
	}

	public static void printAddresses(String label, Object... objects) {
		System.out.print(label + ": 0x");
		long last = 0;
		int offset = unsafe.arrayBaseOffset(objects.getClass());
		int scale = unsafe.arrayIndexScale(objects.getClass());
		switch (scale) {
		case 4:
			long factor = is64bit ? 8 : 1;
			final long i1 = (unsafe.getInt(objects, offset) & 0xFFFFFFFFL)
					* factor;
			System.out.print(Long.toHexString(i1));
			last = i1;
			for (int i = 1; i < objects.length; i++) {
				final long i2 = (unsafe.getInt(objects, offset + i * 4) & 0xFFFFFFFFL)
						* factor;
				if (i2 > last)
					System.out.print(", +" + Long.toHexString(i2 - last));
				else
					System.out.print(", -" + Long.toHexString(last - i2));
				last = i2;
			}
			break;
		case 8:
			throw new AssertionError("Not supported");
		}
		System.out.println();
	}

	private static Unsafe getUnsafe() {
		try {
			Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			return (Unsafe) theUnsafe.get(null);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}

	public static void test1() throws Exception {
		// m、n是同一个string对象
		// u是另一个string对象，只是使用了同一个char数组
		// v是另一个string对象，使用了同一个char数组
		String m = "hello,world";
		String n = "hello,world";
		String u = new String(m);
		String v = new String("hello,world");

		System.out.println(m == n);
		System.out.println(m == u);
		System.out.println(m == v);

		// 利用反射修改字符串内部的字符数组的内容，结果都受到影响
		Field f = m.getClass().getDeclaredField("value");
		f.setAccessible(true);
		char[] cs = (char[]) f.get(m);
		cs[0] = 'H';

		String p = "Hello,world";
		System.out.println(p.equals(m));
		System.out.println(p.equals(n));
		System.out.println(p.equals(u));
		System.out.println(p.equals(v));

		System.out.println(p == m);
		System.out.println(p == n);
		System.out.println(p == u);
		System.out.println(p == v);
	}

	// 字符串常量通常是在编译的时候就确定好的，定义在类的方法区里边，
	// 也就是说，不同的类，即使用了同样的字符串， 还是属于不同的对象

	class A {
		public void print() {
			System.out.println("hello");
		}
	}

	class B {
		public void print() {
			String s = "hello";
			// 修改s的第一个字符为H
			System.out.println("hello"); // 输出Hello
			new A().print(); // 输出hello
		}
	}

	public static void testSubString() throws Exception {
		// java7的substring使用新的char数组来存储
		String m = "hello,world";
		String u = m.substring(2, 10);
		String v = u.substring(4, 7);
	}

	public static void test3() throws Exception {

		// 字符串操作时，可能需要修改原来的字符串数组内容或者原数组没法容纳的时候，
		// 就会使用另外一个新的数组，例如replace,concat,+等操作
		String m = "hello,";
		String u = m.concat("world");
	}

	public static void test4() throws Exception {

		// 常量池中的字符串通常是通过字面量的方式产生的，并且他们是在编译的时候就准备好了，类加载的时候，顺便就在常量池生成。
		// 即使是字符串的内容是一样的，都不能保证是同一个字符串数组。
		String m = "hello,world";
		String u = m + ".";
		String v = "hello,world.";
		// 如果让m声明为final，你就会发现u和v变成是同一个对象,
		// 这其实都是编译器搞的鬼，因为m是final的， u直接被编译成”hello,world.”了
	}

	public static void test5() throws Exception {

		// m,n使用的是同一个字符数组，但intern方法会到常量池里边去寻找字符串”he”,如果找到的话，就直接返回该字符串，
		// 否则就在常量池里边创建一个并返回，
		// 所以v使用的字符数组和m,n不是同一个
		String m = "hello,world";
		String u = m.substring(0, 2);
		String v = u.intern();
	}
}
