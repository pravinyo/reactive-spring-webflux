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

    @Test
    void namesFlux_transform() {
        int stringLength = 3;
        var namesFluxMap = service.namesFlux_transform(stringLength);

        StepVerifier.create(namesFluxMap)
                .expectNext("P","R","A","V","I","N", "P","I","Y","U","S","H", "B","H","A","W","N","A")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_1() {
        int stringLength = 7;
        var namesFluxMap = service.namesFlux_transform(stringLength);

        StepVerifier.create(namesFluxMap)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_switchIfEmpty() {
        int stringLength = 6;
        var namesFluxMap = service.namesFlux_transform_switchIfEmpty(stringLength);

        StepVerifier.create(namesFluxMap)
                .expectNext("D","E","F","A","U","L","T")
                .verifyComplete();
    }

    @Test
    void explore_concat() {
        // subscription happens in sequence
        var concatFlux = service.explore_concat();

        StepVerifier.create(concatFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_concatWith() {
        // subscription happens in sequence
        var concatFlux = service.explore_concatWith();

        StepVerifier.create(concatFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void explore_concatWith_2() {
        // subscription happens in sequence
        var concatFlux = service.explore_concatWith_Mono();

        StepVerifier.create(concatFlux)
                .expectNext("A", "B", "C")
                .verifyComplete();
    }

    @Test
    void explore_merge() {
        // subscription happens in interleaved fashion and same time
        var mergeFlux = service.explore_merge();

        StepVerifier.create(mergeFlux)
                .expectNext("A","D", "B","E", "C", "F")
                .verifyComplete();
    }

    @Test
    void explore_mergeWith() {
        // subscription happens in interleaved fashion and same time
        var mergeFlux = service.explore_mergeWith();

        StepVerifier.create(mergeFlux)
                .expectNext("A","D", "B","E", "C", "F")
                .verifyComplete();
    }

    @Test
    void explore_mergeWith_Mono() {
        // subscription happens in interleaved fashion and same time eagerly
        var mergeFlux = service.explore_mergeWith_Mono();

        StepVerifier.create(mergeFlux)
                .expectNext("A", "B", "C")
                .verifyComplete();
    }

    @Test
    void explore_mergeSequential() {
        // flux are subscribed together but merged in sequence
        var merge = service.explore_mergeSequential();
        StepVerifier.create(merge)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }
}