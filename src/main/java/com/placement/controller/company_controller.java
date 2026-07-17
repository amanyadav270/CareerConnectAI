package com.placement.controller;

import com.placement.exception.not_found_exception;
import com.placement.model.Company;
import com.placement.repository.company_repository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
public class company_controller {

    private final company_repository companyRepo;

    public company_controller(company_repository companyRepo) {
        this.companyRepo = companyRepo;
    }

    // FR-03: Create a company.
    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        if (company.getId() == null || company.getId().isBlank()) {
            company.setId(UUID.randomUUID().toString());
        }
        companyRepo.save(company);
        return ResponseEntity.status(201).body(company);
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyRepo.findAll());
    }

    // FR-03: Retrieve a company.
    @GetMapping("/{companyId}")
    public ResponseEntity<Company> getCompany(@PathVariable String companyId) {
        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new not_found_exception("Company not found: " + companyId));
        return ResponseEntity.ok(company);
    }
}
