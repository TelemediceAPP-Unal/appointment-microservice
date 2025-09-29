package com.example.appointment.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    private String id;

    @Column(nullable = false)
    private String patientId;

    @Column(nullable = false)
    private String practitionerId;

    @Column(nullable = false)
    private OffsetDateTime startAt;

    private OffsetDateTime endAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentState state = AppointmentState.CREATED;

    @Version
    private int version;

    public Appointment() {
        this.id = UUID.randomUUID().toString();
    }

    public static Appointment create(String patientId, String practitionerId, OffsetDateTime startAt) {
        Appointment a = new Appointment();
        a.patientId = patientId;
        a.practitionerId = practitionerId;
        a.startAt = startAt;
        a.state = AppointmentState.CREATED;
        a.endAt = startAt.plusMinutes(30); // Default duration 30 minutes
        return a;
    }

    public void confirm() {
        if (this.state == AppointmentState.CANCELLED) {
            throw new IllegalStateException("Cannot confirm a cancelled appointment");
        }
        this.state = AppointmentState.CONFIRMED;
    }

    public void cancel() {
        if (this.state == AppointmentState.CANCELLED)
            return;
        this.state = AppointmentState.CANCELLED;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPractitionerId() {
        return practitionerId;
    }

    public void setPractitionerId(String practitionerId) {
        this.practitionerId = practitionerId;
    }

    public OffsetDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(OffsetDateTime startAt) {
        this.startAt = startAt;
    }

    public OffsetDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(OffsetDateTime endAt) {
        this.endAt = endAt;
    }

    public AppointmentState getState() {
        return state;
    }

    public void setState(AppointmentState state) {
        this.state = state;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
