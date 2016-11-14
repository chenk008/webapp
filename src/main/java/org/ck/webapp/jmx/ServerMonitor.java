package org.ck.webapp.jmx;

/**
 * MBean，被管理对象，一如JavaBean。Bean对象，实体，对应资源
 * 
 * @author ck
 *
 */
public class ServerMonitor implements CMonitor0 {
	private final long startTime;
	private Runtime runtime = Runtime.getRuntime();

	public ServerMonitor() {
		startTime = System.currentTimeMillis();
	}

	public long getUptime() {
		return System.currentTimeMillis() - startTime;
	}

	public String getMemory() {
		float freeMemory = (float) runtime.freeMemory();
		float totalMemory = (float) runtime.totalMemory();
		String memory = "idle scale : " + (freeMemory / totalMemory) + "%; freeMemory=" + (freeMemory) / 1024
				+ " KB; totalMemory=" + (totalMemory) / 1024 + " KB";
		return memory;
	}
}
