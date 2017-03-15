package org.ck.webapp.jmx.modelBean;

import javax.management.Descriptor;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.modelmbean.DescriptorSupport;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.management.modelmbean.RequiredModelMBean;

/**
 * 
 * 普通的动态 Bean 通常缺乏一些管理系统所需要的支持：比如持久化 MBean
 * 的状态、日志记录、缓存等等。如果让用户去一一实现这些功能确实是件枯燥无聊的工作。为了减轻用户的负担，JMX 提供商都会提供不同的 ModelBean
 * 实现。其中有一个接口是 Java
 * 规范中规定所有厂商必须实现的：javax.management.modelmbean.RequiredModelBean。通过配置 Descriptor
 * 信息，我们可以定制这个 Model Bean， 指定哪些 MBean 状态需要记入日志、如何记录以及是否缓存某些属性、缓存多久等等。
 */
public class Main {

	public static void main(String[] args) throws Exception {
		MBeanServer mBeanServer = MBeanServerFactory.createMBeanServer();
		RequiredModelMBean serverMBean = (RequiredModelMBean) mBeanServer
				.instantiate("javax.management.modelmbean.RequiredModelMBean");

		ObjectName serverMBeanName = new ObjectName("server: id=Server");
		// 和其它 MBean,唯一不同的是，ModelMBean 需要额外两步
		// 第一步用于提供 serverMBean 的元数据
		serverMBean.setModelMBeanInfo(getModelMBeanInfoForServer(serverMBeanName));
		Server server = new Server();
		// 第二步指出了 ServerMBean 管理的对象
		serverMBean.setManagedResource(server, "ObjectReference");

		ObjectInstance registeredServerMBean = mBeanServer.registerMBean((Object) serverMBean, serverMBeanName);

		serverMBean.invoke("start", null, null);

		Thread.sleep(1000);

		System.out.println(serverMBean.getAttribute("upTime"));
		Thread.sleep(5000);
		System.out.println(serverMBean.getAttribute("upTime"));
	}

	private static ModelMBeanInfo getModelMBeanInfoForServer(ObjectName objectName) throws Exception {
		ModelMBeanAttributeInfo[] serverAttributes = new ModelMBeanAttributeInfo[1];
		Descriptor upTime = new DescriptorSupport(new String[] { "name=upTime", "descriptorType=attribute",
				"displayName=Server upTime", "getMethod=getUpTime", });
		serverAttributes[0] = new ModelMBeanAttributeInfo("upTime", "long", "Server upTime", true, false, false,
				upTime);

		ModelMBeanOperationInfo[] serverOperations = new ModelMBeanOperationInfo[2];

		Descriptor getUpTimeDesc = new DescriptorSupport(new String[] { "name=getUpTime", "descriptorType=operation",
				"class=modelmbean.Server", "role=operation" });

		MBeanParameterInfo[] getUpTimeParms = new MBeanParameterInfo[0];
		serverOperations[0] = new ModelMBeanOperationInfo("getUpTime", "get the up time of the server", getUpTimeParms,
				"java.lang.Long", MBeanOperationInfo.ACTION, getUpTimeDesc);

		Descriptor startDesc = new DescriptorSupport(
				new String[] { "name=start", "descriptorType=operation", "class=modelmbean.Server", "role=operation" });
		MBeanParameterInfo[] startParms = new MBeanParameterInfo[0];
		serverOperations[1] = new ModelMBeanOperationInfo("start", "start(): start server", startParms,
				"java.lang.Integer", MBeanOperationInfo.ACTION, startDesc);

		ModelMBeanInfo serverMMBeanInfo = new ModelMBeanInfoSupport("modelmbean.Server",
				"ModelMBean for managing an Server", serverAttributes, null, serverOperations, null);

		// Default strategy for the MBean.
		// 其中用 "currencyTimeLimit=10" 指出属性的缓存时间是 10 秒。所以，在 Main 方法中，两次
		// serverMBean.getAttribute("upTime")；之间的间隔小于 10 秒就会得到同样的缓存值
		Descriptor serverDescription = new DescriptorSupport(
				new String[] { ("name=" + objectName), "descriptorType=mbean", ("displayName=Server"),
						"type=modelmbean.Server", "log=T", "logFile=serverMX.log", "currencyTimeLimit=10" });
		serverMMBeanInfo.setMBeanDescriptor(serverDescription);
		return serverMMBeanInfo;
	}
}
