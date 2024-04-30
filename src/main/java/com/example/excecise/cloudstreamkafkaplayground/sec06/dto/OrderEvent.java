package com.example.excecise.cloudstreamkafkaplayground.sec06.dto;


public record OrderEvent(int customerId,
                         int productId,
                         OrderType orderType) {
}
