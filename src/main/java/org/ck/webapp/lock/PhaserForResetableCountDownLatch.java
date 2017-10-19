package org.ck.webapp.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * Phaser替代CountDownLatch、CyclicBarrier,可重置状态 可以变更并发数
 * 
 */
public class PhaserForResetableCountDownLatch {

	private static AtomicInteger ai = new AtomicInteger(0);

	private static AtomicInteger park = new AtomicInteger(0);

	public static void main(String[] args) {
		ExecutorService executorService = Executors.newFixedThreadPool(3);

		// 用于提交任务
		TaskFetcherThread t = new TaskFetcherThread();

		Phaser phaser = new Phaser(0) {
			protected synchronized boolean onAdvance(int phase, int registeredParties) {

				// 可能会重复unpark
				LockSupport.unpark(t);
				System.out.println("unpark:" + ai.incrementAndGet());

				System.out.println(Thread.currentThread().getName() + "执行onAdvance方法.....;phase:" + phase
						+ "registeredParties=" + registeredParties);

				return false;
			}
		}; // 相当于CountDownLatch(3)

		t.setExecutorService(executorService);
		t.setPhaser(phaser);
		t.start();

		submitAll(3, executorService, phaser);
	}

	private static class TaskFetcherThread extends Thread {

		private ExecutorService executorService;
		private Phaser phaser;

		public void setExecutorService(ExecutorService executorService) {
			this.executorService = executorService;
		}

		public void setPhaser(Phaser phaser) {
			this.phaser = phaser;
		}

		@Override
		public void run() {
			while (true) {
				try {
					System.out.println("wait:" + ai.get());

					// 多线程竞争下，同时对同一个线程执行多次unpark和park，可能导致无法unpark
					LockSupport.park();

					// 必须先取得phase，再提交执行任务，再wait
					int phase = phaser.getPhase();
					int randomNum = ThreadLocalRandom.current().nextInt(1, 3 + 1);
					submitAll(randomNum, executorService, phaser);

					// 测试异常情况会导致phase重复的场景，结果依然可以运行，就是会导致 重复的phase 和 下个phase 合并成一个phase，任务数变多
					// if (randomNum == 2) {
					// throw new RuntimeException("sdfsd");
					// }
					// phaser.awaitAdvance(phase);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	private static void submitAll(int num, ExecutorService executorService, final Phaser phaser) {
		 System.out.println("start:" + phaser.getPhase() + ":" + num);

		for (int i = 0; i < num; i++) {
			phaser.register();
		}

		for (int i = 0; i < num; i++) {
			submitTask(executorService, phaser);
		}
	}

	private static void submitTask(ExecutorService executorService, final Phaser phaser) {
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName() + "执行任务..." + phaser.getPhase());
				phaser.arriveAndDeregister();// countDownLatch.countDown()
			}
		});
	}

}
