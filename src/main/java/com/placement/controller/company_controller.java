package com.placement.controller;

import com.placement.model.Company;
import com.placement.repository.company_repository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class company_controller {
    private final company_repository companyRepo;

    public company_controller(company_repository companyRepo) {
        this.companyRepo = companyRepo;
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        companyRepo.save(company);
        return ResponseEntity.status(201).body(company);
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyRepo.findAll());
    }
}