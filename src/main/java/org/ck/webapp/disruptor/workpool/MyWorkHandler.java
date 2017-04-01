package org.ck.webapp.disruptor.workpool;

import org.ck.webapp.disruptor.OrderEvent;

import com.lmax.disruptor.WorkHandler;

/**
 * Created by hll on 2016/8/1.
 */
public class MyWorkHandler implements WorkHandler<OrderEvent> {

	private String id;

	public MyWorkHandler(String id) {
		this.id = id;
	}

	@Override
	public void onEvent(OrderEvent event) throws Exception {
		System.out.println(id + " received :" + event);
		Thread.sleep(1000);
	}
}
