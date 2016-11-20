package org.ck.webapp.jdk;

import java.util.HashMap;
import java.util.Map;

/**
 * ������gc�Ĳ�������
 * @author ck
 *
 */
public class GCTest {
	public static final int NUM = 1035;
	static Map<Integer, byte[]> map = new HashMap<Integer, byte[]>();

	public static void main(String args[]) throws InterruptedException {
		long timeStart = System.currentTimeMillis();
		for (int i = 0; i < 27 * NUM; i++) {
			byte[] b = new byte[NUM];
			map.put(i, b);
		}
		System.err.println("time cost:"
				+ (System.currentTimeMillis() - timeStart));
		Thread.sleep(10000L);
	}
}
