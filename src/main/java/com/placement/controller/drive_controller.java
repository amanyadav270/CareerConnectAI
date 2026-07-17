package com.placement.controller;

import com.placement.model.PlacementDrive;
import com.placement.repository.DriveRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/drives")
public class drive_controller {
    private final DriveRepository driveRepo;

    public drive_controller(DriveRepository driveRepo) {
        this.driveRepo = driveRepo;
    }

    @PostMapping
    public ResponseEntity<PlacementDrive> createDrive(@RequestBody PlacementDrive drive) {
        driveRepo.save(drive);
        return ResponseEntity.status(201).body(drive);
    }

    @GetMapping
    public ResponseEntity<List<PlacementDrive>> getAllDrives() {
        return ResponseEntity.ok(driveRepo.findAll());
    }
}