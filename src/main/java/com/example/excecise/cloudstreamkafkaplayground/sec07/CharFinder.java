package com.example.excecise.cloudstreamkafkaplayground.sec07;

import com.example.excecise.cloudstreamkafkaplayground.common.MessageConverter;
import com.example.excecise.cloudstreamkafkaplayground.common.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Slf4j
@Configuration
public class CharFinder {

    private static String DLT_TOPIC = "dlt-topic";
    @Autowired
    private StreamBridge streamBridge;

    @Bean
    public Function<Flux<Message<String>>, Flux<Character>> processor() {
        return flux -> flux
                .map(MessageConverter::convertToRecord)
                .concatMap(r->this.find(r.message())
                        .onErrorResume(ex->Mono.fromRunnable(()->this.handleError(ex,r)))
                        .doAfterTerminate(()->r.acknowledgment().acknowledge())
                );
    }

    private Mono<Character> find(String message) {
        return Mono
                .just(message)
                .map(m -> m.charAt(3));
    }

    private void handleError(Throwable ex, Record<String> record) {
        log.error(ex.getMessage());

        this.streamBridge.send(
                DLT_TOPIC,
                MessageBuilder
                        .withPayload(record.message())
                        .setHeader(KafkaHeaders.KEY, record.key())
        );
    }
}
