package com.example.excecise.cloudstreamkafkaplayground.sec03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
@Slf4j
public class KafkaProcessor {

    @Bean
    public Function<Flux<String>,Flux<String>> processor(){
        return flux->flux
                .doOnNext(m->log.info("processor message received {}",m))
                .concatMap(this::process)
                .doOnNext(m->log.info("after process {}",m));
    }

    private Mono<String> process(String input){
        return Mono
                .just(input)
                .map(String::toUpperCase)
                ;
    }
}
