package com.example.excecise.cloudstreamkafkaplayground.sec02;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.cloud.stream.binder.reactorkafka.SenderOptionsCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.function.Supplier;

@Configuration
@Slf4j
public class KafkaProducer {

    @Bean
    public SenderOptionsCustomizer<String,String> customizer(){
        return (s,so)->so
                .producerProperty(ProducerConfig.ACKS_CONFIG,"all")
                .producerProperty(ProducerConfig.BATCH_SIZE_CONFIG,"2001");
    }
    @Bean
    public Supplier<Flux<String>> producer(){
        return ()->Flux
                .interval(Duration.ofSeconds(1))
                .take(10)
                .map(i->"msg"+i)
                .doOnNext(m->log.info("producer {}",m));
    }
}
