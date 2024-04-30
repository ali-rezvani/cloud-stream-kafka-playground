package com.example.excecise.cloudstreamkafkaplayground.sec04;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.function.Supplier;

@Configuration
@Slf4j
public class KafkaProducer {

    /*@Bean
    public SenderOptionsCustomizer<String,String> customizer(){
        return (s,so)->so
                .producerProperty(ProducerConfig.ACKS_CONFIG,"all")
                .producerProperty(ProducerConfig.BATCH_SIZE_CONFIG,"2001");
    }*/

    @Bean
    public Supplier<Flux<Message<String>>> producer(){
        return ()->Flux
                .interval(Duration.ofSeconds(1))
                .take(10)
                .map(this::toMessage)
                .doOnNext(m->log.info("producer {}",m));
    }

    private Message<String> toMessage(Long i){
        return MessageBuilder.withPayload("msg"+i)
//                .setHeader("kafka_messageKey","key-"+i)
                .setHeader(KafkaHeaders.KEY,"key-"+i)
                .build();
    }
}
