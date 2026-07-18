package com.placement.service;

import com.placement.exception.NotFoundException;
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
public class ApplicationService {

    private final ApplicationRepository appRepo;
    private final StudentRepository studentRepo;
    private final DriveRepository driveRepo;
    private final EligibilityPolicy policy;

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

    public ApplicationService(ApplicationRepository appRepo, StudentRepository studentRepo, DriveRepository driveRepo, EligibilityPolicy policy) {
        this.appRepo = appRepo;
        this.studentRepo = studentRepo;
        this.driveRepo = driveRepo;
        this.policy = policy;
    }

    public Application applyToDrive(String studentId, String driveId) {
        Student currentStudent = studentRepo.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found: " + studentId));
        PlacementDrive currentDrive = driveRepo.findById(driveId)
                .orElseThrow(() -> new NotFoundException("Drive not found: " + driveId));

        if (appRepo.findByStudentIdAndDriveId(studentId, driveId).isPresent()) {
            throw new IllegalStateException("Student " + studentId + " has already applied to drive " + driveId + ".");
        }

        if (currentDrive.getDeadline() != null && currentDrive.getDeadline().isBefore(java.time.LocalDate.now())) {
            throw new IllegalStateException("Application deadline for drive " + driveId + " has passed.");
        }

        EligibilityResult result = policy.evaluate(currentStudent, currentDrive);
        if (!result.isEligible()) {
            throw new IllegalStateException("Student is not eligible: " + String.join(", ", result.getReasons()));
        }

        Application newApp = new Application(
                UUID.randomUUID().toString(),
                studentId,
                driveId,
                ApplicationStatus.SUBMITTED,
                LocalDateTime.now()
        );

        return appRepo.save(newApp);
    }

    public EligibilityResult checkEligibility(String studentId, String driveId) {
        Student currentStudent = studentRepo.findById(studentId)
                .orElseThrow(() -> new NotFoundException("Student not found: " + studentId));
        PlacementDrive currentDrive = driveRepo.findById(driveId)
                .orElseThrow(() -> new NotFoundException("Drive not found: " + driveId));

        return policy.evaluate(currentStudent, currentDrive);
    }

    public Application getApplication(String applicationId) {
        return appRepo.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application not found: " + applicationId));
    }

    public List<Application> getApplicationsForStudent(String studentId) {
        // Validate the student exists so the caller gets a 404 instead of a
        // silently empty list for an unknown student id.
        if (studentRepo.findById(studentId).isEmpty()) {
            throw new NotFoundException("Student not found: " + studentId);
        }
        return appRepo.findByStudentId(studentId);
    }

    public Application updateStatus(String applicationId, String requestedStatus) {
        Application application = appRepo.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application not found: " + applicationId));

        ApplicationStatus newStatus;
        try {
            newStatus = ApplicationStatus.valueOf(requestedStatus);
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new IllegalArgumentException("Unknown application status: " + requestedStatus);
        }

        ApplicationStatus currentStatus = application.getStatus();
        Set<ApplicationStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(currentStatus, EnumSet.noneOf(ApplicationStatus.class));

        if (currentStatus == newStatus) {
            throw new IllegalStateException("Application " + applicationId + " is already in status " + currentStatus + ".");
        }

        if (!allowed.contains(newStatus)) {
            throw new IllegalStateException("Invalid status transition: " + currentStatus + " -> " + newStatus + ".");
        }

        application.setStatus(newStatus);
        return appRepo.save(application);
    }
}
