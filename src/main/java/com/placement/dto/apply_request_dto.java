package com.placement.dto;

/**
 * Request body for POST /api/drives/{driveId}/applications.
 * Keeps the API layer decoupled from the Application entity, per
 * section 7.5 of the problem statement (use DTOs, not repository internals).
 */
public class apply_request_dto {
    private String studentId;

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
}
