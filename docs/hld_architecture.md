# High-Level Design (HLD)

## Basic System-Design Decisions
* **Scalability**: The system is designed as a modular monolith. Read-heavy endpoints, such as listing placement drives (`GET /api/drives`), can be horizontally scaled in the future behind a load balancer. 
* **Consistency**: Application creation and duplicate prevention require strong consistency (CP). We process these synchronously to prevent race conditions.
* **Availability**: Core placement APIs operate entirely independently of the LLM. If the Ollama service goes offline, core student applications will not be blocked.
* **Caching**: The generic FAQ responses and public drive listings are prime candidates for caching, with an expiry rule of 5 minutes to reduce database/LLM hits.

## Architecture Diagram Code
```plantuml
@startuml
!theme plain
node "Client Layer" {
  [Student / Admin Web Client] as Client
}

node "Spring Boot REST Application" {
  [API Entry / Controllers] as Entry
  [Profile & Company Module] as Profile
  [Eligibility & Application Module] as AppLogic
  [Career Assistant Module] as AIModule
  
  Entry --> Profile
  Entry --> AppLogic
  Entry --> AIModule
}

database "In-Memory / SQL DB" as DB {
  [Application Data]
}

node "Ollama Local AI Server" as Ollama {
  [Llama Model]
}

Client --> Entry : HTTP REST
Profile --> DB : Read/Write
AppLogic --> DB : Read/Write
AIModule --> Ollama : HTTP POST (Configured Timeout)
@enduml