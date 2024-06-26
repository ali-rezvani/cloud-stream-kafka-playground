package com.example.excecise.cloudstreamkafkaplayground.common;

import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import reactor.kafka.receiver.ReceiverOffset;

public class MessageConverter {
    public static <T> Record<T> convertToRecord(Message<T> message){
        var payload=message.getPayload();
        var key=message.getHeaders().get(KafkaHeaders.RECEIVED_KEY,String.class);
        var ack=message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, ReceiverOffset.class);
        return new Record<>(key,payload,ack);
    }
}
