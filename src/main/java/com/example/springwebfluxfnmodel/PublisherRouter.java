package com.example.springwebfluxfnmodel;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
public class PublisherRouter {
    @Bean
    public RouterFunction<ServerResponse> publisherRoute(PublisherHandler publisherHandler) {
        return RouterFunctions.route()
                .path("/publish", builder -> builder
                        .GET("/greeting", publisherHandler::greeting)
                        .GET("/numberstream", publisherHandler::numberstream)
                )
                .build();
    }
}
