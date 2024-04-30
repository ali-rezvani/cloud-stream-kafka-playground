package com.example.excecise.cloudstreamkafkaplayground.sec06.processor;

import com.example.excecise.cloudstreamkafkaplayground.common.MessageConverter;
import com.example.excecise.cloudstreamkafkaplayground.common.Record;
import com.example.excecise.cloudstreamkafkaplayground.sec05.dto.PhysicalDelivery;
import com.example.excecise.cloudstreamkafkaplayground.sec06.dto.DigitalDelivery;
import com.example.excecise.cloudstreamkafkaplayground.sec06.dto.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
@Slf4j
public class OrderRouter {

    private static String DESTINATION_HEADER="spring.cloud.stream.sendto.destination";
    private static String DIGITAL_DELIVERY_CHANNEL="digital-delivery-out";
    private static String PHYSICAL_DELIVERY_CHANNEL="physical-delivery-out";
    public Function<Flux<Message<OrderEvent>>, Flux<Message<?>>> processor(){
        return flux->flux
                .map(MessageConverter::convertToRecord)
                .map(this::route);

    }

    private Message<?> route(Record<OrderEvent> record) {
        var message=switch (record.message().orderType()){
            case DIGITAL -> toDigitalDelivery(record.message());
            case PHYSICAL -> toPhysicalDelivery(record.message());
        };
        record.acknowledgment().acknowledge();
        return message;
    }

    private Message<PhysicalDelivery> toPhysicalDelivery(OrderEvent event) {
        var physicalDelivery=new PhysicalDelivery(event.productId()
                ,"%s street".formatted(event.customerId())
                ,"%s city".formatted(event.customerId())
                ,"Iran");
        return MessageBuilder
                .withPayload(physicalDelivery)
                .setHeader(DESTINATION_HEADER,PHYSICAL_DELIVERY_CHANNEL)
                .build();
    }

    private Message<DigitalDelivery> toDigitalDelivery(OrderEvent event) {
        var digitalDelivery=new DigitalDelivery(event.productId(),"%s.gmail.com".formatted(event.customerId()));
        return MessageBuilder
                .withPayload(digitalDelivery)
                .setHeader(DESTINATION_HEADER,DIGITAL_DELIVERY_CHANNEL)
                .build();
    }

}
