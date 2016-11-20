package webapp.metrics;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.ck.webapp.metrics.MetricsUtils;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.SlidingTimeWindowReservoir;

public class MetricsTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Random random = new Random();
		Histogram histogram = MetricsUtils.histogram("timeCostAvg");

		SlidingTimeWindowReservoir reservoir = new SlidingTimeWindowReservoir(1, TimeUnit.MINUTES);
		Histogram histogram1 = new Histogram(reservoir);
		MetricsUtils.metric("timeCostAvgMinute", histogram1);
		int i = 1;
		while (true) {
			long start = System.currentTimeMillis();
			try {
				Thread.sleep(random.nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
			long end = System.currentTimeMillis();
			histogram.update(i);
			histogram1.update(i);
		}

	}

}
