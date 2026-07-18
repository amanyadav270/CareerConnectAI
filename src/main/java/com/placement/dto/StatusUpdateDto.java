package com.placement.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body for PATCH /api/applications/{applicationId}/status.
 * "status" is expected to be one of the ApplicationStatus enum names,
 * e.g. "UNDER_REVIEW", "SHORTLISTED", "SELECTED", "REJECTED".
 */
public class StatusUpdateDto {

    @NotBlank(message = "status is required")
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
