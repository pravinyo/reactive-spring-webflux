package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

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

    public Flux<String> namesFlux_flatMap_async(int stringLength) {
        return Flux.fromIterable(List.of("Pravin", "Ram", "Piyush", "Bhawna"))
                .map(String::toUpperCase)
                // PRAVIN, RAM -> P,R,A,V,I,N,R,A,M
                .filter(it -> it.length() > stringLength)
                .flatMap(this::splitStringWithDelay)
                .log();
    }

    public Flux<String> namesFlux_concatMap(int stringLength) {
        return Flux.fromIterable(List.of("Pravin", "Ram", "Piyush", "Bhawna"))
                .map(String::toUpperCase)
                // PRAVIN, RAM -> P,R,A,V,I,N,R,A,M
                .filter(it -> it.length() > stringLength)
                .concatMap(this::splitStringWithDelay)
                .log();
    }

    public Flux<String> splitString(String name) {
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String> splitStringWithDelay(String name) {
        var charArray = name.split("");
        var randomDelay = new Random().nextInt(1000);
        return Flux.fromArray(charArray)
                .delayElements(Duration.ofMillis(randomDelay));
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

    public Flux<String> namesFlux_transform(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name ->
                name.map(String::toUpperCase)
                .filter(string -> string.length() > stringLength);

        return Flux.fromIterable(List.of("Pravin", "Ram", "Piyush", "Bhawna"))
                .transform(filterMap)
                .flatMap(this::splitString)
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> namesFlux_transform_switchIfEmpty(int stringLength) {

        Function<Flux<String>, Flux<String>> filterMap = name ->
                name.map(String::toUpperCase)
                        .filter(string -> string.length() > stringLength)
                        .flatMap(this::splitString);

        var defaultFlux = Flux.just("default")
                .transform(filterMap);

        return Flux.fromIterable(List.of("Pravin", "Ram", "Piyush", "Bhawna"))
                .transform(filterMap)
                .switchIfEmpty(defaultFlux)
                .log();
    }

    public Mono<String> nameMono() {
        return Mono.just("Pravin")
                .log();
    }

    public Mono<List<String>> nameMono_flatMap(int stringLength) {
        return Mono.just("Pravin")
                .map(String::toUpperCase)
                .filter(it -> it.length()>stringLength)
                .flatMap(this::splitStringMono)
                .log();
    }

    public Flux<String> nameMono_flatMapMany(int stringLength) {
        return Mono.just("Pravin")
                .map(String::toUpperCase)
                .filter(it -> it.length()>stringLength)
                .flatMapMany(this::splitString)
                .log();
    }

    private Mono<List<String>> splitStringMono(String s) {
        var charArray = s.split("");
        var list = List.of(charArray);
        return Mono.just(list);
    }

    public Flux<String> explore_concat() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        return Flux.concat(abcFlux, defFlux).log();
    }

    public Flux<String> explore_concatWith() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        return abcFlux.concatWith(defFlux).log();
    }

    public Flux<String> explore_concatWith_Mono() {
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");
        var cMono = Mono.just("C");
//        return aMono.concatWith(bMono).concatWith(cMono).log();
        return aMono.concatWith(bMono.concatWith(cMono)).log();
    }

    public Flux<String> explore_merge() {
        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));
        return Flux.merge(abcFlux, defFlux).log();
    }

    public Flux<String> explore_mergeWith() {
        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));
        return abcFlux.mergeWith(defFlux).log();
    }

    public Flux<String> explore_mergeWith_Mono() {
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");
        var cMono = Mono.just("C");
        return aMono.mergeWith(bMono.mergeWith(cMono)).log();
    }

    public static void main(String[] args) {
        FluxAndMonoGeneratorService service = new FluxAndMonoGeneratorService();
        service.namesFlux()
                .subscribe(name -> System.out.println("Name is "+ name));

        service.nameMono()
                .subscribe(name  -> System.out.println("Mono name is "+ name));
    }
}
