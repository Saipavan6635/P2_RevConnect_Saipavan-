# RevConnect P2

RevConnect is a full-stack Spring Boot social media web application supporting personal, creator, and business accounts.

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA (Hibernate)
- Thymeleaf + HTML/CSS/JS
- Oracle SQL (or compatible SQL database)
- Maven
- Log4J2
- JUnit4 + Mockito

## Core Features

- Authentication (register/login/logout) with role-based accounts
- Profile management (privacy, bio, location, website, avatar)
- Post management (create/edit/delete, hashtags, image upload, repost/share)
- Social interactions (likes, comments, shares)
- Network features (personal-to-personal connections, follows)
- In-app notifications and notification preferences
- Feed with trending hashtags and filters
- Messaging inbox and conversations
- Analytics dashboard (post/account metrics)
- Scheduled post publishing and pinned posts

## Account Rules

- `PERSONAL` users can connect with other `PERSONAL` users.
- Users can follow only `CREATOR` or `BUSINESS` accounts.
- `CREATOR` and `BUSINESS` accounts can create advanced posts (CTA/scheduling/pinning).

## Run Locally

1. Configure database settings in `src/main/resources/application.properties`.
2. Build and run:

```bash
mvn clean spring-boot:run
```

3. Open `http://localhost:8080`.

## Testing

Run tests:

```bash
mvn test
```

Service-level tests are available under `src/test/test/com/rev/app/service`.

## Architecture and ERD

- Architecture: [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)
- ERD: [docs/ERD.md](docs/ERD.md)

