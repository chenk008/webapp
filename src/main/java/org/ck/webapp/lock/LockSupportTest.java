package org.ck.webapp.lock;

import java.util.concurrent.locks.LockSupport;

public class LockSupportTest {

	public static void main(String[] args) {

		Thread m = Thread.currentThread();

		Thread t = new Thread() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(100L);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("t");
					LockSupport.park();
					LockSupport.unpark(m);

				}
			}

		};

		t.start();

		while (true) {
			System.out.println("m");
			LockSupport.unpark(t);
			LockSupport.park();
		}

	}

}
