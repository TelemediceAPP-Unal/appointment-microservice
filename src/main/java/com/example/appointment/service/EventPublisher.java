package com.example.appointment.service;

public interface EventPublisher {
    void publish(String type, String payloadJson);
}
