package org.ck.webapp.jmx;

import javax.management.MXBean;

/**
 * standard MBeans, dynamic MBeans, open MBeans(����MXBean) and model
 * MBeans,@MXBean()ע��Ľӿ����ֿ�������ȡ
 */
@MXBean()
public interface CMonitor0 {
	public long getUptime();

	public String getMemory();
}
