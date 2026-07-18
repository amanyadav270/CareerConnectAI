package com.placement.model;

public class Company {
    private String id;
    private String name;
    private String sector;
    private String description;

    public Company() {}

    public Company(String id, String name, String sector, String description) {
        this.id = id;
        this.name = name;
        this.sector = sector;
        this.description = description;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
