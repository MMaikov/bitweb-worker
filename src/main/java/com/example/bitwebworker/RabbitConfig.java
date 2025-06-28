package com.example.bitwebworker;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue textQueue() {
        return new Queue("textQueue", true);  // durable queue
    }
}
