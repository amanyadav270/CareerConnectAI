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

| Method | Path | Purpose | Key Status Codes |
| :--- | :--- | :--- | :--- |
| POST | `/api/students` | Create student | 201, 400, 409 |
| GET | `/api/students/{studentId}` | Retrieve student | 200, 404 |
| PUT | `/api/students/{studentId}` | Update student profile | 200, 404 |
| POST | `/api/companies` | Create company | 201 |
| GET | `/api/companies` | List companies | 200 |
| GET | `/api/companies/{companyId}` | Retrieve company | 200, 404 |
| POST | `/api/drives` | Create placement drive | 201, 404 (unknown company) |
| GET | `/api/drives` | List/filter drives (`companyId`, `role`, `location` query params) | 200 |
| GET | `/api/drives/{driveId}` | Retrieve drive | 200, 404 |
| GET | `/api/drives/{driveId}/eligibility/{studentId}` | Evaluate eligibility with reasons | 200, 404 |
| POST | `/api/drives/{driveId}/applications` | Submit application | 201, 404, 409 |
| GET | `/api/students/{studentId}/applications` | List a student's applications | 200, 404 |
| GET | `/api/applications/{applicationId}` | Retrieve one application | 200, 404 |
| PATCH | `/api/applications/{applicationId}/status` | Update application status | 200, 400, 404, 409 |
| POST | `/api/chat` | Ask the AI career assistant | 200, 503 |

## Examples

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
    "CGPA is below the minimum requirement of 7.5",
    "Active backlogs exceed the maximum allowed."
  ]
}
```

### Chat (with drive context, grounds the answer in a real eligibility result)
```
POST /api/chat
{
  "student_id": "STU-001",
  "drive_id": "DRV-101",
  "message": "Why am I not eligible and what should I improve?"
}
```
```json
{ "answer": "...", "advisory": true }
```
If Ollama is unreachable, returns `503` with `{ "error": "Service Unavailable", ... }`.
