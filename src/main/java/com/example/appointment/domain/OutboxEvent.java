package com.example.appointment.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox")
public class OutboxEvent {
    @Id
    private String id;

    @Column(nullable = false)
    private String aggregateId;

    @Column(nullable = false)
    private String type; // e.g., telemed.appointment.created

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payloadJson;

    @Column(nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(nullable = false)
    private boolean published = false;

    public OutboxEvent() {
        this.id = UUID.randomUUID().toString();
    }

    public OutboxEvent(String aggregateId, String type, String payloadJson) {
        this();
        this.aggregateId = aggregateId;
        this.type = type;
        this.payloadJson = payloadJson;
    }

    // getters/setters
    public String getId() { return id; }
    public String getAggregateId() { return aggregateId; }
    public String getType() { return type; }
    public String getPayloadJson() { return payloadJson; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public boolean isPublished() { return published; }
    public void setPublished(boolean published) { this.published = published; }
}
