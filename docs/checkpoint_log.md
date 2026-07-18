# Daily Checkpoint Log

**Day 1 - Requirements & OOP Foundation**
* **Completed:** Defined actor assumptions, established core `student`, `company`, `placement_drive`, and `application` entities, and created thread-safe in-memory repositories.
* **Blocker:** None.
* **Target:** Implement core business rules and LLD sequence diagrams.

**Day 2 - Patterns & Core Logic**
* **Completed:** Implemented the Strategy Pattern via `EligibilityPolicy`. Built the `ApplicationService` with duplicate prevention logic. Created LLD diagrams.
* **Blocker:** Clarifying exact sequence diagram formatting for PlantUML.
* **Target:** Design HLD and build Spring Boot REST controllers.

**Day 3 - HLD & REST API**
* **Completed:** Mapped HLD architecture. Configured Spring Boot web server. Built REST endpoints with centralized `@RestControllerAdvice` exception handling and DTOs.
* **Blocker:** Fixing dependency injection for `@Repository` beans.
* **Target:** Connect Ollama AI using Adapter pattern.

**Day 4 - AI Integration**
* **Completed:** Implemented Adapter Pattern (`ollama_ChatClient`). Built `CareerAssistantService` to inject database context into AI prompts. Verified 503 fallback.
* **Blocker:** Handling PowerShell invocation errors for JSON payloads.
* **Target:** Finalize README, Postman collection, and project submission.

**Day 5 - Testing & Documentation**
* **Completed:** Final end-to-end testing, README generation, and repository structuring.
---

**Post-Submission Revision (LLD/Pattern gap closure)**
* **Completed:** Rebuilt eligibility evaluation as a true Strategy+Composite (`composite_EligibilityPolicy` composing `CgpaCriterion`/`BacklogCriterion`/`ProgrammeCriterion`/`GraduationYearCriterion`/`SkillsCriterion`) instead of CGPA/backlog only. Added `DriveStatus` enum and drive status filter. Added `CompanyDto`/`DriveDto` with Jakarta Bean Validation and wired `@Valid` across every controller. Added the drive deadline-vs-creation-date invariant and duplicate-company-name check. Wired the previously-unused `ollama.connect-timeout-seconds`/`read-timeout-seconds` config into `ollama_ChatClient`'s `RestTemplate`. Added the missing LLD artefacts: class diagram (`docs/uml/class-diagram.puml` / `docs/lld-class-diagram.png`), CRC/responsibility table (`docs/lld-responsibility-table.md`), and use-case list (`docs/use-case-list.md`). Regenerated the sequence diagrams to reference the new class names and added their alternate/failure flows. Extended the Postman collection and test checklist with the new scenarios (T-18 to T-22).
* **Blocker:** None - all changes verified by manual code review and a brace/import consistency pass; no Maven-based build was available in the revision environment (no Maven Central network access), so a full `mvn compile` could not be executed here. Please run `mvn spring-boot:run` and re-execute the Postman collection before final submission.
* **Target:** Re-capture screenshots for the newly added and re-verified test scenarios, then final submission.
