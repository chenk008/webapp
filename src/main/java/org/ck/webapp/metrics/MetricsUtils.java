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
				//meter，timer统计频率的单位
				.convertRatesTo(TimeUnit.SECONDS)
				//timer中histogram 统计值的周期
				.convertDurationsTo(TimeUnit.MILLISECONDS).build();
		reporter.start(1, TimeUnit.MINUTES);
	}

	// 缓存接口这里是LoadingCache，LoadingCache在缓存项不存在时可以自动加载缓存
	private static final LoadingCache<String, LongAdder> METRIC_CACHE
	// CacheBuilder的构造函数是私有的，只能通过其静态方法newBuilder()来获得CacheBuilder的实例
			= CacheBuilder.newBuilder()
					// 设置并发级别为8，并发级别是指可以同时写缓存的线程数
					.concurrencyLevel(8)
					// 设置写缓存后8秒钟过期
					.expireAfterWrite(8, TimeUnit.SECONDS)
					// 设置缓存容器的初始容量为10
					.initialCapacity(10)
					// 设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
					.maximumSize(100)
					// 设置要统计缓存的命中率
					.recordStats()
					// 设置缓存的移除通知
					.removalListener(new RemovalListener<Object, Object>() {
						@Override
						public void onRemoval(RemovalNotification<Object, Object> notification) {
							System.out.println(
									notification.getKey() + " was removed, cause is " + notification.getCause());
						}
					})
					// build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
					.build(new CacheLoader<String, LongAdder>() {
						@Override
						public LongAdder load(String key) throws Exception {
							return new LongAdder();
						}
					});

	/**
	 * 统计一段时间内的平均值，最大值，最小值
	 * （有几种统计算法，默认是全局时间内，可以选择ExponentiallyDecayingReservoir（最近五分钟内）/SlidingWindowReservoir（指定滑动窗口）/SlidingTimeWindowReservoir（指定滑动窗口时间））
	 * 
	 * @param name
	 * @return
	 */
	public static Histogram histogram(String name) {
		return METRIC_REGISTRY.histogram(name);
	}

	/**
	 * 统计次数平均值（次数/时间）
	 * 
	 * @param name
	 * @return
	 */
	public static Meter meter(String name) {
		return METRIC_REGISTRY.meter(name);
	}

	/**
	 * 同时统计histogram和meter
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
