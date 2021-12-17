package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

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
        // it should be used for synchronous operation and it cannot return reactive type stream
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

    @Test
    void namesFlux_flatMap_async() {
        // reactive stream sequence is reversed in delay case.
        // processing time is faster
        int stringLength = 3;
        var namesFluxMap = service.namesFlux_flatMap_async(stringLength);

        StepVerifier.create(namesFluxMap)
                .expectNext("P","R","A","V","I","N", "P","I","Y","U","S","H", "B","H","A","W","N","A")
                .verifyComplete();
    }

    @Test
    void namesFlux_concatMap() {
        // reactive stream sequence is preserved in delay case.
        // overall time it takes it more
        int stringLength = 3;
        var namesFluxMap = service.namesFlux_concatMap(stringLength);

        StepVerifier.create(namesFluxMap)
                .expectNext("P","R","A","V","I","N", "P","I","Y","U","S","H", "B","H","A","W","N","A")
                .verifyComplete();
    }

    @Test
    void nameMono_flatMap() {
        int stringLength = 3;
        var value = service.nameMono_flatMap(stringLength);

        StepVerifier.create(value)
                .expectNext(List.of("P", "R", "A", "V", "I", "N"))
                .verifyComplete();
    }

    @Test
    void nameMono_flatMapMany() {
        // to return flux from mono use flatmap many
        int stringLength = 3;
        var value = service.nameMono_flatMapMany(stringLength);

        StepVerifier.create(value)
                .expectNext("P", "R", "A", "V", "I", "N")
                .verifyComplete();
    }
}