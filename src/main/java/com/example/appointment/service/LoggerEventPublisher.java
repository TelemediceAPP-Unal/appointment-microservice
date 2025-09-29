package com.example.appointment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class LoggerEventPublisher implements EventPublisher {
    private static final Logger log = LoggerFactory.getLogger(LoggerEventPublisher.class);
    @Override
    public void publish(String type, String payloadJson) {
        // TODO: Replace with Pub/Sub publisher (GCP)
        log.info("SEND EVENT -> type={} payload={}", type, payloadJson);
    }
}
