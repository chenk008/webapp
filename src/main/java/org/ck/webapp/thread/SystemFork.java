package org.ck.webapp.thread;

import java.io.IOException;

public class SystemFork {

	public static void main(String[] args) throws IOException {
		Runtime.getRuntime().exec("host -t a ");
	}
}
