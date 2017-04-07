package org.ck.webapp.threadPool.forkjoin;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

public class CompletableFutureTest {

	public static void main(String[] args) throws Exception {
		Random rand = new Random();
		CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(10000 + rand.nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "100";
		});
		CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(10000 + rand.nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "abc";
		});
		// CompletableFuture<Void> f = CompletableFuture.allOf(future1,future2);
		// CompletableFuture<Object> f = CompletableFuture.anyOf(future1,
		// future2);

		// System.out.println(f.get());

		System.out.println(sequence(Lists.newArrayList(future1, future2)).get());

	}

	public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
		CompletableFuture<Void> allDoneFuture = CompletableFuture
				.allOf(futures.toArray(new CompletableFuture[futures.size()]));
		return allDoneFuture
				.thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.<T>toList()));
	}

	public static <T> CompletableFuture<List<T>> sequence(Stream<CompletableFuture<T>> futures) {
		List<CompletableFuture<T>> futureList = futures.filter(f -> f != null).collect(Collectors.toList());
		return sequence(futureList);
	}
}
