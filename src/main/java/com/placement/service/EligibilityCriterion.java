package com.placement.service;

import com.placement.model.PlacementDrive;
import com.placement.model.Student;

import java.util.Optional;

/**
 * A single, independently swappable eligibility rule (Strategy pattern).
 * Each criterion checks one aspect of eligibility (CGPA, backlogs,
 * programme, graduation year, skills, ...) and returns a human-readable
 * reason when the student fails that specific check, or Optional.empty()
 * when the criterion is satisfied.
 *
 * CompositeEligibilityPolicy composes every EligibilityCriterion bean
 * (Composite pattern layered on top of Strategy) so new criteria can be
 * added later just by implementing this interface - no existing class
 * needs to change. This is the variation point section 4.3 asks for.
 */
public interface EligibilityCriterion {
    Optional<String> check(Student student, PlacementDrive drive);
}
