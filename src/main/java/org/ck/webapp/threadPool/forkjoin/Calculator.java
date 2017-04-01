package org.ck.webapp.threadPool.forkjoin;

import org.apache.commons.math3.linear.RealVector;

public interface Calculator {
	double[] sumUp(RealVector vector1, RealVector[] vectors);
}
