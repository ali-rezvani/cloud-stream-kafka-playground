package com.example.excecise.cloudstreamkafkaplayground.sec08.dto;

public record OrderEvent(int customerId,
                         int productId,
                         OrderType orderType) {
}
