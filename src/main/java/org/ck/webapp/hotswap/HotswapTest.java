package org.ck.webapp.hotswap;

/**
 * spring load只能reload .class文件
 * 正在执行的代码，是无法reload的
 * 
 * @author viruser
 *
 */
public class HotswapTest {

	public static void main(String[] args) throws InterruptedException {
		while (true) {
			HotswapUtil.say();
			Thread.sleep(1000);
		}
	}

}
