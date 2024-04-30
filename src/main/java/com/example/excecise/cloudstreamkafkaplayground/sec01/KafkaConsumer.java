package com.example.excecise.cloudstreamkafkaplayground.sec01;

import kafka.tools.ConsoleConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.cloud.stream.binder.reactorkafka.ReceiverOptionsCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Configuration
public class KafkaConsumer {

   /* @Bean
    public ReceiverOptionsCustomizer<String, String> customizer() {
        return (s, ro) -> {
            log.info("**************************{}****",s);
            return ro
                    .consumerProperty(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, "234");
        };
    }*/

    @Bean
    public Consumer<Flux<String>> consumer() {
        return flux -> flux
                .doOnNext(s -> log.info("consumer received:{}", s))
                .subscribe();
    }

    @Bean
    public Function<Flux<String>, Mono<Void>> function() {
        return flux -> flux
                .doOnNext(s -> log.info("function received:{}", s))
                .then();
    }
}
