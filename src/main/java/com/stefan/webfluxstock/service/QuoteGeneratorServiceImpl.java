package com.stefan.webfluxstock.service;

import com.stefan.webfluxstock.model.Quote;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

@Service
public class QuoteGeneratorServiceImpl implements QuoteGeneratorService{

    private final MathContext mathContext= new MathContext(2);
    private final List<Quote> prices = new ArrayList<>();
    private final Random random = new Random();

    public QuoteGeneratorServiceImpl(){
        this.prices.add(new Quote("AAPL", 160.16));
        this.prices.add(new Quote("MSFT", 190.76));
        this.prices.add(new Quote("GOOG", 153.26));
        this.prices.add(new Quote("ORCL", 847.24));
        this.prices.add(new Quote("IBM", 159.34));
        this.prices.add(new Quote("INTC", 39.29));
        this.prices.add(new Quote("RHT", 84.29));
        this.prices.add(new Quote("VMW", 92.21));
    }


    @Override
    public Flux<Quote> fetchQuoteStream(Duration period) {
        return  Flux.generate(()->0,(BiFunction<Integer,SynchronousSink<Quote>,Integer>)(index,sink)->{
          Quote updatedQuote = updateQuote(this.prices.get(index));
          sink.next(updatedQuote);
            System.out.println(++index % this.prices.size());
          return ++index % this.prices.size();
        })
                //Emit them with a specific period, therefore zip the Flux with a Flux.interval
                .zipWith(Flux.interval(period)).map(t-> t.getT1())
                //Values are generated in batches, we need to set timestamp after creation
                .map(quote -> {
                    quote.setInstant(Instant.now());
                    return quote;
                }).log("Fetched");
    }

    /**
     * Will update the price of a quote randomly as necessary
     * @param quote
     * @return
     */
    private Quote updateQuote(Quote quote){
        BigDecimal newPrice = quote.getPrice().multiply(new BigDecimal(0.5 * this.random.nextDouble(), mathContext));
        return new Quote(quote.getTicker(), quote.getPrice().add(newPrice));
    }
}
