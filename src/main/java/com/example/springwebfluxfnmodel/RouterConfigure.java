package com.example.springwebfluxfnmodel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfigure {
    @Bean
    public RouterFunction<ServerResponse> appRoute(Publisher publisher, Subscriber subscriber) {
        return RouterFunctions.route()
                .path("/publish2", builder -> builder
                        .GET("/greeting", request -> ServerResponse
                                .ok()
                                .body(publisher.greeting(), String.class))
                        .GET("/numberstream", request -> ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_NDJSON)
                                .body(publisher.numberStream(), Long.class))
                )
                .path("/subscribe2", builder -> builder
                        .GET("/greeting", request -> ServerResponse
                                .ok()
                                .body(subscriber.subscribeGreeting(), String.class))
                        .GET("/fizzbuzz", request -> ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_NDJSON)
                                .body(subscriber.fizzBuzz(), Long.class))
                )
                .build();
    }
}
