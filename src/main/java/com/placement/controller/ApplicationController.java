package com.placement.controller;

import com.placement.dto.ApplyRequestDto;
import com.placement.dto.StatusUpdateDto;
import com.placement.model.Application;
import com.placement.model.EligibilityResult;
import com.placement.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApplicationController {

    private final ApplicationService appService;

    public ApplicationController(ApplicationService appService) {
        this.appService = appService;
    }

    // FR-07: Submit a student application to a drive.
    @PostMapping("/drives/{driveId}/applications")
    public ResponseEntity<Application> applyToDrive(@PathVariable String driveId, @Valid @RequestBody ApplyRequestDto request) {
        Application app = appService.applyToDrive(request.getStudentId(), driveId);
        return ResponseEntity.status(201).body(app);
    }

    // FR-06: Evaluate eligibility with reasons.
    @GetMapping("/drives/{driveId}/eligibility/{studentId}")
    public ResponseEntity<EligibilityResult> checkEligibility(@PathVariable String driveId, @PathVariable String studentId) {
        EligibilityResult result = appService.checkEligibility(studentId, driveId);
        return ResponseEntity.ok(result);
    }

    // FR-08: List all applications for a given student.
    @GetMapping("/students/{studentId}/applications")
    public ResponseEntity<List<Application>> getApplicationsForStudent(@PathVariable String studentId) {
        return ResponseEntity.ok(appService.getApplicationsForStudent(studentId));
    }

    // FR-08: Retrieve a single application.
    @GetMapping("/applications/{applicationId}")
    public ResponseEntity<Application> getApplication(@PathVariable String applicationId) {
        return ResponseEntity.ok(appService.getApplication(applicationId));
    }

    // FR-09: Move an application through its allowed status transitions.
    @PatchMapping("/applications/{applicationId}/status")
    public ResponseEntity<Application> updateStatus(@PathVariable String applicationId, @Valid @RequestBody StatusUpdateDto request) {
        Application updated = appService.updateStatus(applicationId, request.getStatus());
        return ResponseEntity.ok(updated);
    }
}
