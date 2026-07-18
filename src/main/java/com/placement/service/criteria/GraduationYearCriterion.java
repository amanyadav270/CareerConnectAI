package com.placement.service.criteria;

import com.placement.model.PlacementDrive;
import com.placement.model.Student;
import com.placement.service.EligibilityCriterion;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * A drive with an empty/unset eligibleGraduationYears list accepts any
 * graduation year (backward compatible with existing drives).
 */
@Component
public class GraduationYearCriterion implements EligibilityCriterion {

    @Override
    public Optional<String> check(Student student, PlacementDrive drive) {
        List<Integer> eligibleYears = drive.getEligibleGraduationYears();
        if (eligibleYears.isEmpty()) {
            return Optional.empty();
        }
        if (!eligibleYears.contains(student.getGraduationYear())) {
            return Optional.of("Graduation year " + student.getGraduationYear()
                    + " is not eligible; required one of: " + eligibleYears);
        }
        return Optional.empty();
    }
}
