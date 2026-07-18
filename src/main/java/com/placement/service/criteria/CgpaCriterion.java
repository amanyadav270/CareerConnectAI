package com.placement.service.criteria;

import com.placement.model.PlacementDrive;
import com.placement.model.Student;
import com.placement.service.EligibilityCriterion;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CgpaCriterion implements EligibilityCriterion {

    @Override
    public Optional<String> check(Student student, PlacementDrive drive) {
        if (student.getCgpa() < drive.getMinCgpa()) {
            return Optional.of("Minimum CGPA required: " + drive.getMinCgpa() + "; current CGPA: " + student.getCgpa());
        }
        return Optional.empty();
    }
}
