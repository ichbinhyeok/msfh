# 07 Technical Architecture

## Build goal
Implement a server-rendered decision site with a file-backed content and data pipeline, minimal operational drag, and simple lead capture.

## Suggested package root
- `src/main/java/owner/mysafefloridahome`
- `src/test/java/owner/mysafefloridahome`

## Suggested package map
- `owner.mysafefloridahome.data`
- `owner.mysafefloridahome.ingest`
- `owner.mysafefloridahome.pages`
- `owner.mysafefloridahome.leads`
- `owner.mysafefloridahome.web`
- `owner.mysafefloridahome.ops`

## Core application services

### Repository layer
- loads normalized program, improvement, guide, and source records
- no runtime database

### Route builder
- turns program and improvement records into a canonical route inventory
- applies index or noindex defaults

### Page service
- builds home, program, guide, and improvement view models
- centralizes CTA logic by scenario and improvement type

### Lead service
- receives lead submissions
- writes CSV or JSONL storage
- exposes simple aggregate views for admin later

### Event logger
- logs CTA clicks and form events
- keeps enough route context for later analysis

## Recommended storage
- `storage/leads/leads.csv`
- `storage/leads/lead_events.csv`
- `storage/ops/*.json`

## Recommended templates
- `home.jte`
- `program.jte`
- `improvement.jte`
- `guide.jte`
- `admin.jte`

## Recommended data files
- `src/main/resources/data/normalized/program/current.json`
- `src/main/resources/data/normalized/improvements/*.json`
- `src/main/resources/data/normalized/guides/*.json`
- `src/main/resources/data/derived/routes.json`

## Suggested runtime endpoints
- `GET /`
- `GET /program/{route}/`
- `GET /improvements/{slug}/`
- `GET /guides/{slug}/`
- `POST /api/leads/capture`
- `POST /api/leads/event`
- `GET /admin`

## Technical constraints
- no user accounts
- no runtime relational database
- no full contractor marketplace in phase 1
- no automated live sync to program systems

## Testing requirements
- route rendering tests
- canonical and robots tests
- lead capture tests
- route inventory tests
- CTA logic tests by scenario and improvement type

## Architecture principle
Keep the code aligned with the decision engine:

- report or recommendation state in
- next action out

Do not bury that logic in templates.
