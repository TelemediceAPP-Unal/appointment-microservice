package com.example.appointment.repo;

import com.example.appointment.domain.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEvent, String> {
    List<OutboxEvent> findTop50ByPublishedFalseOrderByCreatedAtAsc();
}
