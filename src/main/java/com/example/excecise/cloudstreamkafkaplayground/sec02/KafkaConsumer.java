package com.example.excecise.cloudstreamkafkaplayground.sec02;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Configuration
public class KafkaConsumer {


    @Bean
    public Consumer<Flux<String>> consumer() {
        return flux -> flux
                .doOnNext(s -> log.info("consumer received:{}", s))
                .subscribe();
    }


}
