# Requirements and Assumptions Document

## 1. Project Overview & Scope
CareerConnect AI is a data-driven, local AI-assisted campus placement management system. It serves as a centralized application portal to manage profiles, evaluate eligibility metrics programmatically, and provide automated contextual career advisory assistance through a local LLM integration.

### In-Scope Features
* **Profile Management**: Programmatic and API endpoints to manage student profiles, company detail registries, and placement drive details.
* **Deterministic Eligibility Engine**: A Strategy + Composite evaluation (`composite_EligibilityPolicy`) that runs five independent, swappable criteria - minimum CGPA, maximum active backlogs, eligible programmes, eligible graduation years, and required-skill matching - and returns every failed reason, not just a boolean.
* **Application Workflow Tracking**: Processing job applications through strict states (`SUBMITTED`, `UNDER_REVIEW`, `SHORTLISTED`, `SELECTED`, `REJECTED`) while enforcing zero-duplicate rules, a deadline check, and validated status transitions (see `docs/api_contract.md`).
* **Context-Aware Career Assistant**: Local AI chatbot integration that utilizes verified application context (e.g., student grades, repository reasons) to answer career prep questions.

### Out-of-Scope Features
* **User Authentication & Authorization**: The prototype does not include interactive login sessions, JWT handling, or Role-Based Access Control (RBAC).
* **Persistent Database Infrastructure**: Physical external SQL/NoSQL engine configurations. All operations are handled by in-memory data structures.
* **Front-End User Interface**: HTML/CSS/JavaScript web views. The interface boundary is strictly bounded at the HTTP REST API layer.

---

## 2. Actor Assumptions & System Roles
1. **Student Actor (e.g., Aman Yadav)**: Can view details of placement drives, check eligibility metrics, submit placement applications, and converse with the career assistant.
2. **Company / Recruiter**: Can register corporate profiles and publish placement drives with specific academic cutoffs.
3. **Admin / Training & Placement Officer (TPO)**: Responsible for overall platform visibility, auditing operational exceptions, and tracking metrics.

---

## 3. Non-Functional Requirements & Guardrails
* **NFR-01 (Maintainability)**: Layered packages (`model`, `dto`, `repository`, `service`, `service.criteria`, `service.impl`, `controller`, `exception`); each eligibility rule lives in its own class so new rules can be added without touching existing ones.
* **NFR-03 (Validation)**: Every request DTO (`StudentDto`, `CompanyDto`, `DriveDto`, `ApplyRequestDto`, `StatusUpdateDto`, `ChatRequestDto`) carries Jakarta Bean Validation annotations (`@NotBlank`, `@NotNull`, `@Email`, `@DecimalMin`/`@DecimalMax`, `@Min`, `@PositiveOrZero`) and every controller applies `@Valid`, so structurally invalid requests are rejected with `400` before reaching business logic.
* **NFR-04 (Workflow Consistency)**: Application status modifications conform to deterministic lifecycle rules. The system natively intercepts double-submissions and expired-deadline submissions at the service layer.
* **NFR-05 (System Availability & Degradation)**: The availability of the core application layer is decoupled from the AI engine. If the Ollama local HTTP pipeline crashes, refuses the connection, or exceeds the configured timeout, the profile registry and application submission paths continue running normally, serving a clean `503` error back to the user only on the chat route.
* **NFR-06 (Configurability)**: `ollama.base-url`, `ollama.model`, `ollama.connect-timeout-seconds`, and `ollama.read-timeout-seconds` are external `application.properties` values, and are actually wired into the `RestTemplate` used by `ollama_ChatClient` (not just declared and ignored).

---

## 4. Key Invariants Enforced in Code
* Student email must be unique (`student_controller` checks before save).
* Company name must be unique (`company_controller` checks before save).
* A placement drive must reference an existing company (`drive_controller` returns `404` otherwise).
* A drive's deadline cannot precede its creation date (`drive_controller` returns `400` otherwise).
* A student cannot submit two applications to the same drive (`ApplicationService` returns `409`).
* Only eligible students may submit an application (`ApplicationService` returns `409` with reasons).
* An application's status may change only through the allowed transitions in `ApplicationService.ALLOWED_TRANSITIONS`.
* The chatbot never creates, approves, rejects, or modifies any entity - it only reads verified context supplied by `CareerAssistantService`.
