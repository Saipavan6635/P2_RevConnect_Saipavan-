# Application Architecture

```mermaid
flowchart LR
    UI[Thymeleaf UI + Static JS/CSS]
    CTRL[Spring MVC Controllers]
    SVC[Service Layer]
    REPO[Spring Data JPA Repositories]
    DB[(Oracle SQL)]

    UI --> CTRL
    CTRL --> SVC
    SVC --> REPO
    REPO --> DB

    API[REST/JWT API Controllers] --> SVC
    SEC[Spring Security + JWT Filter] --> CTRL
    SEC --> API
```

## Layers

- Controller layer: handles web pages and REST endpoints.
- Service layer: business rules (authorization checks, feed, notifications).
- Repository layer: persistence queries and specifications.
- Security layer: form login for web + JWT for `/api/**`.

