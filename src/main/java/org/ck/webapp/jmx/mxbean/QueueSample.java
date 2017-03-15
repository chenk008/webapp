package org.ck.webapp.jmx.mxbean;

import java.beans.ConstructorProperties;
import java.util.Date;

/**
 * �� QueueSample �У�MXBean��ܻ����QueueSample�е�����getters���������ص�ʵ��ת��Ϊһ�� CompositeData
 * ʵ����Ȼ��ʹ��@ConstructorPropertiesע�� ��һ��CompositeDataʵ�����ع�һ�� QueueSample ʵ����
 * 
 * @author viruser
 *
 */
public class QueueSample {

	private final Date date;
	private final int size;
	private final String head;

	@ConstructorProperties({ "date", "size", "head" })
	public QueueSample(Date date, int size, String head) {
		this.date = date;
		this.size = size;
		this.head = head;
	}

	public Date getDate() {
		return date;
	}

	public int getSize() {
		return size;
	}

	public String getHead() {
		return head;
	}
}
