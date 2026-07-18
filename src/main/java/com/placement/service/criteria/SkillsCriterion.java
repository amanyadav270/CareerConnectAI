package com.placement.service.criteria;

import com.placement.model.PlacementDrive;
import com.placement.model.Student;
import com.placement.service.EligibilityCriterion;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A drive with no required skills places no skill demand on the student.
 * Otherwise every required skill must be present (case-insensitive) in the
 * student's skill list; missing skills are listed by name so the chatbot
 * and eligibility endpoint can explain exactly what to improve (FR-06,
 * FR-12).
 */
@Component
public class SkillsCriterion implements EligibilityCriterion {

    @Override
    public Optional<String> check(Student student, PlacementDrive drive) {
        List<String> requiredSkills = drive.getRequiredSkills();
        if (requiredSkills.isEmpty()) {
            return Optional.empty();
        }

        Set<String> studentSkillsLower = student.getSkills().stream()
                .filter(s -> s != null)
                .map(s -> s.toLowerCase(Locale.ROOT))
                .collect(Collectors.toSet());

        List<String> missing = new ArrayList<>();
        for (String requiredSkill : requiredSkills) {
            if (requiredSkill == null) {
                continue;
            }
            if (!studentSkillsLower.contains(requiredSkill.toLowerCase(Locale.ROOT))) {
                missing.add(requiredSkill);
            }
        }

        if (!missing.isEmpty()) {
            return Optional.of("Missing required skill(s): " + String.join(", ", missing));
        }
        return Optional.empty();
    }
}
