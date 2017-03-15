package org.ck.webapp.alg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * ��������ʽ�����������������б�

 * Ӳ������ ���Ƚ����ݷֳ�n�ݣ���1-249999�ǵ�һ�ݣ�250000��499999�ǵڶ��ݣ��Դ����ƣ�Ȼ���ÿһ���ļ���ϵͳ����Ȼ����������
 * 
 * @author viruser
 *
 */
public class BitSort2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("usage: java -Xms2M -Xmx2M com.ch1.BitSort3 d:\\input.txt d:\\output.txt");
			return;
		}

		BitSort2 client = new BitSort2();
		long l1 = System.currentTimeMillis();

		client.fun(args[0], args[1]);

		long l2 = System.currentTimeMillis();
		System.out.println("time:" + (l2 - l1));

	}

	public final static int SIZE = 10000000; // �ܴ�С
	public final static int TIMES = 40; // ����
	public final static int UNIT = SIZE / TIMES;

	void fun(String inputName, String outputName) {
		try {
			PrintWriter pw = new PrintWriter(outputName);

			/**
			 * 
			 */
			for (int index = 0; index < TIMES; index++) {
				BufferedReader br = new BufferedReader(new FileReader(inputName));
				int low = UNIT * index, high = low + UNIT;
				int[] arr = new int[UNIT];
				int counter = 0;
				String s = "";
				while ((s = br.readLine()) != null) {
					int in = Integer.parseInt(s);
					if (in > low && in <= high) {
						arr[counter++] = in;
					}
				}
				arr = Arrays.copyOf(arr, counter);
				Arrays.sort(arr);
				int size = arr.length;
				for (int i = 0; i < size; i++) {
					pw.println(arr[i]);
				}
				pw.flush();
				br.close();
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}