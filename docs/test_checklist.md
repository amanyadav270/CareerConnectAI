# Integration and Negative Test Checklist

| Test Scenario | Expected Outcome | Actual Output | Status |
| :--- | :--- | :--- | :--- |
| Create Student Profile (Happy Path) | 200/201 Created | Profile JSON returned | Pass  |
| AI Chat Guidance (Happy Path) | 200 OK | Advisory JSON returned | Pass  |
| Submit Duplicate Application (Negative) | 400/500 Error | Rejected by service layer | Pass  |
| AI Chat while Ollama is offline (Negative) | 503 Service Unavailable | Handled fallback response | Pass  |