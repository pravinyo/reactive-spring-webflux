package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("Pravin", "Ram", "Piyush", "Bhawna"))
                .log();
    }

    public Flux<String> namesFlux_map() {
        return Flux.fromIterable(List.of("Pravin", "Ram", "Piyush", "Bhawna"))
                .map(String::toUpperCase)
                .log();
    }

    public Mono<String> nameMono() {
        return Mono.just("Pravin")
                .log();
    }

    public static void main(String[] args) {
        FluxAndMonoGeneratorService service = new FluxAndMonoGeneratorService();
        service.namesFlux()
                .subscribe(name -> System.out.println("Name is "+ name));

        service.nameMono()
                .subscribe(name  -> System.out.println("Mono name is "+ name));
    }
}
