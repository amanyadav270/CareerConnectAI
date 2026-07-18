package com.placement.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Request body for POST/PUT /api/students. Validated per NFR-03: structurally
 * invalid requests (blank name/email, bad email shape, out-of-range CGPA,
 * negative backlogs) are rejected with 400 before any business logic runs.
 */
public class StudentDto {

    @NotBlank(message = "id is required")
    public String id;

    @NotBlank(message = "name is required")
    public String name;

    @NotBlank(message = "email is required")
    @Email(message = "email must be a valid email address")
    public String email;

    @NotBlank(message = "programme is required")
    public String programme;

    @NotNull(message = "graduationYear is required")
    public Integer graduationYear;

    @NotNull(message = "cgpa is required")
    @DecimalMin(value = "0.0", message = "cgpa must be between 0 and 10")
    @DecimalMax(value = "10.0", message = "cgpa must be between 0 and 10")
    public Double cgpa;

    @NotNull(message = "activeBacklogs is required")
    @Min(value = 0, message = "activeBacklogs cannot be negative")
    public Integer activeBacklogs;

    public List<String> skills;
}
