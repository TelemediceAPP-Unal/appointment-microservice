package com.example.appointment.api;

import com.example.appointment.domain.Appointment;
import com.example.appointment.domain.AppointmentState;
import com.example.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @PostMapping
    public AppointmentResponse create(@Valid @RequestBody NewAppointmentRequest req) {
        Appointment a = service.create(req.patientId, req.practitionerId, req.startAt);
        return AppointmentResponse.from(a);
    }

    @GetMapping
    public Page<AppointmentResponse> list(
            @RequestParam(value = "patientId", required = false) String patientId,
            @RequestParam(value = "practitionerId", required = false) String practitionerId,
            @RequestParam(value = "state", required = false) AppointmentState state,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        return service.list(patientId, practitionerId, state, PageRequest.of(page, pageSize))
                .map(AppointmentResponse::from);
    }

    @GetMapping("/{id}")
    public AppointmentResponse byId(@PathVariable("id") String id) {
        return AppointmentResponse.from(service.get(id));
    }

    @PatchMapping("/{id}/confirm")
    public AppointmentResponse confirm(@PathVariable("id") String id) {
        return AppointmentResponse.from(service.confirm(id));
    }

    @DeleteMapping("/{id}")
    public void cancel(@PathVariable("id") String id) {
        service.cancel(id);
    }
}
