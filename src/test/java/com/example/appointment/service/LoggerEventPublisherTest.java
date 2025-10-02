package com.example.appointment.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
        "gcp.project-id=test-project",
        "gcp.pubsub.enabled=false",
        "gcp.pubsub.topic-name=test-topic"
})
class LoggerEventPublisherTest {

    @InjectMocks
    private LoggerEventPublisher loggerEventPublisher;

    @Test
    void shouldPublishEventWhenPubSubDisabled() {
        // Given
        ReflectionTestUtils.setField(loggerEventPublisher, "projectId", "test-project");
        ReflectionTestUtils.setField(loggerEventPublisher, "topicName", "test-topic");
        ReflectionTestUtils.setField(loggerEventPublisher, "pubsubEnabled", false);

        // When & Then - Should not throw exception
        assertDoesNotThrow(() -> {
            loggerEventPublisher.publish("TEST_EVENT", "{\"message\":\"test\"}");
        });
    }

    @Test
    void shouldInitializeWithoutErrorsWhenPubSubDisabled() {
        // Given
        ReflectionTestUtils.setField(loggerEventPublisher, "projectId", "test-project");
        ReflectionTestUtils.setField(loggerEventPublisher, "topicName", "test-topic");
        ReflectionTestUtils.setField(loggerEventPublisher, "pubsubEnabled", false);

        // When & Then - Should not throw exception
        assertDoesNotThrow(() -> {
            loggerEventPublisher.init();
        });
    }

    @Test
    void shouldCleanupResourcesOnDestroy() {
        // Given
        ReflectionTestUtils.setField(loggerEventPublisher, "publisher", null);

        // When & Then - Should not throw exception
        assertDoesNotThrow(() -> {
            loggerEventPublisher.destroy();
        });
    }
}