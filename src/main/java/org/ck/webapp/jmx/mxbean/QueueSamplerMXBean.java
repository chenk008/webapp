package org.ck.webapp.jmx.mxbean;

/**
 * �ͱ�׼MBeans������һ����һ��MXBean�Ķ���Ϊ����дһ������ SomethingMXBean �Ľӿ� �Լ�
 * ʵ�ָýӿڵ�һ��Java�ࡣȻ����MXBeans�����׼MBeans��MXBeans��Ҫ��Java��������Something���ڸýӿ��е�ÿ�������������Ҫô�Ǹ�MXBean��һ�����ԣ�Ҫô�Ǹ�MXBean��һ��������Ҳ������ע��@MXBean����עJava�ӿڣ��Ӷ�����Ҫ�ӿڵ�������"MXBean"Ϊ��׺��
 * 
 * 
 * MX֧���Զ���������������QueueSample
 * 
 * @author viruser
 *
 */
public interface QueueSamplerMXBean {
	public QueueSample getQueueSample();

	public void clearQueue();
}
