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
 * �������ѽṹ Created by hll on 2016/8/1.
 */
public class DiamondDemo {
	public static void main(String[] args) throws InterruptedException {
		// �����̳߳�
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		EventFactory<OrderEvent> eventFactory = new OrderEventFactory();
		Disruptor<OrderEvent> disruptor = new Disruptor<>(eventFactory, 1024 * 1024, executorService,
				ProducerType.SINGLE, new BusySpinWaitStrategy());

		// ��AccessNameHandler��AccessPriceHandler������һ�Σ�����֤˳�򣩣���󶼻ᱻFinalEventHandler����һ�Σ���֤˳��
		
		//���������ߵĴ�����·����ȫ���ǿ�ringbuffer��SequenceBarrier����ɵ�
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
