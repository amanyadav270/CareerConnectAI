package com.placement.repository;

import com.placement.model.PlacementDrive;
import java.util.List;
import java.util.Optional;

public interface DriveRepository {
    PlacementDrive save(PlacementDrive drive);
    Optional<PlacementDrive> findById(String id);
    List<PlacementDrive> findAll();
}