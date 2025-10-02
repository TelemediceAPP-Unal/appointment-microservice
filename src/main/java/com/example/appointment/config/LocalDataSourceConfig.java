package com.example.appointment.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(name = "gcp.secret-manager.enabled", havingValue = "false", matchIfMissing = true)
public class LocalDataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(LocalDataSourceConfig.class);

    @Value("${spring.datasource.hikari.maximum-pool-size:10}")
    private int maximumPoolSize;

    @Value("${spring.datasource.hikari.minimum-idle:2}")
    private int minimumIdle;

    @Value("${spring.datasource.hikari.connection-timeout:20000}")
    private long connectionTimeout;

    @Value("${spring.datasource.url:jdbc:h2:mem:apptdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE}")
    private String dbUrl;

    @Value("${spring.datasource.username:sa}")
    private String dbUsername;

    @Value("${spring.datasource.password:}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name:#{null}}")
    private String driverClassName;

    @Bean
    @Primary
    public DataSource dataSource() {
        log.info("Configuring local DataSource (GCP Secret Manager disabled)");

        HikariConfig config = new HikariConfig();

        // Basic connection settings
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);

        // Auto-detect driver based on URL
        String actualDriverClassName;
        if (dbUrl.contains("postgresql")) {
            actualDriverClassName = "org.postgresql.Driver";
            log.info("Detected PostgreSQL URL, using PostgreSQL driver");
        } else if (dbUrl.contains("h2")) {
            actualDriverClassName = "org.h2.Driver";
            log.info("Detected H2 URL, using H2 driver");
        } else if (driverClassName != null && !driverClassName.isEmpty()) {
            actualDriverClassName = driverClassName;
            log.info("Using configured driver: {}", actualDriverClassName);
        } else {
            actualDriverClassName = "org.h2.Driver";
            log.warn("No driver detected, defaulting to H2");
        }

        log.info("Using database URL: {} with driver: {}", dbUrl, actualDriverClassName);
        config.setDriverClassName(actualDriverClassName);

        // Pool configuration
        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumIdle);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        // Connection validation
        config.setConnectionTestQuery("SELECT 1");
        config.setValidationTimeout(3000);

        // Pool name for monitoring
        config.setPoolName("LocalHikariPool");

        log.info("Local DataSource configured successfully with URL: {}", dbUrl);

        return new HikariDataSource(config);
    }
}