package com.placement.service;

import com.placement.model.Student;
import com.placement.model.PlacementDrive;
import java.util.List;

public class cgpa_eligibility_policy implements eligibility_policy {
    
    @Override
    public boolean is_eligible(Student target_student, PlacementDrive drive, List<String> reasons) {
        if (target_student.getCgpa() < drive.getMinCgpa()) {
            reasons.add("Minimum CGPA required: " + drive.getMinCgpa() + "; current CGPA: " + target_student.getCgpa());
            return false;
        }
        if (target_student.getActiveBacklogs() > drive.getMaxBacklogsAllowed()) {
            reasons.add("Maximum backlogs allowed: " + drive.getMaxBacklogsAllowed() + "; active backlogs: " + target_student.getActiveBacklogs());
            return false;
        }
        return true;
    }
}