package com.example.springwebfluxfnmodel;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class Publisher {
    Mono<String> greeting() {
        return Mono.just("Hello, WebFlux!");
    }

    Flux<Long> numberStream() {
        return Flux.interval(Duration.ofMillis(500)); // 0.5秒毎に数値を0からカウントアップして返す
    }
}