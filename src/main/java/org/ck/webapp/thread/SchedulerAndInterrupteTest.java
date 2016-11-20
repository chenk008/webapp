package org.ck.webapp.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SchedulerAndInterrupteTest {

	public static void main(String[] args) throws InterruptedException {
		runningAndInterrupt();
	}

	/**
	 * wait������interrupt֮������interrupt�쳣����������Ҳ���Ƴ���
	 * 
	 * @throws InterruptedException
	 */
	public static void waitAndInterrupt() throws InterruptedException {
		ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
		ScheduledFuture<?> future = pool.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				synchronized (this) {
					try {
						System.out.println("Waiting for b to complete...");
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}, 0, 60, TimeUnit.SECONDS);

		Thread.sleep(10000L);
		boolean c = future.cancel(true);
		System.out.println(c);
	}

	/**
	 * ����ѭ���ķ��������ʹ��interrupt����Ҫ����Thread.interrupted()�жϣ�����interruptʱ�����Ѿ��Ƴ���
	 * 
	 * @throws InterruptedException
	 */
	public static void runningAndInterrupt() throws InterruptedException {
		ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
		ScheduledFuture<?> future = pool.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (Thread.interrupted()) {
						System.out.println("interrupt");
						break;
					}
					System.out.println("hello");
				}
			}

		}, 0, 60, TimeUnit.SECONDS);

		Thread.sleep(1000L);

		boolean c = future.cancel(true);
		System.out.println(c);
	}

	/**
	 * ��ǰ�����е�����cancel��ʱ�򲻻�ȥִ��interrupt
	 * 
	 * @throws InterruptedException
	 */
	public static void idleAndInterrupt() throws InterruptedException {
		ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
		ScheduledFuture<?> future = pool.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				System.out.println("hello");
			}

		}, 0, 60, TimeUnit.SECONDS);

		Thread.sleep(1000L);

		boolean c = future.cancel(true);
		System.out.println(c);
	}
}
