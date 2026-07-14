package com.placement.service;

import com.placement.model.Student;
import com.placement.model.PlacementDrive;
import java.util.List;

public interface eligibility_policy {
    boolean is_eligible(Student target_student, PlacementDrive drive, List<String> reasons);
}