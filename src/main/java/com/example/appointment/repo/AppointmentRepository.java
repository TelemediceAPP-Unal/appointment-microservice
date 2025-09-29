package com.example.appointment.repo;

import com.example.appointment.domain.Appointment;
import com.example.appointment.domain.AppointmentState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    Page<Appointment> findByPatientId(String patientId, Pageable pageable);
    Page<Appointment> findByPractitionerId(String practitionerId, Pageable pageable);
    Page<Appointment> findByState(AppointmentState state, Pageable pageable);
}
