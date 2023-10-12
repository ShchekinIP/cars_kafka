package com.example.car.config;

import com.example.common.processor.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagesConfig {
    @Bean
    public MessageConverter messageConverter() {
        return new MessageConverter();
    }
}
