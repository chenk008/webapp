package org.ck.webapp.disruptor.workpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.ck.webapp.disruptor.MyProducer;
import org.ck.webapp.disruptor.OrderEvent;
import org.ck.webapp.disruptor.OrderEventFactory;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * Created by hll on 2016/8/1.
 * 
 * ���߳����������߳�����
 */
public class WorkPoolDemo {
	public static void main(String[] args) {
		// �����̳߳�
		ExecutorService executorService = Executors.newFixedThreadPool(3, new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "comsumer");
			}

		});
		EventFactory<OrderEvent> eventFactory = new OrderEventFactory();
		Disruptor<OrderEvent> disruptor = new Disruptor<>(eventFactory, 1024 * 1024, executorService,
				ProducerType.MULTI, new YieldingWaitStrategy());

		// ÿһ��MyWorkHandler����һ�������߳�
		EventHandlerGroup<OrderEvent> handleEventsWithWorkerPool = disruptor.handleEventsWithWorkerPool(
				new MyWorkHandler("ID-1"), new MyWorkHandler("ID-2"), new MyWorkHandler("ID-3"));
		disruptor.start();

		MyProducer producer = new MyProducer();

		Thread t = new Thread() {
			@Override
			public void run() {
				for (long l = 0; l < 100; l++) {
					producer.onData(disruptor.getRingBuffer(), l, "username-" + l, 1.0 + l);
				}
				System.out.println("finish producer");
			}
		};
		t.start();
	}
}
