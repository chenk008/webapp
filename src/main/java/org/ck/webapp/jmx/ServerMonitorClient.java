package org.ck.webapp.jmx;

import java.util.HashMap;
import java.util.Map;

import javax.management.JMX;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class ServerMonitorClient {
	public static void main(String[] args) throws Exception {
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:1098/myJmx");
		Map<String, Object> env = new HashMap<String, Object>();
		// env.put( "jmx.remote.credentials", new String[] { "admin", "admin123"
		// } );
		JMXConnector conn = JMXConnectorFactory.connect(url, env);

		CMonitor0 monitor = JMX.newMBeanProxy(conn.getMBeanServerConnection(),
				new ObjectName("com.jmx:type=ServerMonitor"), CMonitor0.class);
		for (int i = 0; i < 10; i++) {
			System.out.println(monitor.getUptime());
			System.out.println(monitor.getMemory());
		}
		conn.close();
	}
}
