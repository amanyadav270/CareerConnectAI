package com.placement.service;

import com.placement.model.Student;
import com.placement.model.PlacementDrive;
import com.placement.model.EligibilityResult;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class cgpa_eligibility_policy implements eligibility_policy {
    
    @Override
    public EligibilityResult evaluate(Student student, PlacementDrive drive) {
        List<String> reasons = new ArrayList<>();
        boolean eligible = true;

        if (student.getCgpa() < drive.getMinCgpa()) {
            eligible = false;
            reasons.add("CGPA is below the minimum requirement of " + drive.getMinCgpa());
        }
        
        // Updated to correctly call getMaxBacklogsAllowed() from your model
        if (student.getActiveBacklogs() > drive.getMaxBacklogsAllowed()) {
            eligible = false;
            reasons.add("Active backlogs exceed the maximum allowed.");
        }

        return new EligibilityResult(eligible, reasons);
    }
}