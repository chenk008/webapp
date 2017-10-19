package org.ck.webapp.attach;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class LoopTest {

	/**
	 * java runtime相关的jmx信息
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		String name = runtime.getName();
		while (true) {
			System.out.println(System.currentTimeMillis() + ":" + name);
			Thread.sleep(1000 * 30L);
		}
	}

}
