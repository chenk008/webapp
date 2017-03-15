package org.ck.webapp.collectorSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test {

	public static class A implements Comparable<A> {

		public A(int a) {
			super();
			this.a = a;
			this.b = String.valueOf(a);
		}
		public A(int a,String b) {
			super();
			this.a = a;
			this.b = b;
		}

		int a;
		String b;

		/**
		 * 不返回0的情况，排序还是正确的，稳定性看算法
		 */
		@Override
		public int compareTo(A o) {
			return o.a > this.a ? 1 : -1;
		}

		@Override
		public String toString() {
			return "A [a=" + a + ", b=" + b + "]";
		}

	}

	public static void main(String[] args) {

		List<A> list = new ArrayList<A>();
		list.add(new A(1));
		list.add(new A(2,"aaaa"));
		list.add(new A(2,"bbb"));
		list.add(new A(2));
		list.add(new A(2,"ss"));
		list.add(new A(3));

		Collections.sort(list);
		
		System.out.println(list);
	}
}