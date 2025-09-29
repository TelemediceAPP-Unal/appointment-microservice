package com.example.appointment.service;

import com.example.appointment.domain.OutboxEvent;
import com.example.appointment.repo.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OutboxPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);

    private final OutboxRepository outboxRepository;
    private final EventPublisher publisher;

    public OutboxPublisher(OutboxRepository outboxRepository, EventPublisher publisher) {
        this.outboxRepository = outboxRepository;
        this.publisher = publisher;
    }

    @Scheduled(fixedDelay = 3000)
    @Transactional
    public void publishPending() {
        List<OutboxEvent> batch = outboxRepository.findTop50ByPublishedFalseOrderByCreatedAtAsc();
        for (OutboxEvent ev : batch) {
            try {
                publisher.publish(ev.getType(), ev.getPayloadJson());
                ev.setPublished(true);
                log.info("Published event type={} id={}", ev.getType(), ev.getId());
            } catch (Exception ex) {
                log.error("Failed to publish event id={} type={}, will retry later", ev.getId(), ev.getType(), ex);
            }
        }
    }
}
