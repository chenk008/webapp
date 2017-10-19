package org.ck.webapp.jmx.mxbean;

/**
 * 和标准MBeans的做法一样，一个MXBean的定义为：编写一个叫做 SomethingMXBean 的接口 以及
 * 实现该接口的一个Java类。然而，MXBeans不像标准MBeans，MXBeans不要求Java类必须叫做Something。在该接口中的每个方法，定义的要么是该MXBean的一个属性，要么是该MXBean的一个操作。也可以用注解@MXBean来标注Java接口，从而不需要接口的名字以"MXBean"为后缀。
 * 
 * 
 * MX支持自定义对象，例如这里的QueueSample
 * 
 * @author viruser
 *
 */
public interface QueueSamplerMXBean {
	public QueueSample getQueueSample();

	public void clearQueue();
}
