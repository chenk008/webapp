package org.ck.webapp.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * 运行下面程序，就可以用jconsole进行本地连接了
 * @author ck
 *
 */
public class ServerMonitorAgent {
	public static void main(String[] args) throws Exception {
		// MBeanServer server=MBeanServerFactory.createMBeanServer();
		// //不可在jconsole中使用
		MBeanServer server = ManagementFactory.getPlatformMBeanServer();// 可在jconsole中使用
		// 被管理的bean,"com.jmx"包名任意取
		ObjectName monitorName = new ObjectName("com.jmx:type=ServerMonitor");
		server.registerMBean(new ServerMonitor(), monitorName);
		// 必须确保线程活着
		Thread.sleep(Long.MAX_VALUE);
	}
}