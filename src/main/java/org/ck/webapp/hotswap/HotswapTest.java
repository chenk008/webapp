package org.ck.webapp.hotswap;

/**
 * spring load��reload .class�ļ�
 * ����ִ�еĴ��룬���޷�reload��
 * -Dspringloaded=watchJars  ���Լ�ز���reload jar
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
