package org.ck.webapp.jmx;

import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

/**
 * Server端
 * 
 * @author ck
 *
 */
public class ServerMonitorAgentv2 {

	public static void main(String[] args) throws Exception {
		// MBeanServer server=MBeanServerFactory.createMBeanServer();
		// //不可在jconsole中使用
		MBeanServer server = ManagementFactory.getPlatformMBeanServer();// 可在jconsole中使用

		// 指定端口
		LocateRegistry.createRegistry(1098);
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://127.0.0.1:" + 1098 + "/myJmx");
		Map<String, Object> env = new HashMap<String, Object>();
		// System.setProperty( "com.sun.management.jmxremote.authenticate",
		// "true" );
		// env.put( "jmx.remote.credentials", new String[] { "admin", "admin123"
		// } );

		JMXConnectorServer cntorServer = JMXConnectorServerFactory.newJMXConnectorServer(url, env, server);
		cntorServer.start();

		// 被管理的bean,"com.jmx"包名任意取，可以多个Bean
		ObjectName monitorName = new ObjectName("com.jmx:type=ServerMonitor");
		server.registerMBean(new ServerMonitor(), monitorName);
	}
}
