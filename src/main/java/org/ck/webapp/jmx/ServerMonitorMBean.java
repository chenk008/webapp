package org.ck.webapp.jmx;

import javax.management.MXBean;

/**
 * standard MBeans, dynamic MBeans, open MBeans(����MXBean) and model MBeans,
 */
public interface ServerMonitorMBean {
	public long getUptime();

	public String getMemory();
}
