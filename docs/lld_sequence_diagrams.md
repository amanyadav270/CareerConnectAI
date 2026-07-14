# Sequence Diagram 1: Student Applies to Drive
This diagram maps out the normal flow and conflict path when student Aman Yadav attempts to apply to a placement drive.

```plantuml
@startuml
actor "Student (Aman Yadav)" as Student
participant "Application Controller" as Ctrl
participant "application_service" as AppSvc
participant "cgpa_eligibility_policy" as Policy
database "InMemory Repositories" as DB

Student -> Ctrl: POST /api/drives/{driveId}/applications
Ctrl -> AppSvc: apply_to_drive(studentId, driveId)
AppSvc -> DB: findByStudentIdAndDriveId()
DB --> AppSvc: empty (No duplicate found)
AppSvc -> Policy: is_eligible(student, drive, reasons)
Policy --> AppSvc: true
AppSvc -> DB: save(new_application)
DB --> AppSvc: application_created
AppSvc --> Ctrl: 201 Created (applicationId)
Ctrl --> Student: Success Response
@enduml

# Sequence Diagram 2: Ask AI Assistant
@startuml
actor "Student (Aman Yadav)" as Student
participant "Chat Controller" as ChatCtrl
participant "career_assistant_service" as AISvc
participant "ollama_chat_client" as Adapter
database "Ollama Local API" as LLM

Student -> ChatCtrl: POST /api/chat
ChatCtrl -> AISvc: ask_question(studentId, driveId, message)
AISvc -> DB: retrieve verified system context
AISvc -> Adapter: send_prompt(system_context, user_message)
Adapter -> LLM: HTTP POST (Timeout: 60s)
alt Success
    LLM --> Adapter: JSON Response (Model text)
    Adapter --> AISvc: parsed_answer
    AISvc --> ChatCtrl: advisory_response
    ChatCtrl --> Student: 200 OK (answer)
else Timeout / Service Unavailable
    LLM --> Adapter: Connection Refused / Timeout
    Adapter --> AISvc: throw ServiceUnavailableException
    AISvc --> ChatCtrl: exception_mapped
    ChatCtrl --> Student: 503 Service Unavailable
end
@enduml