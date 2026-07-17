package com.placement.controller;

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

    @PostMapping("/drives/{id}/applications")
    public ResponseEntity<Application> applyToDrive(@PathVariable String id, @RequestBody Application request) {
        // Assuming the request body contains the student_id
        Application app = appService.apply_to_drive(request.getStudentId(), id);
        return ResponseEntity.status(201).body(app);
    }

    @GetMapping("/drives/{id}/eligibility/{studentId}")
    public ResponseEntity<EligibilityResult> checkEligibility(@PathVariable String id, @PathVariable String studentId) {
        // Assuming your application_service has a method to invoke the eligibility policy
        EligibilityResult result = appService.check_eligibility(studentId, id);
        return ResponseEntity.ok(result);
    }
}