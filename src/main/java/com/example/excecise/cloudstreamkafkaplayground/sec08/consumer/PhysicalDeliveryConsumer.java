package com.example.excecise.cloudstreamkafkaplayground.sec08.consumer;

import com.example.excecise.cloudstreamkafkaplayground.common.MessageConverter;
import com.example.excecise.cloudstreamkafkaplayground.common.Record;
import com.example.excecise.cloudstreamkafkaplayground.sec08.dto.PhysicalDelivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
@Slf4j
public class PhysicalDeliveryConsumer {

    @Bean
    public Function<Flux<Message<PhysicalDelivery>>, Mono<Void>> physicalDelivery(){
        return flux->flux
                .map(MessageConverter::convertToRecord)
                .doOnNext(this::printDetails)
                .then();
    }

    private void printDetails(Record<PhysicalDelivery> record) {
        log.info("Physical consumer:{}",record.message());
        record.acknowledgment().acknowledge();
    }

}
