package org.ck.webapp.alg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.BitSet;

/**
 * 采用位图法排序
 * 以增序形式输出经过排序的整数列表。
 */
public class BitSort1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("usage: java -Xms2M -Xmx2M com.ch1.BitSort1 d:\\input.txt d:\\output.txt");
			return;
		}

		try {
			BitSort1 client = new BitSort1();
			BufferedReader br = new BufferedReader(new FileReader(args[0]));
			PrintWriter pw = new PrintWriter(args[1]);
			long l1 = System.currentTimeMillis();
			client.input(br);
			client.output(pw);
			long l2 = System.currentTimeMillis();
			System.out.println("time:" + (l2 - l1));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	BitSet bs = new BitSet(10 ^ 7);

	void input(BufferedReader br) {
		String s = "";
		try {
			while ((s = br.readLine()) != null) {
				int in = Integer.parseInt(s);
				bs.set(in);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void output(PrintWriter pw) {
		int size = bs.size();
		for (int i = 0; i < size; i++) {
			if (bs.get(i)) {
				pw.println(i);
			}
		}
		pw.flush();
	}

}
