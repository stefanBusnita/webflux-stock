package com.stefan.webfluxstock.reactiveWeb;

import com.stefan.webfluxstock.model.Quote;
import com.stefan.webfluxstock.service.QuoteGeneratorService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Quote request handler
 */
@Component
public class QuoteHandler {

    private final QuoteGeneratorService generatorService;

    public QuoteHandler(QuoteGeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    /**
     * Fetch some quotes
     * @param request
     * @return
     */
    public Mono<ServerResponse> fetchQuotes(ServerRequest request){
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(this.generatorService.fetchQuoteStream(Duration.ofMillis(100L)).take(10), Quote.class);
    }

    /**
     * Return a stream of quotes
     * @param request
     * @return
     */
    public Mono<ServerResponse> streamQuotes(ServerRequest request){
        return ServerResponse.ok().contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(this.generatorService.fetchQuoteStream(Duration.ofMillis(100L)), Quote.class);
    }
}
