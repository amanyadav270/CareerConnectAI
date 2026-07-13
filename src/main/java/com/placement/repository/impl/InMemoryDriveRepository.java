package com.placement.repository.impl;

import com.placement.model.PlacementDrive;
import com.placement.repository.DriveRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDriveRepository implements DriveRepository {
    private final Map<String, PlacementDrive> store = new ConcurrentHashMap<>();

    @Override
    public PlacementDrive save(PlacementDrive drive) {
        store.put(drive.getId(), drive);
        return drive;
    }

    @Override
    public Optional<PlacementDrive> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<PlacementDrive> findAll() {
        return new ArrayList<>(store.values());
    }
}