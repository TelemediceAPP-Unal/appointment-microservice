package com.example.appointment.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Google Cloud Platform configuration
 * This configuration is activated when either Pub/Sub or Secret Manager is enabled
 */
@Configuration
@ConditionalOnProperty(
    value = {"gcp.pubsub.enabled", "gcp.secret-manager.enabled"}, 
    havingValue = "true", 
    matchIfMissing = false
)
public class GcpConfig {
    
    private static final Logger log = LoggerFactory.getLogger(GcpConfig.class);
    
    @PostConstruct
    public void init() {
        log.info("Google Cloud Platform configuration loaded");
        
        // Verificar que las variables de entorno necesarias estén configuradas
        String projectId = System.getenv("GCP_PROJECT_ID");
        String credentials = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        
        boolean pubsubEnabled = "true".equals(System.getenv("PUBSUB_ENABLED"));
        boolean secretManagerEnabled = "true".equals(System.getenv("SECRET_MANAGER_ENABLED"));
        
        if (projectId == null || projectId.trim().isEmpty()) {
            log.warn("GCP_PROJECT_ID environment variable is not set");
        }
        
        if (credentials == null || credentials.trim().isEmpty()) {
            log.warn("GOOGLE_APPLICATION_CREDENTIALS environment variable is not set");
        }
        
        if (pubsubEnabled) {
            log.info("Google Cloud Pub/Sub is enabled");
        }
        
        if (secretManagerEnabled) {
            log.info("Google Cloud Secret Manager is enabled");
        }
    }
}