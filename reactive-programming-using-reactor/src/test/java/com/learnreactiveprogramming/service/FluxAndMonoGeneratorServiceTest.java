package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoGeneratorServiceTest {
    FluxAndMonoGeneratorService service = new FluxAndMonoGeneratorService();

    @Test
    void test_namesFlux() {
        var namesFlux = service.namesFlux();

        StepVerifier.create(namesFlux)
                .expectNext("Pravin", "Ram")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void namesFlux_map() {
        var namesFluxMap = service.namesFlux_map();

        StepVerifier.create(namesFluxMap)
                .expectNext("PRAVIN", "RAM" , "PIYUSH", "BHAWNA")
                .verifyComplete();
    }
}