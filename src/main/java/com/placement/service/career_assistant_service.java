package com.placement.service;

import com.placement.model.EligibilityResult;
import com.placement.model.PlacementDrive;
import com.placement.model.Student;
import com.placement.repository.DriveRepository;
import com.placement.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class career_assistant_service {

    private static final String SYSTEM_PROMPT =
            "You are an advisory campus career assistant. Use only the verified context provided. " +
            "You cannot approve, reject, or modify any application. Keep answers short and factual.";

    private final chat_client ai_client;
    private final StudentRepository student_repo;
    private final DriveRepository drive_repo;
    private final eligibility_policy policy;

    public career_assistant_service(chat_client ai_client, StudentRepository student_repo,
                                     DriveRepository drive_repo, eligibility_policy policy) {
        this.ai_client = ai_client;
        this.student_repo = student_repo;
        this.drive_repo = drive_repo;
        this.policy = policy;
    }

    /**
     * Handles FAQ (FR-10), eligibility explanation (FR-11), preparation
     * guidance (FR-12), and profile summary using only verified, application
     * supplied context - never inventing student, drive, or eligibility data.
     */
    public String ask_question(String student_id, String drive_id, String message) {
        StringBuilder context = new StringBuilder();

        Optional<Student> student = student_id != null ? student_repo.findById(student_id) : Optional.empty();
        if (student.isPresent()) {
            Student s = student.get();
            context.append("Student profile -> name: ").append(s.getName())
                    .append(", programme: ").append(s.getProgramme())
                    .append(", graduationYear: ").append(s.getGraduationYear())
                    .append(", cgpa: ").append(s.getCgpa())
                    .append(", activeBacklogs: ").append(s.getActiveBacklogs())
                    .append(", skills: ").append(s.getSkills())
                    .append(". ");
        } else if (student_id != null) {
            context.append("Requested studentId was not found in the system. ");
        }

        Optional<PlacementDrive> drive = drive_id != null ? drive_repo.findById(drive_id) : Optional.empty();
        if (drive.isPresent()) {
            PlacementDrive d = drive.get();
            context.append("Drive -> role: ").append(d.getRole())
                    .append(", requiredSkills: ").append(d.getRequiredSkills())
                    .append(", minCgpa: ").append(d.getMinCgpa())
                    .append(", maxBacklogsAllowed: ").append(d.getMaxBacklogsAllowed())
                    .append(", deadline: ").append(d.getDeadline())
                    .append(". ");

            // FR-11: ground the eligibility explanation in a real, deterministic
            // EligibilityResult rather than letting the model guess.
            if (student.isPresent()) {
                EligibilityResult result = policy.evaluate(student.get(), d);
                context.append("Deterministic eligibility result -> eligible: ").append(result.isEligible());
                if (!result.getReasons().isEmpty()) {
                    context.append(", reasons: ").append(result.getReasons());
                }
                context.append(". ");
            }
        } else if (drive_id != null) {
            context.append("Requested driveId was not found in the system. ");
        }

        if (context.length() == 0) {
            context.append("No specific student or drive context supplied for this question.");
        }

        String full_user_message = "Verified Context: [" + context + "] User Question: " + message;
        return ai_client.generate_response(SYSTEM_PROMPT, full_user_message);
    }
}
