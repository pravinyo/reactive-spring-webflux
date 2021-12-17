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

    public Flux<String> namesFlux_flatMap(int stringLength) {
        return Flux.fromIterable(List.of("Pravin", "Ram", "Piyush", "Bhawna"))
                .map(String::toUpperCase)
                // PRAVIN, RAM -> P,R,A,V,I,N,R,A,M
                .filter(it -> it.length() > stringLength)
                .flatMap(this::splitString)
                .log();
    }

    public Flux<String> splitString(String name) {
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String> namesFlux_immutability() {
        var nameFlux =  Flux.fromIterable(List.of("Pravin", "Ram", "Piyush", "Bhawna"));
        nameFlux.map(String::toUpperCase);
        return nameFlux;
    }

    public Flux<String> namesFlux_filter(int stringLength) {
        return Flux.fromIterable(List.of("Pravin", "Ram", "Piyush", "Bhawna"))
                .map(String::toUpperCase)
                .filter(string -> string.length() > stringLength)
                .map(it -> it.length() +"-"+it)
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
