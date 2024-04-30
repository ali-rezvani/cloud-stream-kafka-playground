package com.example.excecise.cloudstreamkafkaplayground.sec09.processor;

import com.example.excecise.cloudstreamkafkaplayground.common.MessageConverter;
import com.example.excecise.cloudstreamkafkaplayground.sec09.dto.DigitalDelivery;
import com.example.excecise.cloudstreamkafkaplayground.sec09.dto.OrderEvent;
import com.example.excecise.cloudstreamkafkaplayground.sec09.dto.OrderType;
import com.example.excecise.cloudstreamkafkaplayground.sec09.dto.PhysicalDelivery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
@Slf4j
public class FanOutProcessor {

    private final Sinks.Many<OrderEvent> sink=Sinks.many().multicast().onBackpressureBuffer();
    @Bean
    public Function<Flux<Message<OrderEvent>>, Tuple2<Flux<DigitalDelivery>,Flux<PhysicalDelivery>>> processor(){
        return flux->{flux
                .map(MessageConverter::convertToRecord)
                .doOnNext(r->this.sink.tryEmitNext(r.message()))
                .doOnNext(r->r.acknowledgment().acknowledge())
                .subscribe();
            return Tuples.of(
                    this.sink.asFlux().transform(toDigitalDelivery()),
                    this.sink.asFlux().filter(oe->OrderType.PHYSICAL.equals(oe.orderType()))
                            .transform(toPhysicalDelivery())
            );
        };
    }

    private Function<Flux<OrderEvent>,Flux<DigitalDelivery>> toDigitalDelivery(){
        return flux->flux
                .map(e->new DigitalDelivery(e.productId(),"%s@gmail.com".formatted(e.customerId())));
    }

    private Function<Flux<OrderEvent>,Flux<PhysicalDelivery>> toPhysicalDelivery(){
        return flux->flux
                .map(e->new PhysicalDelivery(e.productId()
                ,"%s street".formatted(e.customerId())
                ,"%s city".formatted(e.customerId())
                ,"Iran"));
    }
}
