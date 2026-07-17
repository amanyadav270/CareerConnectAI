# Requirements and Assumptions Document

## 1. Project Overview & Scope
CareerConnect AI is a data-driven, local AI-assisted campus placement management system. It serves as a centralized application portal to manage profiles, evaluate eligibility metrics programmatically, and provide automated contextual career advisory assistance through a local LLM integration.

### In-Scope Features
* **Profile Management**: Programmatic and API endpoints to manage student profiles, company detail registries, and placement drive details.
* **Deterministic Eligibility Engine**: Strategy-based evaluation verifying minimum CGPA thresholds and maximum allowed active backlogs.
* **Application Workflow Tracking**: Processing job applications through strict states (`SUBMITTED`, `UNDER_REVIEW`, `SHORTLISTED`, `SELECTED`, `REJECTED`) while enforcing zero-duplicate rules and validated status transitions (see `docs/api_contract.md`).
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
* **NFR-03 (Validation)**: Every ingress API request evaluates structural integrity (e.g., syntax correctness, validation tags) before transferring variables to core models.
* **NFR-04 (Workflow Consistency)**: Application status modifications conform to deterministic lifecycle rules. The system natively intercepts double-submissions at the data-access layer.
* **NFR-05 (System Availability & Degradation)**: The availability of the core application layer is decoupled from the AI engine. If the Ollama local HTTP pipeline crashes or encounters network timeouts, the profile registry and application submission paths continue running normally, serving a clean 503 error back to the user only on the chat route.