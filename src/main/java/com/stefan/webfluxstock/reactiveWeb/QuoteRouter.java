package com.stefan.webfluxstock.reactiveWeb;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 *
 */
@Configuration
public class QuoteRouter {


    /**
     * Use functional composition and create the routes for
     * returning quotes
     * streaming quotes
     * @param handler
     * @return
     */
    public RouterFunction<ServerResponse> router(QuoteHandler handler){
        return RouterFunctions.route(
                RequestPredicates.GET("/quotes").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),handler::fetchQuotes)
                .andRoute(RequestPredicates.GET("/quotes").and(RequestPredicates.accept(MediaType.APPLICATION_STREAM_JSON)), handler::streamQuotes);
    }

}
