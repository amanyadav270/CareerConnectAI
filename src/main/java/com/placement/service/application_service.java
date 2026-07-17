package com.placement.service;

import com.placement.model.Application;
import com.placement.model.ApplicationStatus;
import com.placement.model.Student;
import com.placement.model.PlacementDrive;
import com.placement.model.EligibilityResult; // Added missing import
import com.placement.repository.ApplicationRepository;
import com.placement.repository.StudentRepository;
import com.placement.repository.DriveRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class application_service {
    private final ApplicationRepository app_repo;
    private final StudentRepository student_repo;
    private final DriveRepository drive_repo;
    private final eligibility_policy policy;

    public application_service(ApplicationRepository app_repo, StudentRepository student_repo, DriveRepository drive_repo, eligibility_policy policy) {
        this.app_repo = app_repo;
        this.student_repo = student_repo;
        this.drive_repo = drive_repo;
        this.policy = policy;
    }

    public Application apply_to_drive(String student_id, String drive_id) {
        Optional<Student> current_student = student_repo.findById(student_id);
        Optional<PlacementDrive> current_drive = drive_repo.findById(drive_id);

        if (current_student.isEmpty() || current_drive.isEmpty()) {
            throw new IllegalArgumentException("Student or Drive not found.");
        }

        if (app_repo.findByStudentIdAndDriveId(student_id, drive_id).isPresent()) {
            throw new IllegalStateException("Student has already applied to this drive.");
        }

        // Updated to use the new evaluate method returning EligibilityResult
        EligibilityResult result = policy.evaluate(current_student.get(), current_drive.get());
        if (!result.isEligible()) {
            throw new IllegalStateException("Student is not eligible: " + String.join(", ", result.getReasons()));
        }

        Application new_app = new Application(
                UUID.randomUUID().toString(),
                student_id,
                drive_id,
                ApplicationStatus.SUBMITTED,
                LocalDateTime.now()
        );
        
        return app_repo.save(new_app);
    }

    // Added missing helper method required by application_controller
    public EligibilityResult check_eligibility(String student_id, String drive_id) {
        Optional<Student> current_student = student_repo.findById(student_id);
        Optional<PlacementDrive> current_drive = drive_repo.findById(drive_id);

        if (current_student.isEmpty() || current_drive.isEmpty()) {
            throw new IllegalArgumentException("Student or Drive not found.");
        }

        return policy.evaluate(current_student.get(), current_drive.get());
    }
}