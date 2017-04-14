package org.ck.webapp.springReactor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@EnableAutoConfiguration
public class SampleController {

	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "Hello World!";
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SampleController.class, "--debug");
	}

	private final MyReactiveLibrary reactiveLibrary;

	public ExampleController(@Autowired MyReactiveLibrary reactiveLibrary) {
	     this.reactiveLibrary = reactiveLibrary;
	  }

	@RequestMapping("hello/{who}")
	@ResponseBody
	public Mono<String> hello(@PathVariable String who) {
		return Mono.just(who).map(w -> "Hello " + w + "!");
	}

	@RequestMapping(value = "heyMister", method = RequestMethod.POST)
	@ResponseBody
	public Flux<String> hey(@RequestBody Mono<Sir> body) {
		return Mono.just("Hey mister ").concatWith(
				body.flatMap(sir -> Flux.fromArray(sir.getLastName().split(""))).map(String::toUpperCase).take(1))
				.concatWith(Mono.just(". how are you?"));
	}
}
