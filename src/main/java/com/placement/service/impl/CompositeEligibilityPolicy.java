package com.placement.service.impl;

import com.placement.model.EligibilityResult;
import com.placement.model.PlacementDrive;
import com.placement.model.Student;
import com.placement.service.EligibilityCriterion;
import com.placement.service.EligibilityPolicy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

/**
 * Strategy + Composite: the single EligibilityPolicy implementation wired
 * into ApplicationService and CareerAssistantService. Spring injects
 * every EligibilityCriterion bean (CgpaCriterion, BacklogCriterion,
 * ProgrammeCriterion, GraduationYearCriterion, SkillsCriterion) as a
 * List, so this class evaluates CGPA, backlog, programme, graduation year,
 * AND skills as required by section 4.1/4.3 of the problem statement -
 * without needing an if/else chain, and without any existing criterion
 * needing to change when a new one is added later.
 */
@Service
public class CompositeEligibilityPolicy implements EligibilityPolicy {

    private final List<EligibilityCriterion> criteria;

    public CompositeEligibilityPolicy(List<EligibilityCriterion> criteria) {
        this.criteria = criteria;
    }

    @Override
    public EligibilityResult evaluate(Student student, PlacementDrive drive) {
        List<String> reasons = new ArrayList<>();
        for (EligibilityCriterion criterion : criteria) {
            criterion.check(student, drive).ifPresent(reasons::add);
        }
        return new EligibilityResult(reasons.isEmpty(), reasons);
    }
}
