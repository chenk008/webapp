package org.ck.webapp.jvm;

public class TestTLAB {

	/**
	 * -XX:+PrintTLAB -XX:+Verbose
	 * 
	 * TLABResize 默认是true，每次gc的时候都会动态调整每个线程的TLAB大小，每个线程不同
	 * refill 代表分给该线程的TLAB块数，desired_size就是TLAB的大小，refill waste是指没有用起来的TLAB
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 5; i++) {
			new Thread() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.Thread#run()
				 */
				@Override
				public void run() {
					while (true) {
						byte[] bytes = new byte[1024 * 1024];
						byte[] bytes1 = new byte[1024];
						// Thread.sleep(1000);
					}
				}

			}.start();
		}
	}
}
