package org.ck.webapp.metrics;

import java.lang.management.ManagementFactory;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.ClassLoadingGaugeSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

public class MetricsUtils {

	public static final MetricRegistry METRIC_REGISTRY = new MetricRegistry();

	static {
		// METRIC_REGISTRY.registerAll(new MemoryUsageGaugeSet());
		// METRIC_REGISTRY.registerAll(new ClassLoadingGaugeSet());
		// METRIC_REGISTRY.registerAll(new GarbageCollectorMetricSet());
		// METRIC_REGISTRY.registerAll(new
		// BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));

		final ConsoleReporter reporter = ConsoleReporter.forRegistry(METRIC_REGISTRY)
				//meter��timerͳ��Ƶ�ʵĵ�λ
				.convertRatesTo(TimeUnit.SECONDS)
				//timer��histogram ͳ��ֵ������
				.convertDurationsTo(TimeUnit.MILLISECONDS).build();
		reporter.start(1, TimeUnit.MINUTES);
	}

	// ����ӿ�������LoadingCache��LoadingCache�ڻ��������ʱ�����Զ����ػ���
	private static final LoadingCache<String, LongAdder> METRIC_CACHE
	// CacheBuilder�Ĺ��캯����˽�еģ�ֻ��ͨ���侲̬����newBuilder()�����CacheBuilder��ʵ��
			= CacheBuilder.newBuilder()
					// ���ò�������Ϊ8������������ָ����ͬʱд������߳���
					.concurrencyLevel(8)
					// ����д�����8���ӹ���
					.expireAfterWrite(8, TimeUnit.SECONDS)
					// ���û��������ĳ�ʼ����Ϊ10
					.initialCapacity(10)
					// ���û����������Ϊ100������100֮��ͻᰴ��LRU�������ʹ���㷨���Ƴ�������
					.maximumSize(100)
					// ����Ҫͳ�ƻ����������
					.recordStats()
					// ���û�����Ƴ�֪ͨ
					.removalListener(new RemovalListener<Object, Object>() {
						@Override
						public void onRemoval(RemovalNotification<Object, Object> notification) {
							System.out.println(
									notification.getKey() + " was removed, cause is " + notification.getCause());
						}
					})
					// build�����п���ָ��CacheLoader���ڻ��治����ʱͨ��CacheLoader��ʵ���Զ����ػ���
					.build(new CacheLoader<String, LongAdder>() {
						@Override
						public LongAdder load(String key) throws Exception {
							return new LongAdder();
						}
					});

	/**
	 * ͳ��һ��ʱ���ڵ�ƽ��ֵ�����ֵ����Сֵ
	 * ���м���ͳ���㷨��Ĭ����ȫ��ʱ���ڣ�����ѡ��ExponentiallyDecayingReservoir�����������ڣ�/SlidingWindowReservoir��ָ���������ڣ�/SlidingTimeWindowReservoir��ָ����������ʱ�䣩��
	 * 
	 * @param name
	 * @return
	 */
	public static Histogram histogram(String name) {
		return METRIC_REGISTRY.histogram(name);
	}

	/**
	 * ͳ�ƴ���ƽ��ֵ������/ʱ�䣩
	 * 
	 * @param name
	 * @return
	 */
	public static Meter meter(String name) {
		return METRIC_REGISTRY.meter(name);
	}

	/**
	 * ͬʱͳ��histogram��meter
	 * 
	 * @param name
	 * @return
	 */
	public static Timer timer(String name) {
		return METRIC_REGISTRY.timer(name);
	}

	public static <T extends Metric> void metric(String name, T metric) {
		METRIC_REGISTRY.register(name, metric);
	}

	public static void addMinuteCounter(String key, long n) {
		try {
			METRIC_CACHE.get("key").add(n);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

}
