package org.ck.webapp.jmx.dynamicBean;

public class ServerMonitorImpl {
	private final long startTime;
	private Runtime runtime = Runtime.getRuntime();

	public ServerMonitorImpl() {
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
