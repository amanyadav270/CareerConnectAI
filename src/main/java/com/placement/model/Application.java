package com.placement.model;

import java.time.LocalDateTime;

public class Application {
    private String id;
    private String studentId;
    private String driveId;
    private ApplicationStatus status;
    private LocalDateTime submittedAt;

    public Application() {}

    public Application(String id, String studentId, String driveId, ApplicationStatus status, LocalDateTime submittedAt) {
        this.id = id;
        this.studentId = studentId;
        this.driveId = driveId;
        this.status = status;
        this.submittedAt = submittedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getDriveId() { return driveId; }
    public void setDriveId(String driveId) { this.driveId = driveId; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}
