package org.ck.webapp.lock;

import java.util.concurrent.CountDownLatch;

/**
 * 若有多条线程，其中一条线程需要等到其他所有线程准备完所需的资源后才能运行，这样的情况可以使用闭锁。
 * 
 * @author chenkang
 *
 */
public class CountDownLatchTest {

	public static void main(String[] args) {
		// 初始化闭锁，并设置资源个数
		CountDownLatch latch = new CountDownLatch(2);

		new Thread(new Runnable() {
			public void run() {
				// 加载资源1
				// 本资源加载完后，闭锁-1
				System.out.println(Thread.currentThread().getName() + ":exit");
				latch.countDown();
			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				// 加载资源2
				// 本资源加载完后，闭锁-1
				System.out.println(Thread.currentThread().getName() + ":exit");
				latch.countDown();
			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				// 本线程必须等待所有资源加载完后才能执行
				try {
					latch.await();
					System.out.println(Thread.currentThread().getName() + ":start");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 当闭锁数量为0时，await返回，执行接下来的任务
				// 任务代码……
			}
		}).start();
	}

}
