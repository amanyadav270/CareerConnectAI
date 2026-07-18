package com.placement.service.criteria;

import com.placement.model.PlacementDrive;
import com.placement.model.Student;
import com.placement.service.EligibilityCriterion;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * A drive with an empty/unset eligibleProgrammes list is open to every
 * programme (backward compatible with drives that never set this field).
 * Only drives that explicitly list eligible programmes restrict students.
 */
@Component
public class ProgrammeCriterion implements EligibilityCriterion {

    @Override
    public Optional<String> check(Student student, PlacementDrive drive) {
        List<String> eligibleProgrammes = drive.getEligibleProgrammes();
        if (eligibleProgrammes.isEmpty()) {
            return Optional.empty();
        }
        String studentProgramme = student.getProgramme();
        boolean matches = studentProgramme != null && eligibleProgrammes.stream()
                .anyMatch(p -> p.equalsIgnoreCase(studentProgramme));
        if (!matches) {
            return Optional.of("Programme " + studentProgramme + " is not eligible; required one of: " + eligibleProgrammes);
        }
        return Optional.empty();
    }
}
