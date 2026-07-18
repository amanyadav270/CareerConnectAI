package com.placement.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body for POST /api/companies. Section 7.5 asks for request DTOs
 * rather than binding controllers directly to entities, so the id is
 * generated server-side and never trusted from the client.
 */
public class CompanyDto {

    // Optional: if supplied and non-blank, used as-is (keeps existing
    // Postman collections and chained requests working); otherwise the
    // controller generates a UUID.
    private String id;

    @NotBlank(message = "name is required")
    private String name;

    private String sector;

    private String description;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
