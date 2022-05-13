package com.example.springwebfluxfnmodel;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class PublisherHandler {
    private final Publisher publisher;

    public PublisherHandler(Publisher publisher) {
        this.publisher = publisher;
    }

    public Mono<ServerResponse> greeting(ServerRequest request) {
        return ServerResponse
                .ok()
                .body(publisher.greeting(), String.class);
    }

    public Mono<ServerResponse> numberstream(ServerRequest request) {
        var flux = Flux.interval(Duration.ofMillis(500)); // 0.5秒毎に数値を0からカウントアップして返す
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(publisher.numberStream(), Long.class);
    }
}
