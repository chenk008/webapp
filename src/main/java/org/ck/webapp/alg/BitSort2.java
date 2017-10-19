package org.ck.webapp.alg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * 以增序形式输出经过排序的整数列表。

 * 硬盘排序法 首先将数据分成n份，如1-249999是第一份，250000到499999是第二份，以此类推，然后对每一份文件用系统排序，然后把它输出。
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

	public final static int SIZE = 10000000; // 总大小
	public final static int TIMES = 40; // 次数
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