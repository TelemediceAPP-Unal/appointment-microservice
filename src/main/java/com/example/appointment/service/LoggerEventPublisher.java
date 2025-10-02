package com.example.appointment.service;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Primary
public class LoggerEventPublisher implements EventPublisher {
    private static final Logger log = LoggerFactory.getLogger(LoggerEventPublisher.class);

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${gcp.pubsub.topic-name:appointment-events}")
    private String topicName;

    @Value("${gcp.pubsub.enabled:false}")
    private boolean pubsubEnabled;

    private Publisher publisher;

    @PostConstruct
    public void init() {
        if (pubsubEnabled) {
            try {
                TopicName topic = TopicName.of(projectId, topicName);
                this.publisher = Publisher.newBuilder(topic).build();
                log.info("Google Cloud Pub/Sub publisher initialized for topic: {}", topic);
            } catch (IOException e) {
                log.error("Failed to initialize Google Cloud Pub/Sub publisher", e);
                throw new RuntimeException("Failed to initialize Pub/Sub publisher", e);
            }
        } else {
            log.info("Google Cloud Pub/Sub is disabled, using logger fallback");
        }
    }

    @Override
    public void publish(String type, String payloadJson) {
        if (pubsubEnabled && publisher != null) {
            try {
                PubsubMessage message = PubsubMessage.newBuilder()
                        .setData(ByteString.copyFromUtf8(payloadJson))
                        .putAttributes("eventType", type)
                        .putAttributes("source", "appointment-service")
                        .build();

                publisher.publish(message).get();
                log.info("Event published to Pub/Sub -> type={}, topic={}", type, topicName);
            } catch (Exception e) {
                log.error("Failed to publish event to Pub/Sub -> type={}, payload={}", type, payloadJson, e);
                // Fallback to logging
                log.info("FALLBACK EVENT -> type={} payload={}", type, payloadJson);
            }
        } else {
            // Fallback when Pub/Sub is disabled
            log.info("SEND EVENT -> type={} payload={}", type, payloadJson);
        }
    }

    @PreDestroy
    public void destroy() {
        if (publisher != null) {
            try {
                publisher.shutdown();
                publisher.awaitTermination(30, TimeUnit.SECONDS);
                log.info("Google Cloud Pub/Sub publisher shutdown completed");
            } catch (InterruptedException e) {
                log.warn("Publisher shutdown was interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
