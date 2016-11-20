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
		// m��n��ͬһ��string����
		// u����һ��string����ֻ��ʹ����ͬһ��char����
		// v����һ��string����ʹ����ͬһ��char����
		String m = "hello,world";
		String n = "hello,world";
		String u = new String(m);
		String v = new String("hello,world");

		System.out.println(m == n);
		System.out.println(m == u);
		System.out.println(m == v);

		// ���÷����޸��ַ����ڲ����ַ���������ݣ�������ܵ�Ӱ��
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

	// �ַ�������ͨ�����ڱ����ʱ���ȷ���õģ���������ķ�������ߣ�
	// Ҳ����˵����ͬ���࣬��ʹ����ͬ�����ַ����� �������ڲ�ͬ�Ķ���

	class A {
		public void print() {
			System.out.println("hello");
		}
	}

	class B {
		public void print() {
			String s = "hello";
			// �޸�s�ĵ�һ���ַ�ΪH
			System.out.println("hello"); // ���Hello
			new A().print(); // ���hello
		}
	}

	public static void testSubString() throws Exception {
		// java7��substringʹ���µ�char�������洢
		String m = "hello,world";
		String u = m.substring(2, 10);
		String v = u.substring(4, 7);
	}

	public static void test3() throws Exception {

		// �ַ�������ʱ��������Ҫ�޸�ԭ�����ַ����������ݻ���ԭ����û�����ɵ�ʱ��
		// �ͻ�ʹ������һ���µ����飬����replace,concat,+�Ȳ���
		String m = "hello,";
		String u = m.concat("world");
	}

	public static void test4() throws Exception {

		// �������е��ַ���ͨ����ͨ���������ķ�ʽ�����ģ������������ڱ����ʱ���׼�����ˣ�����ص�ʱ��˳����ڳ��������ɡ�
		// ��ʹ���ַ�����������һ���ģ������ܱ�֤��ͬһ���ַ������顣
		String m = "hello,world";
		String u = m + ".";
		String v = "hello,world.";
		// �����m����Ϊfinal����ͻᷢ��u��v�����ͬһ������,
		// ����ʵ���Ǳ�������Ĺ���Ϊm��final�ģ� uֱ�ӱ�����ɡ�hello,world.����
	}

	public static void test5() throws Exception {

		// m,nʹ�õ���ͬһ���ַ����飬��intern�����ᵽ���������ȥѰ���ַ�����he��,����ҵ��Ļ�����ֱ�ӷ��ظ��ַ�����
		// ������ڳ�������ߴ���һ�������أ�
		// ����vʹ�õ��ַ������m,n����ͬһ��
		String m = "hello,world";
		String u = m.substring(0, 2);
		String v = u.intern();
	}
}
