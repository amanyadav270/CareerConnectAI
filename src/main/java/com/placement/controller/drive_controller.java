package com.placement.controller;

import com.placement.exception.not_found_exception;
import com.placement.model.PlacementDrive;
import com.placement.repository.DriveRepository;
import com.placement.repository.company_repository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/drives")
public class drive_controller {

    private final DriveRepository driveRepo;
    private final company_repository companyRepo;

    public drive_controller(DriveRepository driveRepo, company_repository companyRepo) {
        this.driveRepo = driveRepo;
        this.companyRepo = companyRepo;
    }

    // FR-04: Create a drive. Key invariant (section 5.3): the drive must
    // reference an existing company, so we look it up before saving.
    @PostMapping
    public ResponseEntity<PlacementDrive> createDrive(@RequestBody PlacementDrive drive) {
        if (drive.getCompanyId() == null || companyRepo.findById(drive.getCompanyId()).isEmpty()) {
            throw new not_found_exception("Company not found: " + drive.getCompanyId());
        }
        if (drive.getId() == null || drive.getId().isBlank()) {
            drive.setId(UUID.randomUUID().toString());
        }
        driveRepo.save(drive);
        return ResponseEntity.status(201).body(drive);
    }

    // FR-05: List/search drives (simple filtering by company/role/location).
    @GetMapping
    public ResponseEntity<List<PlacementDrive>> getAllDrives(
            @RequestParam(required = false) String companyId,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String location) {
        List<PlacementDrive> drives = driveRepo.findAll();
        if (companyId != null) {
            drives.removeIf(d -> !companyId.equals(d.getCompanyId()));
        }
        if (role != null) {
            drives.removeIf(d -> d.getRole() == null || !d.getRole().equalsIgnoreCase(role));
        }
        if (location != null) {
            drives.removeIf(d -> d.getLocation() == null || !d.getLocation().equalsIgnoreCase(location));
        }
        return ResponseEntity.ok(drives);
    }

    // Retrieve a single drive.
    @GetMapping("/{driveId}")
    public ResponseEntity<PlacementDrive> getDrive(@PathVariable String driveId) {
        PlacementDrive drive = driveRepo.findById(driveId)
                .orElseThrow(() -> new not_found_exception("Drive not found: " + driveId));
        return ResponseEntity.ok(drive);
    }
}
