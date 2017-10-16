package org.ck.webapp.lock;

import java.util.concurrent.Phaser;

/**
 * Phaser替代CountDownLatch、CyclicBarrier,可重置状态
 */
public class PhaserForCountDownLatch {

	public static void main(String[] args) {
		Phaser phaser = new Phaser(3) {
			protected boolean onAdvance(int phase, int registeredParties) {
				System.out.println(Thread.currentThread().getName() + "执行onAdvance方法.....;phase:" + phase
						+ "registeredParties=" + registeredParties);
				return phase == 3;
			}
		}; // 相当于CountDownLatch(3)

		// 子任务
		for (int i = 0; i < 3; i++) {
			Task_05 task = new Task_05(phaser);
			Thread thread = new Thread(task, "PhaseTest_" + i);
			thread.start();
		}
	}

	static class Task_05 implements Runnable {
		private final Phaser phaser;

		Task_05(Phaser phaser) {
			this.phaser = phaser;
		}

		@Override
		public void run() {
			for (int i = 0; i < 5; i++) {
				System.out.println(Thread.currentThread().getName() + "执行任务...");
				phaser.arriveAndAwaitAdvance(); // countDownLatch.countDown()
			}
		}
	}
}
