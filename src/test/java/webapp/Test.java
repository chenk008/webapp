package webapp;

import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {

	private final static Logger logger = LoggerFactory.getLogger(Test.class);

	public static void main(String[] args) {

		long beginTime = System.currentTimeMillis();

		ThreadContext.put("a", "a");
		ThreadContext.push("p1");
		logger.info("�������������ʱ��{}����", (System.currentTimeMillis() - beginTime)); // ��һ���÷�
		ThreadContext.put("a", "b");
		ThreadContext.push("p2");
		logger.info("�������������ʱ��" + (System.currentTimeMillis() - beginTime) + "����");

		ThreadContext.clearAll();
	}

}
