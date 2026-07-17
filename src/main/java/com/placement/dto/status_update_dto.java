package com.placement.dto;

/**
 * Request body for PATCH /api/applications/{applicationId}/status.
 * "status" is expected to be one of the ApplicationStatus enum names,
 * e.g. "UNDER_REVIEW", "SHORTLISTED", "SELECTED", "REJECTED".
 */
public class status_update_dto {
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
