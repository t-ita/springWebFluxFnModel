package com.example.springwebfluxfnmodel;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
public class SubscriberRouter {
    @Bean
    public RouterFunction<ServerResponse> subscriberRoute(SubscriberHandler subscriberHandler) {
        return RouterFunctions.route()
                .path("/subscribe", builder -> builder
                        .GET("/greeting", subscriberHandler::greeting)
                        .GET("/fizzbuzz", subscriberHandler::fizzbuzz)
                )
                .build();
    }
}
