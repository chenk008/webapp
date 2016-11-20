package org.ck.webapp.resourceLoad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {

	public static void main(String[] args) {
		BufferedReader reader = null;
		try {
			// ������URLClassPath�����JarLoader��ÿ��jar��Ӧһ������jar�ļ���ȡ
			// �ж��JarLoader������˳��һ��һ�������ȶ����򷵻�
			// ����Ҫ��getResourceAsStream�������ö��ļ�����ʽ��ȡ����Ϊjar���������Դ�ļ��Բ���ϵͳ��˵����һ���ļ�
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
