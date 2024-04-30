package com.example.excecise.cloudstreamkafkaplayground.sec04;

import com.example.excecise.cloudstreamkafkaplayground.common.MessageConverter;
import com.example.excecise.cloudstreamkafkaplayground.common.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class KafkaConsumer {


    @Bean
    public Consumer<Flux<Message<String>>> consumer() {
        return flux -> flux
                .map(MessageConverter::convertToRecord)
                .doOnNext(this::printMessage)
                .subscribe();
    }

    private void printMessage(Record<String> record){
        log.info("payload: {}",record.message());
        log.info("Key: {}",record.key());
        log.info("Acknowledgment: {}",record.acknowledgment());
        record.acknowledgment().acknowledge();
    }
}
