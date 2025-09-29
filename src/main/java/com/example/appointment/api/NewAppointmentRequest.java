package com.example.appointment.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public class NewAppointmentRequest {
    @NotBlank
    public String patientId;
    @NotBlank
    public String practitionerId;
    @NotNull
    public OffsetDateTime startAt;
}
