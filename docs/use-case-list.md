# Use-Case List

Required LLD artefact per section 5.1. Covers every actor/use-case pair from
section 2.2, with full flows for "Apply to Drive" and "Ask AI Assistant" as
explicitly required, plus a summary table for the remaining use cases.

## UC-1: Apply to Drive (full flow)

- **Actor:** Student
- **Preconditions:** The student and drive both exist; the student has not
  already applied to this drive.
- **Normal flow:**
  1. Student sends `POST /api/drives/{driveId}/applications` with their `studentId`.
  2. `ApplicationService` loads the `Student` and `PlacementDrive`; either missing → 404 (alternate flow A1).
  3. Service checks `ApplicationRepository` for an existing application for this student+drive → if found, 409 (alternate flow A2).
  4. Service checks the drive deadline against today → if passed, 409 (alternate flow A3).
  5. Service calls `composite_EligibilityPolicy.evaluate(...)`, which runs every `EligibilityCriterion` (CGPA, backlog, programme, graduation year, skills).
  6. If any criterion fails → 409 with the collected reasons (alternate flow A4).
  7. Service creates an `Application` with status `SUBMITTED` and saves it.
  8. Controller returns `201 Created` with the new application.
- **Alternate flows:**
  - **A1 – Unknown student or drive:** `NotFoundException` → `404 NOT_FOUND`.
  - **A2 – Duplicate application:** `IllegalStateException` → `409 CONFLICT_ERROR`.
  - **A3 – Deadline passed:** `IllegalStateException` → `409 CONFLICT_ERROR`.
  - **A4 – Ineligible student:** `IllegalStateException` with the composite eligibility reasons → `409 CONFLICT_ERROR`.
- **Postconditions:** Exactly one new `Application` exists in status `SUBMITTED`, or no state changed on any alternate flow.

## UC-2: Ask AI Assistant (full flow)

- **Actor:** Student
- **Preconditions:** None strictly required — `studentId`/`driveId` are optional context.
- **Normal flow:**
  1. Student sends `POST /api/chat` with an optional `studentId`, optional `driveId`, and a required `message`.
  2. `CareerAssistantService` looks up the student (if given) and drive (if given) from the repositories.
  3. If both are present, the service calls `composite_EligibilityPolicy.evaluate(...)` to ground any eligibility claim in a real, deterministic result.
  4. Service assembles a "Verified Context" block (never inventing data) plus the user's question and calls `ChatClient.generate_response(...)`.
  5. `ollama_ChatClient` posts to the configured Ollama URL with the configured model and a bounded connect/read timeout.
  6. On success, controller returns `200 OK` with `{ answer, model, advisory: true }`.
- **Alternate flows:**
  - **A1 – Unknown studentId/driveId:** service notes "not found in the system" in the context instead of failing, so the model can say so rather than inventing data.
  - **A2 – Ollama unreachable or times out:** `ResourceAccessException` is caught in `ollama_ChatClient` and rethrown; `chat_controller` catches it and returns `503 SERVICE_UNAVAILABLE` with an advisory-offline message. All non-chat APIs remain unaffected (NFR-05).
  - **A3 – Blank message:** rejected by `@Valid` before reaching the service → `400 BAD_REQUEST`.
- **Postconditions:** No entity state is ever modified by this use case — the assistant is read-only/advisory only, matching the core assumption in section 2.5.

## Remaining use cases (summary)

| Use Case | Actor | Precondition | Normal Flow | Alternate Flow |
|---|---|---|---|---|
| Create Student Profile | Student/Admin | Email not already registered | `POST /api/students` → validate → save → `201` | Blank/invalid field → `400`; duplicate email → `409` |
| Retrieve/Update Student | Student/Admin | Student exists | `GET`/`PUT /api/students/{id}` → return/update | Unknown id → `404`; invalid update payload → `400` |
| Create Company | Admin | Name not already registered | `POST /api/companies` → validate → save → `201` | Blank name → `400`; duplicate name → `409` |
| Create Placement Drive | Admin | Referenced company exists; deadline ≥ today | `POST /api/drives` → validate → save → `201` | Unknown company → `404`; deadline before creation date → `400` |
| List/Search Drives | Student/Admin | — | `GET /api/drives` with optional `companyId`/`role`/`location`/`status` filters | No matches → `200` with empty list |
| Evaluate Eligibility | Student | Student and drive exist | `GET /api/drives/{driveId}/eligibility/{studentId}` → composite policy → result with reasons | Unknown student/drive → `404` |
| Track Applications | Student | Student exists | `GET /api/students/{studentId}/applications`, `GET /api/applications/{id}` | Unknown id → `404` |
| Update Application Status | Admin | Application exists; transition is allowed | `PATCH /api/applications/{id}/status` → validate transition → save | Unknown status name → `400`; disallowed transition → `409` |
