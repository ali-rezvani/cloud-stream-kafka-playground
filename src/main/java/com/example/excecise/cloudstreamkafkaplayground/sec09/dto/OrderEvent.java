package com.example.excecise.cloudstreamkafkaplayground.sec09.dto;

public record OrderEvent(int customerId,
                         int productId,
                         OrderType orderType) {
}
