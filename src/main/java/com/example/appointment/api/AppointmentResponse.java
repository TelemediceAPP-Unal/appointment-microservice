package com.example.appointment.api;

import com.example.appointment.domain.Appointment;
import com.example.appointment.domain.AppointmentState;
import java.time.OffsetDateTime;

public class AppointmentResponse {
    public String id;
    public String patientId;
    public String practitionerId;
    public OffsetDateTime startAt;
    public OffsetDateTime endAt;
    public AppointmentState state;
    public int version;

    public static AppointmentResponse from(Appointment a) {
        AppointmentResponse r = new AppointmentResponse();
        r.id = a.getId();
        r.patientId = a.getPatientId();
        r.practitionerId = a.getPractitionerId();
        r.startAt = a.getStartAt();
        r.endAt = a.getEndAt();
        r.state = a.getState();
        r.version = a.getVersion();
        return r;
    }
}
