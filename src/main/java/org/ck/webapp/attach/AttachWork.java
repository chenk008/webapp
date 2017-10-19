package org.ck.webapp.attach;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.VirtualMachine;

public class AttachWork {

	/**
	 * jvm attach api测试
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		VirtualMachine virtualmachine = VirtualMachine.attach("9884");

		String javaHome = virtualmachine.getSystemProperties().getProperty("java.home");
		String agentPath = javaHome + File.separator + "jre" + File.separator + "lib" + File.separator
				+ "management-agent.jar";
		File file = new File(agentPath);
		if (!file.exists()) {
			agentPath = javaHome + File.separator + "lib" + File.separator + "management-agent.jar";
			file = new File(agentPath);
			if (!file.exists()) {
				throw new IOException("Management agent not found");
			}
		}

		agentPath = file.getCanonicalPath();
		try {
			virtualmachine.loadAgent(agentPath, "com.sun.management.jmxremote");
		} catch (AgentLoadException e) {
			throw new IOException(e);
		} catch (AgentInitializationException agentinitializationexception) {
			throw new IOException(agentinitializationexception);
		}
		Properties properties = virtualmachine.getAgentProperties();
		String address = (String) properties.get("com.sun.management.jmxremote.localConnectorAddress");
		System.out.println(address);
		virtualmachine.detach();
	}
}
