# Integration and Negative Test Checklist

Run these manually with the Postman collection in `test-evidence/postman/` (or curl) against
`http://localhost:8080` with Ollama running for the chat cases.

| ID | Scenario | Expected Outcome | Status |
| :--- | :--- | :--- | :--- |
| T-01 | Create valid student | 201 Created | Manual verification required |
| T-02 | Create duplicate student email | 409 Conflict | Manual verification required |
| T-03 | Create drive for unknown company | 404 Not Found | Manual verification required |
| T-04 | Evaluate eligible student | `eligible: true`, empty/positive reasons | Manual verification required |
| T-05 | Evaluate ineligible student | `eligible: false` with reasons | Manual verification required |
| T-06 | Submit eligible application | 201 Created | Manual verification required |
| T-07 | Submit duplicate application | 409 Conflict | Manual verification required |
| T-08 | Submit ineligible application | 409 Conflict (`IllegalStateException`) | Manual verification required |
| T-09 | Apply after deadline | 409 Conflict | Manual verification required |
| T-10 | Valid status transition (e.g. SUBMITTED -> UNDER_REVIEW) | 200 OK | Manual verification required |
| T-11 | Invalid status transition (e.g. SELECTED -> SUBMITTED) | 409 Conflict | Manual verification required |
| T-12 | Retrieve unknown resource (student/company/drive/application) | 404 Not Found | Manual verification required |
| T-13 | Blank/invalid request field | 400 Bad Request | Manual verification required |
| T-14 | Chatbot general FAQ (no driveId) | 200 with advisory answer | Manual verification required |
| T-15 | Chatbot eligibility explanation (with driveId) | Answer grounded in the same result `/eligibility` returns | Manual verification required |
| T-16 | Chatbot preparation guidance | Uses supplied role/skill context | Manual verification required |
| T-17 | Ollama unavailable | 503 on `/api/chat`; all other APIs keep working | Manual verification required |

## Notes
- Status is intentionally left as "Manual verification required" rather than "Pass" until each
  row has actually been executed against a running instance and the response captured (screenshot
  or Postman run) in `test-evidence/`. Marking a row "Pass" without having run it misrepresents
  what's been verified.
- T-08's exact status code is implementation-defined per section 7.5 of the problem statement;
  this project uses 409 (`IllegalStateException` -> `CONFLICT_ERROR`) since ineligibility is a
  business-rule conflict, not a malformed request.
