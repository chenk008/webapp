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
 * Server��
 * 
 * @author ck
 *
 */
public class ServerMonitorAgentv2 {

	public static void main(String[] args) throws Exception {
		// MBeanServer server=MBeanServerFactory.createMBeanServer();
		// //������jconsole��ʹ��
		MBeanServer server = ManagementFactory.getPlatformMBeanServer();// ����jconsole��ʹ��

		// ָ���˿�
		LocateRegistry.createRegistry(1098);
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://127.0.0.1:" + 1098 + "/myJmx");
		Map<String, Object> env = new HashMap<String, Object>();
		// System.setProperty( "com.sun.management.jmxremote.authenticate",
		// "true" );
		// env.put( "jmx.remote.credentials", new String[] { "admin", "admin123"
		// } );

		JMXConnectorServer cntorServer = JMXConnectorServerFactory.newJMXConnectorServer(url, env, server);
		cntorServer.start();

		// �������bean,"com.jmx"��������ȡ�����Զ��Bean
		ObjectName monitorName = new ObjectName("com.jmx:type=ServerMonitor");
		server.registerMBean(new ServerMonitor(), monitorName);
	}
}
