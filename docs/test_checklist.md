# Integration and Negative Test Checklist

Run these manually with the Postman collection in `test-evidence/postman/` (or curl) against `http://localhost:8080` with Ollama running for the chat cases.

| ID | Scenario | Expected Outcome | Status |
| :--- | :--- | :--- | :--- |
| T-01 | Create valid student | 201 Created | Verified (screenshot) |
| T-02 | Create duplicate student email | 409 Conflict | Verified (screenshot) |
| T-03 | Create drive for unknown company | 404 Not Found | Verified (Postman) |
| T-04 | Evaluate eligible student | `eligible: true`, empty reasons | Verified (Postman) |
| T-05 | Evaluate ineligible student | `eligible: false` with reasons | Verified (Postman) |
| T-06 | Submit eligible application | 201 Created | Verified (screenshot) |
| T-07 | Submit duplicate application | 409 Conflict | Verified (screenshot) |
| T-08 | Submit ineligible application | 409 Conflict (`IllegalStateException`) | Verified (Postman) |
| T-09 | Apply after deadline | 409 Conflict | Verified (Postman) |
| T-10 | Valid status transition (e.g. SUBMITTED -> UNDER_REVIEW) | 200 OK | Verified (Postman) |
| T-11 | Invalid status transition (e.g. SELECTED -> SUBMITTED) | 409 Conflict | Verified (Postman) |
| T-12 | Retrieve unknown resource (student/company/drive/application) | 404 Not Found | Verified (Postman) |
| T-14 | Chatbot general FAQ (no driveId) | 200 with advisory answer | Verified (screenshot) |
| T-15 | Chatbot eligibility explanation (with driveId) | Answer grounded in the same result `/eligibility` returns | Verified (Postman) |
| T-16 | Chatbot preparation guidance | Uses supplied role/skill context | Verified (Postman) |
| T-17 | Ollama unavailable | 503 on `/api/chat`; all other APIs keep working | Verified (screenshot) |
| T-13 | Blank/invalid request field (name, email, cgpa, etc.) | 400 Bad Request with `VALIDATION_ERROR` | Verified (script) |
| T-18 | Create drive with deadline before today | 400 Bad Request | Verified (script) |
| T-19 | Create company with a name that already exists | 409 Conflict | Verified (script) |
| T-20 | Filter drives by `?status=OPEN` / `?status=CLOSED` | 200 with only matching drives; unknown value -> 400 | Verified (script) |
| T-21 | Ineligible student due to programme/graduation-year/skills mismatch (not just CGPA/backlog) | `eligible: false` listing the specific missing criterion | Verified (script) |
| T-22 | Malformed JSON body on any POST/PUT/PATCH | 400 Bad Request with `MALFORMED_REQUEST` | Verified (script) |

## Notes
- T-13 and T-18-T22 were re-verified by running `test-evidence/run-verification.sh` against a live instance and confirming each printed HTTP status/body matched the expected outcome above.
- "Verified (screenshot)" rows have image evidence in `test-evidence/screenshots/`; "Verified (Postman)" / "Verified (script)" rows were exercised via the Postman collection or the curl script but do not have a saved screenshot - add one if your mentor expects visual evidence for every row.
- T-08 uses 409 (`IllegalStateException` -> `CONFLICT_ERROR`) because ineligibility is treated as a business-rule conflict, consistent with the project's exception handling strategy.
- All chatbot scenarios are verified using the local Ollama adapter, so they require `ollama serve` running with the configured model pulled first.
