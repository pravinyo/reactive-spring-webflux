package com.reactivespring.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.reactivespring.domain.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient(timeout = "10000")
@AutoConfigureWireMock(port = 8084)// spin up a http server on port 8084
@TestPropertySource(
        properties = {
                "restClient.moviesInfoUrl=http://localhost:8084/v1/movieinfos",
                "restClient.moviesReviewUrl=http://localhost:8084/v1/reviews",
        }
)
public class MoviesControllerIntgTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void retrieveMovieById() {
        String movieId = "abc";

        stubFor(get(urlEqualTo("/v1/movieinfos/"+movieId))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("movieinfo.json")));

        stubFor(get(urlPathEqualTo("/v1/reviews"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("reviews.json")));

        webTestClient
                .get()
                .uri("/v1/movies/{id}", movieId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Movie.class)
                .consumeWith( movieEntityExchangeResult -> {
                    var movie = movieEntityExchangeResult.getResponseBody();
                    assert Objects.requireNonNull(movie).getReviewList().size() == 2;
                    assertEquals("Batman Begins", movie.getMovieInfo().getName());
                });
    }

    @Test
    void retrieveMovieById_movieInfo_notFound() {
        String movieId = "abc";

        stubFor(get(urlEqualTo("/v1/movieinfos/"+movieId))
                .willReturn(aResponse()
                        .withStatus(404)));

        stubFor(get(urlPathEqualTo("/v1/reviews"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("reviews.json")));

        webTestClient
                .get()
                .uri("/v1/movies/{id}", movieId)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(String.class)
                .isEqualTo("There is no movie present with Id:abc");

        WireMock.verify(1, getRequestedFor(urlEqualTo("/v1/movieinfos/"+movieId)));
    }

    @Test
    void retrieveMovieById_reviews_notFound() {
        String movieId = "abc";

        stubFor(get(urlEqualTo("/v1/movieinfos/"+movieId))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("movieinfo.json")));

        stubFor(get(urlPathEqualTo("/v1/reviews"))
                .willReturn(aResponse()
                        .withStatus(404)));

        webTestClient
                .get()
                .uri("/v1/movies/{id}", movieId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Movie.class)
                .consumeWith( movieEntityExchangeResult -> {
                    var movie = movieEntityExchangeResult.getResponseBody();
                    assert Objects.requireNonNull(movie).getReviewList().size() == 0;
                    assertEquals("Batman Begins", movie.getMovieInfo().getName());
                });
    }

    @Test
    void retrieveMovieById_5XX() {
        String movieId = "abc";

        stubFor(get(urlEqualTo("/v1/movieinfos/"+movieId))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withBody("MovieInfo Service Unavailable")));

        webTestClient
                .get()
                .uri("/v1/movies/{id}", movieId)
                .exchange()
                .expectStatus()
                .is5xxServerError()
                .expectBody(String.class)
                .isEqualTo("Server Exception in Movie Info Service: MovieInfo Service Unavailable");

        WireMock.verify(4, getRequestedFor(urlEqualTo("/v1/movieinfos/"+movieId)));
    }

    @Test
    void retrieveMovieById_reviews_5XX() {
        String movieId = "abc";
        stubFor(get(urlEqualTo("/v1/movieinfos/"+movieId))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("movieinfo.json")));

        stubFor(get(urlPathEqualTo("/v1/reviews"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withBody("Reviews Service Unavailable")));

        webTestClient
                .get()
                .uri("/v1/movies/{id}", movieId)
                .exchange()
                .expectStatus()
                .is5xxServerError()
                .expectBody(String.class)
                .isEqualTo("Server Exception in Movie Review Service: Reviews Service Unavailable");

        WireMock.verify(4, getRequestedFor(urlPathMatching("/v1/reviews*")));
    }
}
