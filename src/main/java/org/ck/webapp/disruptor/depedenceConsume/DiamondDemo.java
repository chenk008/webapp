package org.ck.webapp.disruptor.depedenceConsume;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ck.webapp.disruptor.MyProducer;
import org.ck.webapp.disruptor.OrderEvent;
import org.ck.webapp.disruptor.OrderEventFactory;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * 菱形消费结构 Created by hll on 2016/8/1.
 */
public class DiamondDemo {
	public static void main(String[] args) throws InterruptedException {
		// 消费线程池
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		EventFactory<OrderEvent> eventFactory = new OrderEventFactory();
		Disruptor<OrderEvent> disruptor = new Disruptor<>(eventFactory, 1024 * 1024, executorService,
				ProducerType.SINGLE, new BusySpinWaitStrategy());

		// 被AccessNameHandler、AccessPriceHandler各消费一次（不保证顺序），最后都会被FinalEventHandler消费一次（保证顺序）
		
		//关于消费者的处理链路，完全都是靠ringbuffer的SequenceBarrier来完成的
		disruptor.handleEventsWith(new AccessNameHandler(), new AccessPriceHandler()).then(new FinalEventHandler());
		
		disruptor.start();

		MyProducer producer = new MyProducer();

		for (long i = 0; i < 100; i++) {
			producer.onData(disruptor.getRingBuffer(), i, "tom-" + i, 20.8 + i);
		}

		Thread.sleep(5000);

		disruptor.shutdown();
		executorService.shutdown();
	}
}
