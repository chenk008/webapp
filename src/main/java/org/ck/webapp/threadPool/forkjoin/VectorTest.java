package org.ck.webapp.threadPool.forkjoin;

import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(value = Scope.Thread)
public class VectorTest {

	ForkJoinCalculator forkJoinCalculator;
	RealVector vector1 = new ArrayRealVector(new double[] { 1, 2, 3, 4, 6, 5, 4, 2, 1, 3, 2, 5, 1, 3, 2, 5 });
	RealVector vector2 = new ArrayRealVector(new double[] { 2, 3, 4, 4, 1, 2, 3, 4, 2, 3, 4, 4, 2, 3, 4, 4 });
	RealVector[] vectors = new RealVector[100000];

	@Setup
	public void prepare() {
		for (int i = 0; i < 100000; i++) {
			vectors[i] = new ArrayRealVector(
					new double[] { 1 + i, 2, 3, 4, 6 + i, 5, 4, 2 + i, 1, 3, 2 + i, 5, 1, 3, 2 + i, 5 });
		}
		forkJoinCalculator = new ForkJoinCalculator();
	}

	@TearDown
	public void shutdown() {
		forkJoinCalculator.shutdown();
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	public void test() {

		vector1.cosine(vector2);
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	public void test1() {
		forkJoinCalculator.sumUp(vector1, vectors);
	}

	public static void main(String[] args) throws RunnerException {

		Options opt = new OptionsBuilder().include(VectorTest.class.getSimpleName()).forks(1).build();

		new Runner(opt).run();
	}

}
