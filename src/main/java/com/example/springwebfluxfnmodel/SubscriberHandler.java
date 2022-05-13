package com.example.springwebfluxfnmodel;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class SubscriberHandler {
    private final Subscriber subscriber;

    public SubscriberHandler(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public Mono<ServerResponse> greeting(ServerRequest request) {
        return ServerResponse
                .ok()
                .body(subscriber.subscribeGreeting(), String.class);
    }

    public Mono<ServerResponse> fizzbuzz(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(subscriber.fizzBuzz(), Long.class);
    }
}
