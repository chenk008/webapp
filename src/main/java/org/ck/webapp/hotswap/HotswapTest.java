package org.ck.webapp.hotswap;

/**
 * spring loadֻ��reload .class�ļ�
 * ����ִ�еĴ��룬���޷�reload��
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
