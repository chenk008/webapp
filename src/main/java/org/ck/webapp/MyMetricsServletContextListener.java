package org.ck.webapp;

import org.ck.webapp.metrics.MetricsUtils;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;

public class MyMetricsServletContextListener extends MetricsServlet.ContextListener {

	@Override
	protected MetricRegistry getMetricRegistry() {
		return MetricsUtils.METRIC_REGISTRY;
	}

}
