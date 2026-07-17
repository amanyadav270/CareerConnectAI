# CareerConnect AI

A local AI-assisted campus placement management system backend, built using Spring Boot. This system manages student profiles, placement drives, and job applications, while securely integrating a local LLM to provide students with career guidance based on their specific academic profile and job requirements.

## 🚀 Setup & Execution

### Prerequisites
* Java 17 or higher
* Maven 3.8+

### Running the Application
To start the Spring Boot server, navigate to the root project directory and run:
`mvn spring-boot:run`

The server will start on `http://localhost:8080`.

### 🧠 Local AI Setup (Ollama)
This project requires a local installation of Ollama to process AI chat requests securely without external API calls.
1. Download and install Ollama from [ollama.com](https://ollama.com).
2. Open a terminal and pull the required Llama 3 model: `ollama run llama3`
3. Ensure Ollama is running in the background (default port 11434) before testing the `/api/chat` endpoint.

---

## 📡 API Endpoints Summary

**Students**
* `POST /api/students` - Create a new student profile
* `GET /api/students/{id}` - Retrieve a student profile
* `PUT /api/students/{id}` - Update a student profile

**Companies**
* `POST /api/companies` - Register a new company
* `GET /api/companies` - List all registered companies
* `GET /api/companies/{companyId}` - Retrieve a single company

**Placement Drives**
* `POST /api/drives` - Create a new placement drive (404 if the referenced company doesn't exist)
* `GET /api/drives` - List placement drives (optional `companyId`, `role`, `location` query filters)
* `GET /api/drives/{driveId}` - Retrieve a single drive

**Applications & Eligibility**
* `POST /api/drives/{id}/applications` - Submit a job application (body: `{"studentId": "..."}`)
* `GET /api/drives/{id}/eligibility/{studentId}` - Check if a student meets drive criteria
* `GET /api/students/{studentId}/applications` - List a student's applications
* `GET /api/applications/{applicationId}` - Retrieve a single application
* `PATCH /api/applications/{applicationId}/status` - Move an application through its status lifecycle (body: `{"status": "UNDER_REVIEW"}`). Allowed transitions: `SUBMITTED → UNDER_REVIEW → SHORTLISTED → SELECTED`, with `REJECTED` reachable from `SUBMITTED`, `UNDER_REVIEW`, or `SHORTLISTED`. Invalid transitions return `409`.

**AI Assistant**
* `POST /api/chat` - Ask the AI career advisor a question. Body: `{"student_id": "...", "drive_id": "...", "message": "..."}`. `student_id` and `drive_id` are both optional; when both are supplied, the assistant grounds its answer in the same deterministic `EligibilityResult` returned by the eligibility endpoint (never invents eligibility).

---

## 🏗️ Design Patterns & Architecture

* **Strategy Pattern:** The eligibility validation logic is decoupled into a `cgpa_eligibility_policy` interface. This ensures that grading criteria (like CGPA or backlogs) can be swapped or extended dynamically without modifying the core application service.
* **Adapter Pattern:** The external HTTP calls to the Ollama AI model are wrapped behind a unified `ollama_chat_client` interface. This protects the core business logic from being tightly coupled to the AI's specific network implementation and allows for graceful error handling if the AI is offline.

---

## ⚠️ Limitations

* **Data Persistence:** The current prototype utilizes thread-safe in-memory repositories (`ConcurrentHashMap`). All data is wiped and resets upon server reboot. A future enhancement would involve migrating to a persistent relational database like PostgreSQL.
* **Hardware Dependency:** The AI chat endpoint's performance depends heavily on the host machine's hardware capabilities to run the local Llama 3 model efficiently.