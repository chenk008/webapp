package org.ck.webapp.resourceLoad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {

	public static void main(String[] args) {
		BufferedReader reader = null;
		try {
			// 会利用URLClassPath里面的JarLoader（每个jar对应一个）从jar文件读取
			// 有多个JarLoader，按照顺序一个一个都，先读到则返回
			// 必须要用getResourceAsStream，不能用读文件的形式读取，因为jar包里面的资源文件对操作系统来说不是一个文件
			reader = new BufferedReader(new InputStreamReader(Test.class
					.getClassLoader().getResourceAsStream("resourceLoad/a")));
			String row;
			while ((row = reader.readLine()) != null) {
				System.out.println(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
