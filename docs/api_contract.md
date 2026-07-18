# REST API Contract

All endpoints are prefixed with `/api`. All error responses use the shared error shape:

```json
{
  "timestamp": "2026-07-20T11:30:00",
  "status": 409,
  "code": "CONFLICT_ERROR",
  "message": "Student STU-001 has already applied to drive DRV-101."
}
```

Structural validation failures (blank/missing/out-of-range fields) return `400` with `"code": "VALIDATION_ERROR"` and a semicolon-joined list of field messages.

| Method | Path | Purpose | Key Status Codes |
| :--- | :--- | :--- | :--- |
| POST | `/api/students` | Create student | 201, 400, 409 |
| GET | `/api/students/{studentId}` | Retrieve student | 200, 404 |
| PUT | `/api/students/{studentId}` | Update student profile | 200, 400, 404 |
| POST | `/api/companies` | Create company | 201, 400, 409 |
| GET | `/api/companies` | List companies | 200 |
| GET | `/api/companies/{companyId}` | Retrieve company | 200, 404 |
| POST | `/api/drives` | Create placement drive | 201, 400 (bad payload / deadline before creation date), 404 (unknown company) |
| GET | `/api/drives` | List/filter drives (`companyId`, `role`, `location`, `status` query params) | 200, 400 (unknown status value) |
| GET | `/api/drives/{driveId}` | Retrieve drive | 200, 404 |
| GET | `/api/drives/{driveId}/eligibility/{studentId}` | Evaluate eligibility with reasons (CGPA, backlog, programme, graduation year, skills) | 200, 404 |
| POST | `/api/drives/{driveId}/applications` | Submit application | 201, 400, 404, 409 (duplicate / deadline passed / ineligible) |
| GET | `/api/students/{studentId}/applications` | List a student's applications | 200, 404 |
| GET | `/api/applications/{applicationId}` | Retrieve one application | 200, 404 |
| PATCH | `/api/applications/{applicationId}/status` | Update application status | 200, 400, 404, 409 |
| POST | `/api/chat` | Ask the AI career assistant | 200, 400, 503 |

## Examples

### Create company
```
POST /api/companies
{ "name": "Acme Corp", "sector": "Software", "description": "Cloud platforms" }
```
```
HTTP/1.1 201 Created
{ "id": "8f3c...", "name": "Acme Corp", "sector": "Software", "description": "Cloud platforms" }
```
A repeated `name` (case-insensitive) returns `409 CONFLICT_ERROR`.

### Create placement drive
```
POST /api/drives
{
  "companyId": "8f3c...",
  "role": "Backend Engineer",
  "location": "Bengaluru",
  "packageAmount": 1200000,
  "deadline": "2026-08-15",
  "requiredSkills": ["Java", "Spring Boot"],
  "minCgpa": 7.5,
  "maxBacklogsAllowed": 0,
  "eligibleProgrammes": ["B.Tech"],
  "eligibleGraduationYears": [2026, 2027]
}
```
`eligibleProgrammes`/`eligibleGraduationYears` are optional; omit them (or send an empty list) to keep the drive open to every programme/graduation year. A `deadline` before today returns `400 BAD_REQUEST`; an unknown `companyId` returns `404 NOT_FOUND`.

### Submit application
```
POST /api/drives/DRV-101/applications
{ "studentId": "STU-001" }
```
```
HTTP/1.1 201 Created
{
  "id": "APP-501",
  "studentId": "STU-001",
  "driveId": "DRV-101",
  "status": "SUBMITTED",
  "submittedAt": "2026-07-20T11:25:00"
}
```

### Update application status
```
PATCH /api/applications/APP-501/status
{ "status": "UNDER_REVIEW" }
```
```
HTTP/1.1 200 OK
{ "id": "APP-501", "status": "UNDER_REVIEW", ... }
```
Invalid transitions (e.g. `SELECTED -> SUBMITTED`, or repeating the current status) return `409 CONFLICT_ERROR`.
An unknown status name returns `400 BAD_REQUEST`.

### Eligibility check
```
GET /api/drives/DRV-101/eligibility/STU-001
```
```json
{
  "eligible": false,
  "reasons": [
    "Minimum CGPA required: 7.5; current CGPA: 7.2",
    "Missing required skill(s): Spring Boot"
  ]
}
```
Reasons can now come from any of the five composed criteria - CGPA, backlogs, programme, graduation year, or skills - not just CGPA/backlogs.

### Chat (with drive context, grounds the answer in a real eligibility result)
```
POST /api/chat
{
  "studentId": "STU-001",
  "driveId": "DRV-101",
  "message": "Why am I not eligible and what should I improve?"
}
```
```json
{ "answer": "...", "model": "llama3.1", "advisory": true }
```
A blank `message` returns `400 BAD_REQUEST`. If Ollama is unreachable or the configured timeout is exceeded, returns `503` with `{ "error": "Service Unavailable", "message": "..." }`.
