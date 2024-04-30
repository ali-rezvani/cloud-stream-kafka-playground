package com.example.excecise.cloudstreamkafkaplayground.sec08.processor;

import com.example.excecise.cloudstreamkafkaplayground.common.MessageConverter;
import com.example.excecise.cloudstreamkafkaplayground.sec08.dto.DigitalDelivery;
import com.example.excecise.cloudstreamkafkaplayground.sec08.dto.OrderEvent;
import com.example.excecise.cloudstreamkafkaplayground.sec08.dto.PhysicalDelivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
@Slf4j
public class FanOutProcessor {

/*    private static String DIGITAL_DELIVERY_CHANNEL="digital-delivery-out";
    private static String PHYSICAL_DELIVERY_CHANNEL="physical-delivery-out";*/

    private static String DIGITAL_DELIVERY_CHANNEL="digital-topic";
    private static String PHYSICAL_DELIVERY_CHANNEL="physical-topic";

    private final Consumer<OrderEvent> digitalSend=this::toDigitalDelivery;
    private final Consumer<OrderEvent> physicalSend=this::toPhysicalDelivery;
    private final Consumer<OrderEvent> fanOut=digitalSend.andThen(physicalSend);
    @Autowired
    private StreamBridge streamBridge;

    @Bean
    public Function<Flux<Message<OrderEvent>>, Mono<Void>> processor(){
        return flux->flux
                .map(MessageConverter::convertToRecord)
                .doOnNext(r->this.rout(r.message()))
                .doOnNext(r->r.acknowledgment().acknowledge())
                .then();
    }

    private void rout(OrderEvent event){
        switch (event.orderType()){
            case DIGITAL -> digitalSend.accept(event);
            case PHYSICAL -> fanOut.accept(event);
        }
    }

    private void toDigitalDelivery(OrderEvent event){
        var degitalDelivery=new DigitalDelivery(event.productId(),"%s@gmail.com".formatted(event.customerId()));
        this.streamBridge.send(DIGITAL_DELIVERY_CHANNEL,degitalDelivery);
    }

    private void toPhysicalDelivery(OrderEvent event){
        var physicalDelivery=new PhysicalDelivery(event.productId()
                ,"%s street".formatted(event.customerId())
                ,"%s city".formatted(event.customerId())
                ,"Iran");
        this.streamBridge.send(PHYSICAL_DELIVERY_CHANNEL,physicalDelivery);
    }
}
