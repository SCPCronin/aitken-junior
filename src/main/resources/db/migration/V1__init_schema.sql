-- =============================================================================
-- V1__init_schema.sql — Baseline schema migration
--
-- This is the first Flyway migration. It establishes the baseline schema
-- for the AitkenJunior application.
--
-- Flyway runs migrations in version order (V1, V2, V3...) and records each
-- execution in the 'flyway_schema_history' table. A migration will never
-- run twice on the same database.
--
-- Naming convention for future migrations:
--   V{number}__{description}.sql
--   e.g. V2__create_discord_users.sql
--        V3__create_notes_table.sql
-- =============================================================================

-- Enable the pgcrypto extension for UUID generation functions.
-- This is available in standard PostgreSQL and the postgres:alpine Docker image.
-- gen_random_uuid() (built-in since Postgres 13) is used by Hibernate for UUID PKs.
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

