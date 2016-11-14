package org.ck.webapp;

import java.lang.management.ManagementFactory;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.ClassLoadingGaugeSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.servlets.MetricsServlet;

public class MyMetricsServletContextListener extends MetricsServlet.ContextListener {

	public static final MetricRegistry METRIC_REGISTRY = new MetricRegistry();

	{
		METRIC_REGISTRY.registerAll(new MemoryUsageGaugeSet());
		METRIC_REGISTRY.registerAll(new ClassLoadingGaugeSet());
		METRIC_REGISTRY.registerAll(new GarbageCollectorMetricSet());
		METRIC_REGISTRY.registerAll(new BufferPoolMetricSet(ManagementFactory.getPlatformMBeanServer()));
	}

	@Override
	protected MetricRegistry getMetricRegistry() {
		return METRIC_REGISTRY;
	}

}
