package com.placement.controller;

import com.placement.dto.DriveDto;
import com.placement.exception.NotFoundException;
import com.placement.model.DriveStatus;
import com.placement.model.PlacementDrive;
import com.placement.repository.DriveRepository;
import com.placement.repository.CompanyRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/drives")
public class DriveController {

    private final DriveRepository driveRepo;
    private final CompanyRepository companyRepo;

    public DriveController(DriveRepository driveRepo, CompanyRepository companyRepo) {
        this.driveRepo = driveRepo;
        this.companyRepo = companyRepo;
    }

    // FR-04: Create a drive. Key invariant (section 5.3): the drive must
    // reference an existing company (404), and the deadline cannot precede
    // the drive's creation date (400).
    @PostMapping
    public ResponseEntity<PlacementDrive> createDrive(@Valid @RequestBody DriveDto request) {
        if (companyRepo.findById(request.getCompanyId()).isEmpty()) {
            throw new NotFoundException("Company not found: " + request.getCompanyId());
        }

        LocalDate createdAt = LocalDate.now();
        if (request.getDeadline() != null && request.getDeadline().isBefore(createdAt)) {
            throw new IllegalArgumentException("Drive deadline cannot precede its creation date (" + createdAt + ").");
        }

        String driveId = (request.getId() != null && !request.getId().isBlank())
                ? request.getId()
                : UUID.randomUUID().toString();

        PlacementDrive drive = new PlacementDrive(
                driveId,
                request.getCompanyId(),
                request.getRole(),
                request.getLocation(),
                request.getPackageAmount(),
                request.getDeadline(),
                request.getRequiredSkills(),
                request.getMinCgpa(),
                request.getMaxBacklogsAllowed()
        );
        drive.setCreatedAt(createdAt);
        drive.setStatus(DriveStatus.OPEN);
        drive.setEligibleProgrammes(request.getEligibleProgrammes());
        drive.setEligibleGraduationYears(request.getEligibleGraduationYears());

        driveRepo.save(drive);
        return ResponseEntity.status(201).body(drive);
    }

    // FR-05: List/search drives (filter by company/role/location/status).
    @GetMapping
    public ResponseEntity<List<PlacementDrive>> getAllDrives(
            @RequestParam(required = false) String companyId,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String status) {
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
        if (status != null) {
            DriveStatus parsedStatus;
            try {
                parsedStatus = DriveStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Unknown drive status: " + status + ". Expected OPEN or CLOSED.");
            }
            drives.removeIf(d -> d.getStatus() != parsedStatus);
        }
        return ResponseEntity.ok(drives);
    }

    // Retrieve a single drive.
    @GetMapping("/{driveId}")
    public ResponseEntity<PlacementDrive> getDrive(@PathVariable String driveId) {
        PlacementDrive drive = driveRepo.findById(driveId)
                .orElseThrow(() -> new NotFoundException("Drive not found: " + driveId));
        return ResponseEntity.ok(drive);
    }
}
