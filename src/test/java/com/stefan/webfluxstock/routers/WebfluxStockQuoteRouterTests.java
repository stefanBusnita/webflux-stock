package com.stefan.webfluxstock.routers;

import com.stefan.webfluxstock.model.Quote;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.concurrent.CountDownLatch;


/**
 * Test used for quotes router
 * json and stream
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebfluxStockQuoteRouterTests {

    @Autowired
    private WebTestClient webTestClient;

    /**
     * Test returning the application_json
     */
    @Test
    public void testFetchQuotes(){
        webTestClient.get()
                     .uri("/quotes?size=20")
                     .accept(MediaType.APPLICATION_JSON).exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBodyList(Quote.class).hasSize(20)
                    .consumeWith(allQuotes -> {
                        Assertions.assertThat(allQuotes.getResponseBody()).allSatisfy(
                                quote -> {
                                    Assertions.assertThat(quote.getPrice()).isPositive();
                                }
                        );
                        Assertions.assertThat(allQuotes.getResponseBody()).hasSize(20);
                    });
    }

    /**
     * Test application_stream_json
     * @throws Exception
     */
    @Test
    public void testStreamingQuotes() throws Exception{

        CountDownLatch countDownLatch = new CountDownLatch(10);

            webTestClient.get()
                    .uri("/quotes")
                    .accept(MediaType.APPLICATION_STREAM_JSON)
                    .exchange()
                    .returnResult(Quote.class)
                    .getResponseBody()
                    .take(10)
                    .subscribe(quote -> {
                        System.out.println(quote);

                        countDownLatch.countDown();
                    });

            countDownLatch.await();
    }

}
