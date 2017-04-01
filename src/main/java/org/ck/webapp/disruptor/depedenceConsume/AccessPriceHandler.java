package org.ck.webapp.disruptor.depedenceConsume;

import org.ck.webapp.disruptor.OrderEvent;

import com.lmax.disruptor.EventHandler;

/**
 * Created by hll on 2016/8/1.
 */
public class AccessPriceHandler implements EventHandler<OrderEvent> {
  @Override
  public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
    System.out.println("receiving price: " + event.getPrice());
    event.setPrice(event.getPrice() * 10);
  }
}
