package org.ck.webapp.lock;

public class SynchronizedTest {

	public static void main(String[] args) throws InterruptedException {
		String a = "1";
		new TestThread(a).start();
		// 由于a、b是字符串，常量池中a、b是同一个对象
		String b = "1";
		new TestThread(b).start();

		Thread.sleep(10L);
		a = "2";
		//这个线程能拿到锁，因为a的对象发生了变化，锁的是“1”这个字符串对象
		new TestThread(a).start();
	}

	static class TestThread extends Thread {
		private String lock;

		public TestThread(String lock) {
			this.lock = lock;
		}

		@Override
		public void run() {
			synchronized (lock) {
				System.out.println("lock:" + lock);
				try {
					Thread.sleep(1000000L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("unlock:" + lock);
			}
		}

	}
}
