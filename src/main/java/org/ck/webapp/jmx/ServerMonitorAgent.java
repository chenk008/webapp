package org.ck.webapp.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * ����������򣬾Ϳ�����jconsole���б���������
 * @author ck
 *
 */
public class ServerMonitorAgent {
	public static void main(String[] args) throws Exception {
		// MBeanServer server=MBeanServerFactory.createMBeanServer();
		// //������jconsole��ʹ��
		MBeanServer server = ManagementFactory.getPlatformMBeanServer();// ����jconsole��ʹ��
		// �������bean,"com.jmx"��������ȡ
		ObjectName monitorName = new ObjectName("com.jmx:type=ServerMonitor");
		server.registerMBean(new ServerMonitor(), monitorName);
		// ����ȷ���̻߳���
		Thread.sleep(Long.MAX_VALUE);
	}
}