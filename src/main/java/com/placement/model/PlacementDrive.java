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
    private List<String> requiredSkills;
    private double minCgpa;
    private int maxBacklogsAllowed;

    public PlacementDrive() {
        this.requiredSkills = new ArrayList<>();
    }

    public PlacementDrive(String id, String companyId, String role, String location, double packageAmount, LocalDate deadline, List<String> requiredSkills, double minCgpa, int maxBacklogsAllowed) {
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

    public List<String> getRequiredSkills() { return new ArrayList<>(requiredSkills); }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills != null ? new ArrayList<>(requiredSkills) : new ArrayList<>(); }

    public double getMinCgpa() { return minCgpa; }
    public void setMinCgpa(double minCgpa) { this.minCgpa = minCgpa; }

    public int getMaxBacklogsAllowed() { return maxBacklogsAllowed; }
    public void setMaxBacklogsAllowed(int maxBacklogsAllowed) { this.maxBacklogsAllowed = maxBacklogsAllowed; }
}