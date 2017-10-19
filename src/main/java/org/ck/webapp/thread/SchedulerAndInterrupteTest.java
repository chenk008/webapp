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
	 * wait的任务，interrupt之后会产生interrupt异常，但是任务也会移除掉
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
	 * 无限循环的方法，如果使用interrupt，需要增加Thread.interrupted()判断，但是interrupt时任务已经移除掉
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
	 * 当前不运行的任务，cancel的时候不会去执行interrupt
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
