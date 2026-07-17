# Daily Checkpoint Log

**Day 1 - Requirements & OOP Foundation**
* **Completed:** Defined actor assumptions, established core `student`, `company`, `placement_drive`, and `application` entities, and created thread-safe in-memory repositories.
* **Blocker:** None.
* **Target:** Implement core business rules and LLD sequence diagrams.

**Day 2 - Patterns & Core Logic**
* **Completed:** Implemented the Strategy Pattern via `eligibility_policy`. Built the `application_service` with duplicate prevention logic. Created LLD diagrams.
* **Blocker:** Clarifying exact sequence diagram formatting for PlantUML.
* **Target:** Design HLD and build Spring Boot REST controllers.

**Day 3 - HLD & REST API**
* **Completed:** Mapped HLD architecture. Configured Spring Boot web server. Built REST endpoints with centralized `@RestControllerAdvice` exception handling and DTOs.
* **Blocker:** Fixing dependency injection for `@Repository` beans.
* **Target:** Connect Ollama AI using Adapter pattern.

**Day 4 - AI Integration**
* **Completed:** Implemented Adapter Pattern (`ollama_chat_client`). Built `career_assistant_service` to inject database context into AI prompts. Verified 503 fallback.
* **Blocker:** Handling PowerShell invocation errors for JSON payloads.
* **Target:** Finalize README, Postman collection, and project submission.

**Day 5 - Testing & Documentation**
* **Completed:** Final end-to-end testing, README generation, and repository structuring.