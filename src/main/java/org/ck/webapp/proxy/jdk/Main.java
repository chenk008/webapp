package org.ck.webapp.proxy.jdk;

public class Main {
	public static void main(String[] args) {

		IVehical car = new Car();
		VehicalProxy proxy = new VehicalProxy(car);

		IVehical proxyObj = proxy.create();
		proxyObj.run();
	}
}
