package com.stefan.webfluxstock.service;

import com.stefan.webfluxstock.model.Quote;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 *  Quote generator service
 */
public interface QuoteGeneratorService {
   Flux<Quote> fetchQuoteStream(Duration period);
}
