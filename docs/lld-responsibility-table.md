# CRC / Responsibility Table

Required LLD artefact per section 5.1 ("Responsibility Table / CRC — at least
10 key classes with responsibilities and collaborators"). Matches the class
diagram at `docs/uml/class-diagram.puml` / `docs/lld-class-diagram.png`.

| # | Class | Responsibility | Collaborators |
|---|---|---|---|
| 1 | `Student` | Own student profile state (name, programme, CGPA, backlogs, skills). | Referenced by `Application`, read by `EligibilityCriterion` implementations and `CareerAssistantService`. |
| 2 | `Company` | Represent an organisation participating in placements. | Referenced by `PlacementDrive`; looked up by `drive_controller` and `company_controller`. |
| 3 | `PlacementDrive` | Represent a role's requirements, deadline, and eligibility rules. | References `Company`; read by `EligibilityCriterion` implementations, `ApplicationService`, `CareerAssistantService`. |
| 4 | `Application` | Represent one student-drive application and its current status. | References `Student` and `PlacementDrive`; owned/updated by `ApplicationService`. |
| 5 | `EligibilityResult` | Hold the eligible/ineligible outcome plus human-readable reasons. | Produced by `composite_EligibilityPolicy`; consumed by `ApplicationService`, `CareerAssistantService`, `application_controller`. |
| 6 | `EligibilityPolicy` (interface) | Define the eligibility evaluation contract (Strategy). | Implemented by `composite_EligibilityPolicy`; used by `ApplicationService`, `CareerAssistantService`. |
| 7 | `EligibilityCriterion` (interface) | Define one independent, swappable eligibility rule (Strategy variation point). | Implemented by `CgpaCriterion`, `BacklogCriterion`, `ProgrammeCriterion`, `GraduationYearCriterion`, `SkillsCriterion`; composed by `composite_EligibilityPolicy`. |
| 8 | `composite_EligibilityPolicy` | Compose every `EligibilityCriterion` bean into one aggregate `EligibilityResult` (Composite over Strategy). | Depends on all `EligibilityCriterion` implementations; implements `EligibilityPolicy`. |
| 9 | `CgpaCriterion` / `BacklogCriterion` / `ProgrammeCriterion` / `GraduationYearCriterion` / `SkillsCriterion` | Each evaluates exactly one eligibility rule (CGPA, backlog count, programme match, graduation year match, required-skill match) and returns a failure reason or nothing. | Read `Student` and `PlacementDrive`; invoked by `composite_EligibilityPolicy`. |
| 10 | `ChatClient` (interface) | Define a model-independent chatbot request contract (Adapter). | Implemented by `ollama_ChatClient`; used by `CareerAssistantService`. |
| 11 | `ollama_ChatClient` | Translate application requests to/from the Ollama HTTP API; enforce connect/read timeouts; map failures to a runtime exception. | Implements `ChatClient`; calls the external Ollama HTTP API. |
| 12 | `ApplicationService` | Coordinate eligibility checks, duplicate/deadline prevention, application creation, and status-transition rules. | Depends on `ApplicationRepository`, `StudentRepository`, `DriveRepository`, `EligibilityPolicy`; called by `application_controller`. |
| 13 | `CareerAssistantService` | Build a safe, grounded prompt from verified student/drive/eligibility data and delegate generation to `ChatClient`. | Depends on `ChatClient`, `StudentRepository`, `DriveRepository`, `EligibilityPolicy`; called by `chat_controller`. |
| 14 | `StudentRepository` / `DriveRepository` / `ApplicationRepository` / `CompanyRepository` (interfaces) | Abstract storage access from business logic (Repository pattern). | Implemented by the four `InMemory*Repository` classes; used by services and controllers. |
| 15 | `InMemoryStudentRepository` / `InMemoryDriveRepository` / `InMemoryApplicationRepository` / `in_memory_CompanyRepository` | Provide thread-safe (`ConcurrentHashMap`-backed) in-memory storage per the five-day prototype scope. | Implement their respective repository interfaces. |
| 16 | `student_controller` / `company_controller` / `drive_controller` / `application_controller` / `chat_controller` | Expose REST endpoints, bind/validate request DTOs, map results to HTTP responses. | Depend on their respective repositories/services; DTOs in the `dto` package. |
| 17 | `GlobalExceptionHandler` | Centralise mapping from domain/validation exceptions to the shared HTTP error contract. | Handles `NotFoundException`, `IllegalArgumentException`, `IllegalStateException`, `MethodArgumentNotValidException`, `HttpMessageNotReadableException`, and unexpected exceptions. |
| 18 | `NotFoundException` | Signal that a referenced entity does not exist. | Thrown by controllers/services; caught by `GlobalExceptionHandler`. |
