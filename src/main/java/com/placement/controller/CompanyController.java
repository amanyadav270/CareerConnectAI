package com.placement.controller;

import com.placement.dto.CompanyDto;
import com.placement.exception.NotFoundException;
import com.placement.model.Company;
import com.placement.repository.CompanyRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyRepository companyRepo;

    public CompanyController(CompanyRepository companyRepo) {
        this.companyRepo = companyRepo;
    }

    // FR-03: Create a company. Rejects blank names (400, via @Valid) and
    // duplicate names (409) before saving.
    @PostMapping
    public ResponseEntity<Company> createCompany(@Valid @RequestBody CompanyDto request) {
        if (companyRepo.findByName(request.getName()).isPresent()) {
            throw new IllegalStateException("Company with name '" + request.getName() + "' already exists.");
        }

        String companyId = (request.getId() != null && !request.getId().isBlank())
                ? request.getId()
                : UUID.randomUUID().toString();

        Company company = new Company(companyId, request.getName(), request.getSector(), request.getDescription());
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
                .orElseThrow(() -> new NotFoundException("Company not found: " + companyId));
        return ResponseEntity.ok(company);
    }
}
