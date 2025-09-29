package com.example.appointment.service;

import com.example.appointment.domain.*;
import com.example.appointment.repo.AppointmentRepository;
import com.example.appointment.repo.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class AppointmentService {

    private final AppointmentRepository repo;
    private final OutboxRepository outboxRepo;
    private final ObjectMapper mapper;

    public AppointmentService(AppointmentRepository repo, OutboxRepository outboxRepo, ObjectMapper mapper) {
        this.repo = repo;
        this.outboxRepo = outboxRepo;
        this.mapper = mapper;
    }

    @Transactional
    public Appointment create(String patientId, String practitionerId, OffsetDateTime startAt) {
        Appointment a = Appointment.create(patientId, practitionerId, startAt);
        repo.save(a);
        writeOutbox(a, "telemed.appointment.created");
        return a;
    }

    @Transactional(readOnly = true)
    public Page<Appointment> list(String patientId, String practitionerId, AppointmentState state, Pageable pageable) {
        if (patientId != null) return repo.findByPatientId(patientId, pageable);
        if (practitionerId != null) return repo.findByPractitionerId(practitionerId, pageable);
        if (state != null) return repo.findByState(state, pageable);
        return repo.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Appointment get(String id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    @Transactional
    public Appointment confirm(String id) {
        Appointment a = get(id);
        a.confirm();
        writeOutbox(a, "telemed.appointment.confirmed");
        return a;
    }

    @Transactional
    public void cancel(String id) {
        Appointment a = get(id);
        a.cancel();
        writeOutbox(a, "telemed.appointment.cancelled");
    }

    private void writeOutbox(Appointment a, String type) {
        try {
            String payload = mapper.writeValueAsString(new EventPayload(a.getId(), a.getPatientId(), a.getPractitionerId(), a.getStartAt(), a.getVersion()));
            outboxRepo.save(new OutboxEvent(a.getId(), type, payload));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    static class EventPayload {
        public String appointmentId;
        public String patientId;
        public String practitionerId;
        public OffsetDateTime startAt;
        public int version;

        EventPayload(String appointmentId, String patientId, String practitionerId, OffsetDateTime startAt, int version) {
            this.appointmentId = appointmentId;
            this.patientId = patientId;
            this.practitionerId = practitionerId;
            this.startAt = startAt;
            this.version = version;
        }
    }
}
