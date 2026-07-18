package com.placement.service.criteria;

import com.placement.model.PlacementDrive;
import com.placement.model.Student;
import com.placement.service.EligibilityCriterion;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BacklogCriterion implements EligibilityCriterion {

    @Override
    public Optional<String> check(Student student, PlacementDrive drive) {
        if (student.getActiveBacklogs() > drive.getMaxBacklogsAllowed()) {
            return Optional.of("Maximum active backlogs allowed: " + drive.getMaxBacklogsAllowed()
                    + "; current active backlogs: " + student.getActiveBacklogs());
        }
        return Optional.empty();
    }
}
