# Sequence Diagram 1: Student Applies to Drive

Maps the normal flow and every alternate/conflict path (section 5.1 requires
both success and conflict/eligibility decision paths to be visible).

```plantuml
@startuml
actor "Student (Aman Yadav)" as Student
participant "application_controller" as Ctrl
participant "ApplicationService" as AppSvc
participant "composite_EligibilityPolicy" as Policy
database "InMemory Repositories" as DB

Student -> Ctrl: POST /api/drives/{driveId}/applications
Ctrl -> AppSvc: apply_to_drive(studentId, driveId)
AppSvc -> DB: findById(student), findById(drive)
DB --> AppSvc: student, drive (or empty -> 404)
AppSvc -> DB: findByStudentIdAndDriveId()
DB --> AppSvc: empty (no duplicate found)
AppSvc -> AppSvc: check deadline not passed
AppSvc -> Policy: evaluate(student, drive)
Policy -> Policy: run cgpa/backlog/programme/\ngraduationYear/skills criteria
Policy --> AppSvc: EligibilityResult(eligible=true, reasons=[])
AppSvc -> DB: save(new_application, status=SUBMITTED)
DB --> AppSvc: application_created
AppSvc --> Ctrl: Application
Ctrl --> Student: 201 Created (applicationId)

alt Duplicate application
    AppSvc -> DB: findByStudentIdAndDriveId()
    DB --> AppSvc: existing application found
    AppSvc --> Ctrl: throw IllegalStateException
    Ctrl --> Student: 409 Conflict (DUPLICATE_APPLICATION)
else Deadline passed
    AppSvc -> AppSvc: deadline.isBefore(today) == true
    AppSvc --> Ctrl: throw IllegalStateException
    Ctrl --> Student: 409 Conflict (deadline passed)
else Ineligible
    AppSvc -> Policy: evaluate(student, drive)
    Policy --> AppSvc: EligibilityResult(eligible=false, reasons=[...])
    AppSvc --> Ctrl: throw IllegalStateException(reasons)
    Ctrl --> Student: 409 Conflict (with reasons)
end
@enduml
```

# Sequence Diagram 2: Ask AI Assistant

Maps the normal flow plus the timeout/failure path required by section 5.1.

```plantuml
@startuml
actor "Student (Aman Yadav)" as Student
participant "chat_controller" as ChatCtrl
participant "CareerAssistantService" as AISvc
database "InMemory Repositories" as DB
participant "ollama_ChatClient" as Adapter
participant "Ollama Local API" as LLM

Student -> ChatCtrl: POST /api/chat {message, studentId?, driveId?}
ChatCtrl -> AISvc: ask_question(studentId, driveId, message)
AISvc -> DB: findById(student), findById(drive)
DB --> AISvc: verified student/drive context (if present)
AISvc -> AISvc: evaluate(student, drive) via composite_EligibilityPolicy\n(grounds any eligibility claim in real data)
AISvc -> Adapter: generate_response(system_prompt, verified_context + question)
Adapter -> LLM: HTTP POST /api/chat (connect/read timeout from config)
alt Success
    LLM --> Adapter: JSON response (message.content)
    Adapter --> AISvc: parsed_answer
    AISvc --> ChatCtrl: advisory_answer
    ChatCtrl --> Student: 200 OK { answer, model, advisory: true }
else Ollama unreachable or timeout
    LLM --> Adapter: connection refused / SocketTimeoutException
    Adapter --> Adapter: catch ResourceAccessException
    Adapter --> AISvc: throw RuntimeException("Ollama service unavailable")
    AISvc --> ChatCtrl: exception propagates
    ChatCtrl --> Student: 503 Service Unavailable (advisory-offline message)
    note right of Student: All non-chat APIs (students, drives,\napplications) keep working - NFR-05
end
@enduml
```
