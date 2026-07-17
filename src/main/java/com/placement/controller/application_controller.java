package com.placement.controller;

import com.placement.dto.apply_request_dto;
import com.placement.dto.status_update_dto;
import com.placement.model.Application;
import com.placement.model.EligibilityResult;
import com.placement.service.application_service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class application_controller {

    private final application_service appService;

    public application_controller(application_service appService) {
        this.appService = appService;
    }

    // FR-07: Submit a student application to a drive.
    @PostMapping("/drives/{driveId}/applications")
    public ResponseEntity<Application> applyToDrive(@PathVariable String driveId, @RequestBody apply_request_dto request) {
        Application app = appService.apply_to_drive(request.getStudentId(), driveId);
        return ResponseEntity.status(201).body(app);
    }

    // FR-06: Evaluate eligibility with reasons.
    @GetMapping("/drives/{driveId}/eligibility/{studentId}")
    public ResponseEntity<EligibilityResult> checkEligibility(@PathVariable String driveId, @PathVariable String studentId) {
        EligibilityResult result = appService.check_eligibility(studentId, driveId);
        return ResponseEntity.ok(result);
    }

    // FR-08: List all applications for a given student.
    @GetMapping("/students/{studentId}/applications")
    public ResponseEntity<List<Application>> getApplicationsForStudent(@PathVariable String studentId) {
        return ResponseEntity.ok(appService.get_applications_for_student(studentId));
    }

    // FR-08: Retrieve a single application.
    @GetMapping("/applications/{applicationId}")
    public ResponseEntity<Application> getApplication(@PathVariable String applicationId) {
        return ResponseEntity.ok(appService.get_application(applicationId));
    }

    // FR-09: Move an application through its allowed status transitions.
    @PatchMapping("/applications/{applicationId}/status")
    public ResponseEntity<Application> updateStatus(@PathVariable String applicationId, @RequestBody status_update_dto request) {
        Application updated = appService.update_status(applicationId, request.getStatus());
        return ResponseEntity.ok(updated);
    }
}
