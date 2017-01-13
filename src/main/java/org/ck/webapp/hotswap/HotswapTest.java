package org.ck.webapp.hotswap;

/**
 * spring load能reload .class文件
 * 正在执行的代码，是无法reload的
 * -Dspringloaded=watchJars  可以监控并且reload jar
 * -Dspringloaded="verbose;watchJars=spring-load-example-extra.jar" 
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
