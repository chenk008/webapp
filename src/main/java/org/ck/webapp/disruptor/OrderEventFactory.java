package org.ck.webapp.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * Created by hll on 2016/8/1. ���ڳ�ʼ��event
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {
	@Override
	public OrderEvent newInstance() {
		return new OrderEvent();
	}
}
