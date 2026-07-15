# REST API Contract

| Method | Path | Purpose | Key Status Codes |
| :--- | :--- | :--- | :--- |
| POST | `/api/students` | Create student | 201 Created, 400 Bad Request, 409 Conflict |
| GET | `/api/students/{id}` | Retrieve student | 200 OK, 404 Not Found |
| POST | `/api/companies` | Create company | 201 Created, 400 Bad Request, 409 Conflict |
| GET | `/api/companies/{id}` | Retrieve company | 200 OK, 404 Not Found |
| POST | `/api/drives` | Create placement drive | 201 Created, 400 Bad Request |
| GET | `/api/drives` | List all drives | 200 OK |