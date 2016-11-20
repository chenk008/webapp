package org.ck.webapp.jdk;

import java.util.HashMap;
import java.util.Map;

public class MemTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub

		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < 100; i++) {
			map.put(String.valueOf(i), String.valueOf(i));
		}
		Thread.sleep(10000000);
	}

}
