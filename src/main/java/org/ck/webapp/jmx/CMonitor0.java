package org.ck.webapp.jmx;

import javax.management.MXBean;

/**
 * standard MBeans, dynamic MBeans, open MBeans(形如MXBean) and model
 * MBeans,@MXBean()注解的接口名字可以任意取
 */
@MXBean()
public interface CMonitor0 {
	public long getUptime();

	public String getMemory();
}
