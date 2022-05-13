package com.example.springwebfluxfnmodel;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class Subscriber {
    private final WebClient client;

    public Subscriber(WebClient.Builder builder) {
        this.client = builder.baseUrl("http://localhost:8080").build();
    }

    public Mono<String> subscribeGreeting() {
        return client.get().uri("publish/greeting").accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .map(s -> "Subscribe: " + s);
    }

    public Flux<String> fizzBuzz() {
        return client.get().uri("publish/numberstream").accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(Long.class)
                .map(aLong -> (aLong % 3 == 0 ? "Fizz" : "") + (aLong % 5 == 0 ? "Buzz" : aLong % 3 == 0 ? "" : aLong));
    }
}
