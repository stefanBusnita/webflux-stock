package com.stefan.webfluxstock.services;

import com.stefan.webfluxstock.model.Quote;
import com.stefan.webfluxstock.service.QuoteGeneratorService;
import com.stefan.webfluxstock.service.QuoteGeneratorServiceImpl;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class QuoteGeneratorServiceTest {

    QuoteGeneratorService quoteGeneratorService = new QuoteGeneratorServiceImpl();

    @Before
    public void setUp() {}

    // Runs too fast, can't properly track anything
    @Test
    public void fetchQuoteStream() {
        Flux<Quote> quoteFlux = quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(100L));

        quoteFlux.take(10).subscribe(System.out::println);
    }

    //Another way of running it, will be able to see something on screen this time
    @Test
    public void fetchQuoteStreamCountDown() throws InterruptedException {

        //get quoteFlux of quotes
        Flux<Quote> quoteFlux = quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(10L));

        //subscriber lambda
        Consumer<Quote> consumer = System.out::println;

        //error handler
        Consumer<Throwable> errorHandler = e -> System.out.println("Some Error Occured");

        //set countdown to 1
        CountDownLatch countDownLatch = new CountDownLatch(1);

        //called upon complete
        Runnable allDone = ()-> countDownLatch.countDown();

        quoteFlux.take(10).subscribe(consumer,errorHandler,allDone);

        countDownLatch.await();
    }

}
