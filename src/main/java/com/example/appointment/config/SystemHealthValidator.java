package com.example.appointment.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
@ConditionalOnProperty(name = "gcp.secret-manager.enabled", havingValue = "false", matchIfMissing = true)
public class SystemHealthValidator {

    private static final Logger log = LoggerFactory.getLogger(SystemHealthValidator.class);

    @PostConstruct
    public void performHealthCheck() {
        log.info("System health check - GCP Secret Manager is disabled");
        log.info("Using local configuration for database connection");
    }
}