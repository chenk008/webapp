package org.ck.webapp.disruptor;

import com.lmax.disruptor.RingBuffer;

/**
 * Created by hll on 2016/8/1.
 */
public class MyProducer {

	public void onData(RingBuffer<OrderEvent> ringBuffer, long id, String username, double price) {
		long sequence = ringBuffer.next();
		OrderEvent orderEvent = ringBuffer.get(sequence);
		orderEvent.setId(id);
		orderEvent.setPrice(price);
		orderEvent.setUsername(username);
		ringBuffer.publish(sequence);
	}
}
