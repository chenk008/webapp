package org.ck.webapp.finalize;

/**
 * 该类用于演示在对象的finalize()方法中复活对象
 * 
 * @version v1.0
 */
public class CanReliveObj {
	public static CanReliveObj obj;

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println("CanReliveObj finalize called !!!");
		obj = this;// 在finalize方法中复活对象
	}

	@Override
	public String toString() {
		return "I am canReliveObj";
	}

	public static void main(String[] args) throws InterruptedException {
		obj = new CanReliveObj();
		obj = null; // 将obj设为null
		System.gc();// 垃圾回收

		Thread.sleep(1000);//
		if (obj == null) {
			System.out.println("obj is null");
		} else {
			System.out.println("obj is alive");
		}

		System.out.println("第2次调用gc后");
		obj = null;// 由于obj被复活，此处再次将obj设为null
		System.gc();// 再次gc
		Thread.sleep(1000);
		if (obj == null) {
			// 对象的finalize方法仅仅会被调用一次，所以可以预见再次设置obj为null后，obj会被垃圾回收，该语句会被调用
			System.out.println("obj is null");
		} else {
			System.out.println("obj is alive");
		}
	}

}