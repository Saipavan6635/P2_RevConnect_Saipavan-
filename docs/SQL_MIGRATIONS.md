# SQL Migrations

Use these scripts when promoting schema changes in controlled environments.

## V2 - User Profile Extensions

Script path:

- `src/main/resources/db/migration/V2__add_user_external_links_and_offerings.sql`

Purpose:

- Adds `EXTERNAL_LINKS` to `USERS`
- Adds `OFFERINGS` to `USERS`

Notes:

- The script is Oracle-compatible and idempotent (safe to run repeatedly).
- In local dev, Hibernate `ddl-auto=update` may already create columns automatically.
- In staging/production, run SQL migration scripts explicitly.
