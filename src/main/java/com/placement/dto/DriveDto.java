package com.placement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;
import java.util.List;

/**
 * Request body for POST /api/drives. Section 7.5 asks for request DTOs
 * rather than binding controllers directly to entities.
 * eligibleProgrammes / eligibleGraduationYears are optional - omit or
 * leave empty to keep the drive open to every programme/graduation year.
 */
public class DriveDto {

    // Optional: if supplied and non-blank, used as-is (keeps existing
    // Postman collections and chained requests working); otherwise the
    // controller generates a UUID.
    private String id;

    @NotBlank(message = "companyId is required")
    private String companyId;

    @NotBlank(message = "role is required")
    private String role;

    private String location;

    @PositiveOrZero(message = "packageAmount cannot be negative")
    private double packageAmount;

    @NotNull(message = "deadline is required")
    private LocalDate deadline;

    private List<String> requiredSkills;

    @NotNull(message = "minCgpa is required")
    @DecimalMin(value = "0.0", message = "minCgpa must be between 0 and 10")
    private Double minCgpa;

    @NotNull(message = "maxBacklogsAllowed is required")
    @Min(value = 0, message = "maxBacklogsAllowed cannot be negative")
    private Integer maxBacklogsAllowed;

    private List<String> eligibleProgrammes;

    private List<Integer> eligibleGraduationYears;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getPackageAmount() { return packageAmount; }
    public void setPackageAmount(double packageAmount) { this.packageAmount = packageAmount; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }

    public Double getMinCgpa() { return minCgpa; }
    public void setMinCgpa(Double minCgpa) { this.minCgpa = minCgpa; }

    public Integer getMaxBacklogsAllowed() { return maxBacklogsAllowed; }
    public void setMaxBacklogsAllowed(Integer maxBacklogsAllowed) { this.maxBacklogsAllowed = maxBacklogsAllowed; }

    public List<String> getEligibleProgrammes() { return eligibleProgrammes; }
    public void setEligibleProgrammes(List<String> eligibleProgrammes) { this.eligibleProgrammes = eligibleProgrammes; }

    public List<Integer> getEligibleGraduationYears() { return eligibleGraduationYears; }
    public void setEligibleGraduationYears(List<Integer> eligibleGraduationYears) { this.eligibleGraduationYears = eligibleGraduationYears; }
}
