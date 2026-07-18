package com.placement.repository;

import com.placement.model.Application;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository {
    Application save(Application application);
    Optional<Application> findById(String id);
    List<Application> findByStudentId(String studentId);
    Optional<Application> findByStudentIdAndDriveId(String studentId, String driveId);
    List<Application> findAll();
}
