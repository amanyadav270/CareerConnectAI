package com.placement.service;

import com.placement.model.Student;
import com.placement.model.PlacementDrive;
import com.placement.model.EligibilityResult;

public interface EligibilityPolicy {
    EligibilityResult evaluate(Student student, PlacementDrive drive);
}
