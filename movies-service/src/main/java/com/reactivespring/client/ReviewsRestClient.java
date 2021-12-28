package com.reactivespring.client;

import com.reactivespring.domain.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

public class ReviewsRestClient {
    private final WebClient webClient;

    @Value("${restClient.moviesReviewUrl}")
    private String moviesReviewUrl;

    public ReviewsRestClient(WebClient wc) {
        this.webClient= wc;
    }

    public Flux<Review> retrieveMovieReviews(String movieId) {
        var url = UriComponentsBuilder.fromHttpUrl(moviesReviewUrl)
                .queryParam("movieInfoId", movieId)
                .buildAndExpand().toUriString();

        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Review.class)
                .log();
    }
}
