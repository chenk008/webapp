package org.ck.webapp.springReactor1;

import java.time.Duration;

import javax.annotation.PreDestroy;

import org.ck.webapp.springReactor.MyReactiveLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MyReactiveLibraryTest {

	@Autowired
	private MyReactiveLibrary myReactiveLibrary;

	public Flux<String> alphabet5(char from) {
		return Flux.range((int) from, 5).map(i -> "" + (char) i.intValue());
	}

	public Mono<String> withDelay(String value, int delaySeconds) {
		return Mono.just(value).delaySubscription(Duration.ofSeconds(delaySeconds));
	}

	public void say() {
		System.out.println(myReactiveLibrary.toString());
	}

	@PreDestroy
	public void beanDestroy() {
		System.out.println("close MyReactiveLibraryTest");
	}
}
