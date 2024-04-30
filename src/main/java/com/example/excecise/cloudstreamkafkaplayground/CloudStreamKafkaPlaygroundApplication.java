package com.example.excecise.cloudstreamkafkaplayground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.excecise.cloudstreamkafkaplayground.${sec}")
public class CloudStreamKafkaPlaygroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudStreamKafkaPlaygroundApplication.class, args);
	}

}
