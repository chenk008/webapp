package org.ck.webapp.lock;

public class SynchronizedTest {

	public static void main(String[] args) throws InterruptedException {
		String a = "1";
		new TestThread(a).start();
		// ����a��b���ַ�������������a��b��ͬһ������
		String b = "1";
		new TestThread(b).start();

		Thread.sleep(10L);
		a = "2";
		//����߳����õ�������Ϊa�Ķ������˱仯�������ǡ�1������ַ�������
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
