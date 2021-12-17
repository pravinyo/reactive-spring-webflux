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

    @Test
    void namesFlux_immutability() {
        var namesFluxMap = service.namesFlux_immutability();

        StepVerifier.create(namesFluxMap)
                .expectNext("Pravin", "Ram")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void namesFlux_filter() {
        int stringLength = 3;
        var namesFluxMap = service.namesFlux_filter(stringLength);

        StepVerifier.create(namesFluxMap)
                .expectNext("6-PRAVIN", "6-PIYUSH", "6-BHAWNA")
                .verifyComplete();
    }

    @Test
    void namesFlux_flatMap() {
        int stringLength = 3;
        var namesFluxMap = service.namesFlux_flatMap(stringLength);

        StepVerifier.create(namesFluxMap)
                .expectNext("P","R","A","V","I","N", "P","I","Y","U","S","H", "B","H","A","W","N","A")
                .verifyComplete();
    }
}