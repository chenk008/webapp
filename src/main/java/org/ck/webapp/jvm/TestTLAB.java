package org.ck.webapp.jvm;

public class TestTLAB {

	/**
	 * -XX:+PrintTLAB -XX:+Verbose
	 * 
	 * TLABResize Ĭ����true��ÿ��gc��ʱ�򶼻ᶯ̬����ÿ���̵߳�TLAB��С��ÿ���̲߳�ͬ
	 * refill ����ָ����̵߳�TLAB������desired_size����TLAB�Ĵ�С��refill waste��ָû����������TLAB
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
