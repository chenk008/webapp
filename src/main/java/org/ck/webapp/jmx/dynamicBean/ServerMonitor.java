package org.ck.webapp.jmx.dynamicBean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;

/**
 * ���ںܶ����е� bean ʵ�֣��� ����淶 �������ϱ�׼ MBean ��Ҫ���ع�������Щ bean �Է��ϱ�׼ MBean ��׼�ȷ���Ҳ��ʵ�ʡ�JMX
 * �и����˶�̬��Dynamic�� MBean �ĸ��MBServer �������� ����淶 ����ֱ�Ӳ�ѯ��̬ MBean ������Ԫ���ݣ�meta
 * data���Ի�� MBean �Ķ���ӿ�
 * 
 * @author viruser
 *
 */
public class ServerMonitor implements DynamicMBean {

	private final ServerMonitorImpl target;
	private MBeanInfo mBeanInfo;

	public ServerMonitor(ServerMonitorImpl target) {
		this.target = target;
	}

	// ʵ�ֻ�ȡ������� ServerImpl �� upTime
	public long upTime() {
		return target.getUptime();
	}

	// javax.management.MBeanServer ��ͨ����ѯ getAttribute("Uptime") ��� "Uptime" ����ֵ
	public Object getAttribute(String attribute)
			throws AttributeNotFoundException, MBeanException, ReflectionException {
		if (attribute.equals("UpTime")) {
			return upTime();
		}
		return null;
	}

	// ���� ServerMonitor ��Ԫ��Ϣ��
	public MBeanInfo getMBeanInfo() {
		if (mBeanInfo == null) {
			try {
				Class cls = this.getClass();
				// �÷����� "upTime" ���ԵĶ�����
				Method readMethod = cls.getMethod("upTime", new Class[0]);
				// �÷����ù��췽��
				Constructor constructor = cls.getConstructor(new Class[] { ServerMonitorImpl.class });
				// ���� "upTime" ���Ե�Ԫ��Ϣ : ����Ϊ UpTime��ֻ������ ( û��д���� )��
				MBeanAttributeInfo upTimeMBeanAttributeInfo = new MBeanAttributeInfo("UpTime",
						"The time span since server start", readMethod, null);
				// ���ڹ��캯����Ԫ��Ϣ
				MBeanConstructorInfo mBeanConstructorInfo = new MBeanConstructorInfo("Constructor for ServerMonitor",
						constructor);
				// ServerMonitor ��Ԫ��Ϣ��Ϊ�˼����������������
				// û���ṩ invocation �Լ� listener �����Ԫ��Ϣ
				mBeanInfo = new MBeanInfo(cls.getName(), "Monitor that controls the server",
						new MBeanAttributeInfo[] { upTimeMBeanAttributeInfo },
						new MBeanConstructorInfo[] { mBeanConstructorInfo }, null, null);
			} catch (Exception e) {
				throw new Error(e);
			}

		}
		return mBeanInfo;
	}

	public AttributeList getAttributes(String[] arg0) {
		return null;
	}

	public Object invoke(String arg0, Object[] arg1, String[] arg2) throws MBeanException, ReflectionException {
		return null;
	}

	public void setAttribute(Attribute arg0)
			throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
		return;
	}

	public AttributeList setAttributes(AttributeList arg0) {
		return null;
	}
}
