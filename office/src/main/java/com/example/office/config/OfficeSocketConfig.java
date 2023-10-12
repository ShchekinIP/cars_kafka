package com.example.office.config;

import com.example.common.processor.MessageConverter;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class OfficeSocketConfig implements WebSocketConfigurer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Cache<String, WebSocketSession> sessionCache;
    private final MessageConverter messageConverter;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(new OfficeSocketHandler(kafkaTemplate, sessionCache, messageConverter), "/websocket")
                .setAllowedOrigins("*");

    }
}
