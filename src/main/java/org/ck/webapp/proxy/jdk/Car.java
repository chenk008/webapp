package org.ck.webapp.proxy.jdk;

public class Car implements IVehical {

	public void run() {
		System.out.println("Car is running");
		System.out.println(this.getClass());
	}

}
