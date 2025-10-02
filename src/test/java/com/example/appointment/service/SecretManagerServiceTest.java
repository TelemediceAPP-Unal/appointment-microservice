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
    "gcp.secret-manager.enabled=false"
})
class SecretManagerServiceTest {

    @InjectMocks
    private SecretManagerService secretManagerService;

    @Test
    void shouldInitializeWithoutErrorsWhenDisabled() {
        // Given
        ReflectionTestUtils.setField(secretManagerService, "projectId", "test-project");
        ReflectionTestUtils.setField(secretManagerService, "secretManagerEnabled", false);

        // When & Then - Should not throw exception
        assertDoesNotThrow(() -> {
            secretManagerService.init();
        });
    }

    @Test
    void shouldReturnNullWhenSecretManagerDisabled() {
        // Given
        ReflectionTestUtils.setField(secretManagerService, "secretManagerEnabled", false);

        // When
        String secret = secretManagerService.getSecret("test-secret");

        // Then
        assertNull(secret);
    }

    @Test
    void shouldReturnNullForDatabaseCredentialsWhenDisabled() {
        // Given
        ReflectionTestUtils.setField(secretManagerService, "secretManagerEnabled", false);

        // When
        SecretManagerService.DatabaseCredentials credentials = 
            secretManagerService.getDatabaseCredentials();

        // Then
        assertNull(credentials);
    }

    @Test
    void shouldReturnNullForPubSubCredentialsWhenDisabled() {
        // Given
        ReflectionTestUtils.setField(secretManagerService, "secretManagerEnabled", false);

        // When
        SecretManagerService.PubSubCredentials credentials = 
            secretManagerService.getPubSubCredentials();

        // Then
        assertNull(credentials);
    }

    @Test
    void shouldClearCacheWithoutErrors() {
        // When & Then - Should not throw exception
        assertDoesNotThrow(() -> {
            secretManagerService.clearCache();
        });
    }

    @Test
    void shouldDestroyWithoutErrors() {
        // When & Then - Should not throw exception
        assertDoesNotThrow(() -> {
            secretManagerService.destroy();
        });
    }

    @Test
    void databaseCredentialsShouldBuildJdbcUrl() {
        // Given
        SecretManagerService.DatabaseCredentials credentials = 
            new SecretManagerService.DatabaseCredentials(
                "localhost", "5432", "testdb", "user", "pass"
            );

        // When
        String jdbcUrl = credentials.getJdbcUrl();

        // Then
        assertEquals("jdbc:postgresql://localhost:5432/testdb", jdbcUrl);
    }

    @Test
    void databaseCredentialsShouldReturnCorrectValues() {
        // Given
        SecretManagerService.DatabaseCredentials credentials = 
            new SecretManagerService.DatabaseCredentials(
                "localhost", "5432", "testdb", "user", "pass"
            );

        // Then
        assertEquals("localhost", credentials.getHost());
        assertEquals("5432", credentials.getPort());
        assertEquals("testdb", credentials.getName());
        assertEquals("user", credentials.getUsername());
        assertEquals("pass", credentials.getPassword());
    }

    @Test
    void pubSubCredentialsShouldReturnCorrectValues() {
        // Given
        SecretManagerService.PubSubCredentials credentials = 
            new SecretManagerService.PubSubCredentials("test-topic", "test-key");

        // Then
        assertEquals("test-topic", credentials.getTopicName());
        assertEquals("test-key", credentials.getServiceAccountKey());
    }
}