package com.example.excecise.cloudstreamkafkaplayground.sec05.dto;

public record OrderEvent(int customerId,
                         int productId,
                         OrderType orderType) {
}
