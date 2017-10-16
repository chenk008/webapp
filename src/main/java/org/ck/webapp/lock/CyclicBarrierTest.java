package org.ck.webapp.lock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 闭锁 与 同步屏障 的区别
 * 
 * 闭锁只会阻塞一条线程，目的是为了让该条任务线程满足条件后执行；
 * 而同步屏障会阻塞所有线程，目的是为了让所有线程同时执行（实际上并不会同时执行，而是尽量把线程启动的时间间隔降为最少）。
 * CountDownLatch的计数器无法被重置；CyclicBarrier的计数器可以被重置后使用，因此它被称为是循环的barrier。
 * CyclicBarrier 可以在所有线程到达但未释放时调用指定的Action (Runnable)做一些任务;
 * 
 * @author chenkang
 *
 */
public class CyclicBarrierTest {

	public static class BarrierAction extends Thread {
		private CyclicBarrier cb;

		public void setCyclicBarrier(CyclicBarrier cb) {
			this.cb = cb;
		}

		public void run() {
			// 当所有线程准备完毕后触发此任务,其中一个线程会执行该代码，其他线程等待
			// 执行完该任务后，所有线程开始运行自己的代码
			// System.out.println(Thread.currentThread().getName());

			// 如果这里调用 reset ，导致await的线程抛java.util.concurrent.BrokenBarrierException
			cb.reset();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		// 创建同步屏障对象，并制定需要等待的线程个数 和 打开屏障时需要执行的任务

		BarrierAction ba = new BarrierAction();

		CyclicBarrier barrier = new CyclicBarrier(3, ba);
		ba.setCyclicBarrier(barrier);

		// 启动三条线程
		for (int i = 0; i < 3; i++) {
			new Thread(new Runnable() {
				public void run() {
					// 等待，（每执行一次barrier.await，同步屏障数量-1，直到为0时，打开屏障）
					try {
						barrier.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BrokenBarrierException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// 任务
					System.out.println(Thread.currentThread().getName());
				}
			}).start();
		}

		Thread.sleep(60 * 1000);
	}

}
