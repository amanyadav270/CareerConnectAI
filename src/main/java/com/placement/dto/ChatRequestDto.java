package com.placement.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body for POST /api/chat. studentId/driveId are intentionally
 * optional - FR-10 (general FAQ) supports asking a question with no
 * student/drive context at all.
 */
public class ChatRequestDto {
    private String studentId;
    private String driveId;

    @NotBlank(message = "message is required")
    private String message;

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getDriveId() { return driveId; }
    public void setDriveId(String driveId) { this.driveId = driveId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
