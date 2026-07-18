package com.placement.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlacementDrive {
    private String id;
    private String companyId;
    private String role;
    private String location;
    private double packageAmount;
    private LocalDate deadline;
    private LocalDate createdAt;
    private List<String> requiredSkills;
    private double minCgpa;
    private int maxBacklogsAllowed;
    private DriveStatus status;

    // Optional eligibility restrictions. Empty/null lists mean "no
    // restriction on this criterion" so existing drives created before
    // these fields existed keep working exactly as before.
    private List<String> eligibleProgrammes;
    private List<Integer> eligibleGraduationYears;

    public PlacementDrive() {
        this.requiredSkills = new ArrayList<>();
        this.eligibleProgrammes = new ArrayList<>();
        this.eligibleGraduationYears = new ArrayList<>();
        this.status = DriveStatus.OPEN;
    }

    public PlacementDrive(String id, String companyId, String role, String location, double packageAmount,
                           LocalDate deadline, List<String> requiredSkills, double minCgpa, int maxBacklogsAllowed) {
        this();
        this.id = id;
        this.companyId = companyId;
        this.role = role;
        this.location = location;
        this.packageAmount = packageAmount;
        this.deadline = deadline;
        this.requiredSkills = requiredSkills != null ? new ArrayList<>(requiredSkills) : new ArrayList<>();
        this.minCgpa = minCgpa;
        this.maxBacklogsAllowed = maxBacklogsAllowed;
    }

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

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public List<String> getRequiredSkills() { return new ArrayList<>(requiredSkills); }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills != null ? new ArrayList<>(requiredSkills) : new ArrayList<>(); }

    public double getMinCgpa() { return minCgpa; }
    public void setMinCgpa(double minCgpa) { this.minCgpa = minCgpa; }

    public int getMaxBacklogsAllowed() { return maxBacklogsAllowed; }
    public void setMaxBacklogsAllowed(int maxBacklogsAllowed) { this.maxBacklogsAllowed = maxBacklogsAllowed; }

    public DriveStatus getStatus() { return status; }
    public void setStatus(DriveStatus status) { this.status = status; }

    public List<String> getEligibleProgrammes() { return new ArrayList<>(eligibleProgrammes); }
    public void setEligibleProgrammes(List<String> eligibleProgrammes) {
        this.eligibleProgrammes = eligibleProgrammes != null ? new ArrayList<>(eligibleProgrammes) : new ArrayList<>();
    }

    public List<Integer> getEligibleGraduationYears() { return new ArrayList<>(eligibleGraduationYears); }
    public void setEligibleGraduationYears(List<Integer> eligibleGraduationYears) {
        this.eligibleGraduationYears = eligibleGraduationYears != null ? new ArrayList<>(eligibleGraduationYears) : new ArrayList<>();
    }
}
