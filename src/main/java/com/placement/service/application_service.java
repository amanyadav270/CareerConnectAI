package com.placement.service;

import com.placement.exception.not_found_exception;
import com.placement.model.Application;
import com.placement.model.ApplicationStatus;
import com.placement.model.EligibilityResult;
import com.placement.model.PlacementDrive;
import com.placement.model.Student;
import com.placement.repository.ApplicationRepository;
import com.placement.repository.DriveRepository;
import com.placement.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class application_service {

    private final ApplicationRepository app_repo;
    private final StudentRepository student_repo;
    private final DriveRepository drive_repo;
    private final eligibility_policy policy;

    // Section 5.2 recommended lifecycle:
    // SUBMITTED -> UNDER_REVIEW -> SHORTLISTED -> SELECTED
    //                                          \-> REJECTED
    // UNDER_REVIEW/SHORTLISTED -> REJECTED is also allowed.
    // SELECTED and REJECTED are terminal states.
    private static final Map<ApplicationStatus, Set<ApplicationStatus>> ALLOWED_TRANSITIONS = new EnumMap<>(ApplicationStatus.class);
    static {
        ALLOWED_TRANSITIONS.put(ApplicationStatus.SUBMITTED, EnumSet.of(ApplicationStatus.UNDER_REVIEW, ApplicationStatus.REJECTED));
        ALLOWED_TRANSITIONS.put(ApplicationStatus.UNDER_REVIEW, EnumSet.of(ApplicationStatus.SHORTLISTED, ApplicationStatus.REJECTED));
        ALLOWED_TRANSITIONS.put(ApplicationStatus.SHORTLISTED, EnumSet.of(ApplicationStatus.SELECTED, ApplicationStatus.REJECTED));
        ALLOWED_TRANSITIONS.put(ApplicationStatus.SELECTED, EnumSet.noneOf(ApplicationStatus.class));
        ALLOWED_TRANSITIONS.put(ApplicationStatus.REJECTED, EnumSet.noneOf(ApplicationStatus.class));
    }

    public application_service(ApplicationRepository app_repo, StudentRepository student_repo, DriveRepository drive_repo, eligibility_policy policy) {
        this.app_repo = app_repo;
        this.student_repo = student_repo;
        this.drive_repo = drive_repo;
        this.policy = policy;
    }

    public Application apply_to_drive(String student_id, String drive_id) {
        Student current_student = student_repo.findById(student_id)
                .orElseThrow(() -> new not_found_exception("Student not found: " + student_id));
        PlacementDrive current_drive = drive_repo.findById(drive_id)
                .orElseThrow(() -> new not_found_exception("Drive not found: " + drive_id));

        if (app_repo.findByStudentIdAndDriveId(student_id, drive_id).isPresent()) {
            throw new IllegalStateException("Student " + student_id + " has already applied to drive " + drive_id + ".");
        }

        if (current_drive.getDeadline() != null && current_drive.getDeadline().isBefore(java.time.LocalDate.now())) {
            throw new IllegalStateException("Application deadline for drive " + drive_id + " has passed.");
        }

        EligibilityResult result = policy.evaluate(current_student, current_drive);
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

    public EligibilityResult check_eligibility(String student_id, String drive_id) {
        Student current_student = student_repo.findById(student_id)
                .orElseThrow(() -> new not_found_exception("Student not found: " + student_id));
        PlacementDrive current_drive = drive_repo.findById(drive_id)
                .orElseThrow(() -> new not_found_exception("Drive not found: " + drive_id));

        return policy.evaluate(current_student, current_drive);
    }

    public Application get_application(String application_id) {
        return app_repo.findById(application_id)
                .orElseThrow(() -> new not_found_exception("Application not found: " + application_id));
    }

    public List<Application> get_applications_for_student(String student_id) {
        // Validate the student exists so the caller gets a 404 instead of a
        // silently empty list for an unknown student id.
        if (student_repo.findById(student_id).isEmpty()) {
            throw new not_found_exception("Student not found: " + student_id);
        }
        return app_repo.findByStudentId(student_id);
    }

    public Application update_status(String application_id, String requested_status) {
        Application application = app_repo.findById(application_id)
                .orElseThrow(() -> new not_found_exception("Application not found: " + application_id));

        ApplicationStatus new_status;
        try {
            new_status = ApplicationStatus.valueOf(requested_status);
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new IllegalArgumentException("Unknown application status: " + requested_status);
        }

        ApplicationStatus current_status = application.getStatus();
        Set<ApplicationStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(current_status, EnumSet.noneOf(ApplicationStatus.class));

        if (current_status == new_status) {
            throw new IllegalStateException("Application " + application_id + " is already in status " + current_status + ".");
        }

        if (!allowed.contains(new_status)) {
            throw new IllegalStateException("Invalid status transition: " + current_status + " -> " + new_status + ".");
        }

        application.setStatus(new_status);
        return app_repo.save(application);
    }
}
