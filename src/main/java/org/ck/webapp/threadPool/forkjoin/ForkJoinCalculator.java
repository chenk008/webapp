package org.ck.webapp.threadPool.forkjoin;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import com.google.common.collect.Lists;

public class ForkJoinCalculator implements Calculator {
	private ForkJoinPool pool;

	private static class SumTask extends RecursiveTask<double[]> {
		private RealVector vector1;
		private RealVector[] vectors;
		private int startIndex;
		private int endIndex;

		public SumTask(RealVector vector1, RealVector[] vectors, int startIndex, int endIndex) {
			this.vector1 = vector1;
			this.vectors = vectors;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
		}

		@Override
		protected double[] compute() {
			// TODO Auto-generated method stub
			if (this.endIndex - this.startIndex <= 1000) {
				double[] result = new double[this.endIndex - this.startIndex];
				for (int i = this.startIndex, j = 0; i < this.endIndex; i++, j++) {
					result[j] = vector1.cosine(vectors[i]);
				}
				return null;
			} else {
				List<SumTask> subTask = Lists.newArrayList();
				for (int i = startIndex; i < this.endIndex; i += 1000) {
					SumTask taskLeft = new SumTask(vector1, vectors, i, i += 1000);
					taskLeft.fork();
					subTask.add(taskLeft);
				}
				for (SumTask task : subTask) {
					task.join();
				}
				return null;
			}
		}

		// @Override
		// protected Long compute() {
		// // 当需要计算的数字小于6时，直接计算结果
		// if (to - from < 6) {
		// long total = 0;
		// for (int i = from; i <= to; i++) {
		// total += numbers[i];
		// }
		// return total;
		// // 否则，把任务一分为二，递归计算
		// } else {
		// int middle = (from + to) / 2;
		// SumTask taskLeft = new SumTask(numbers, from, middle);
		// SumTask taskRight = new SumTask(numbers, middle + 1, to);
		// taskLeft.fork();
		// taskRight.fork();
		// return taskLeft.join() + taskRight.join();
		// }
		// }
	}

	public ForkJoinCalculator() {
		// 也可以使用公用的 ForkJoinPool：
		// pool = ForkJoinPool.commonPool()
		pool = new ForkJoinPool();
	}

	@Override
	public double[] sumUp(RealVector vector1, RealVector[] vectors) {
		return pool.invoke(new SumTask(vector1, vectors, 0, 100000));
	}

	public void shutdown() {
		pool.shutdown();
	}
}
