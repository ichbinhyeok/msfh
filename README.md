# My Safe Florida Home Verdict

Working internal project: `MySafeFloridaHomeVerdict`  
Suggested package root: `owner.mysafefloridahome`

**Date:** 2026-04-13 (Asia/Seoul)  
**Purpose:** This folder now contains a working Spring Boot prototype plus the design packet for a Florida-focused **post-inspection decision and contractor-routing site** around the My Safe Florida Home program workflow.

## What you are building
A Florida-only decision site for homeowners who already touched the program and now need the next step:

- the initial inspection report is in hand
- recommended improvements need interpretation
- one upgrade must be chosen first
- contractor quotes need to be compared safely
- the homeowner needs to understand what is grant-aligned and what is not

The product should tell the user:

- what the report is actually saying
- which recommended improvement should go first
- what the grant can and cannot cover
- what quote and contractor mistakes to avoid
- whether they need a roofer, window and shutter contractor, or a roof retrofit specialist first

## Phase 1 launch wedge
Phase 1 is narrower than the full MSFH category.

The launch wedge is:

- Florida homeowner with an initial inspection report or immediate post-inspection context
- recommended improvement exists or likely exists
- user is deciding project priority, contractor type, and quote path
- attached-home versus detached-home logic is handled explicitly when it changes eligible scope

This means the first public build should behave more like:

- `post-inspection decision engine for MSFH homeowners`

and less like:

- `complete MSFH FAQ mirror`
- `generic Florida hurricane-hardening blog`

## Why this concept is attractive
- Official demand already exists because the state program drives inspections and grant applications.
- The program explicitly pushes contractor selection burden onto the homeowner.
- The question after the report is not fully answered by official FAQs.
- Lead value is meaningful because roofing, opening protection, and retrofit work are real-ticket categories.
- The site can stay form-first and email-first without phone-heavy operations.

## Product thesis
Do not build `How do I qualify for My Safe Florida Home?`

Build a **post-inspection decision engine** for homeowners trying to answer:

- What should I do after this report?
- Which recommended improvement should I start with?
- What does the program actually pay for?
- What should I ask contractors before I lock in scope?
- Which contractor type should I contact first?

## Source-grounded market proof
Current official and quasi-official program materials indicate:

- free wind-mitigation inspections are available through the program
- grants of up to `$10,000` remain central to the offer
- inspection and grant remain separate steps
- current program pages emphasize post-report choices, contractor selection, and insurance savings
- Florida Senate and CFO materials describe the program as already very large in inspections, grants, and reimbursements

## File map
- `AGENT_START_HERE.md` - read order and handoff rules for any future agent
- `ops/context_tracker.md` - current status, decisions, and next tasks
- `ops/wedge_focus_2026-04-13.md` - current primary wedge and narrow operating loop for the first build phase
- `ops/source_audit_2026-04-13.md` - official-source anchor map and how each source should shape the product
- `ops/persona_council_2026-04-13.md` - forced debate across demand, SERP, funnel, risk, sponsor, and portfolio perspectives
- `ops/promotion_review_system_2026-04-13.md` - how future agents should review metrics and recommend route promotion
- `ops/route_promotion_board.md` - current held-route board and recommendation status
- `spec/00_strategy.md` - market thesis, positioning, wedge, and rollout philosophy
- `spec/01_query_and_user_map.md` - jobs-to-be-done, trigger states, query families, and first user map
- `spec/02_site_architecture.md` - canonical entities, URL graph, route families, and internal linking
- `spec/03_data_and_operations.md` - data model, source hierarchy, verification workflow, and refresh cadence
- `spec/04_commercial_model.md` - CTA logic, partner types, lead intake, and sponsor packaging
- `spec/05_editorial_rules_and_execution.md` - writing rules, trust guardrails, and page-family ship criteria
- `spec/06_indexing_quality_and_analytics.md` - indexing gates, route quality rules, and measurement plan
- `spec/07_technical_architecture.md` - system boundaries, package map, rendering model, and services
- `spec/08_delivery_and_handoff.md` - workstreams, milestones, and implementation order
- `spec/09_launch_surface_and_route_inventory.md` - first launch-surface page inventory
- `spec/10_acceptance_test_matrix.md` - launch-critical tests and definition of done

## Recommended build stack
- `Spring Boot` + `jte`
- Server-rendered program and improvement pages with file-backed content
- File-based pipeline using raw `CSV` plus normalized and derived `JSON`
- No runtime database in phase 1
- Java runtime baseline: `21`

## Current implementation state
- Spring Boot plus jte scaffold in place
- File-backed normalized and derived content seeds in place
- Core public routes, held support routes, sitemap, robots, and trust pages rendering
- Lead form storage and event logging writing to local CSV files
- Acceptance pass tightened around phase-1 route coverage, partner-type routing, source-stack presence, and route-health review
- Acceptance coverage now sweeps all non-admin public routes, event context storage, error-state rendering, stale-source fail-closed behavior, and redirect safety
- Home, program, improvement, and guide routes now share an explicit decision-engine frame: interpret, choose, then prepare the quote path

## Persistent CSV storage in production
- Runtime CSV data should not live inside the deployed release folder.
- For an Oracle VM, use absolute paths outside the app directory, such as `/var/lib/mysafefloridahome/...`.
- The app now accepts storage paths through environment variables:
  - `APP_STORAGE_LEADS_PATH`
  - `APP_STORAGE_EVENTS_PATH`
  - `APP_STORAGE_PARTNER_INQUIRIES_PATH`
- Keep GitHub Actions deploy output under something like `/opt/mysafefloridahome/current`, but keep CSV files under `/var/lib/mysafefloridahome`.
- Detailed server notes: `ops/oracle_vm_persistent_storage_2026-04-14.md`

## Recommended launch geography
Start with `Florida` statewide routes only.

Reason:

- the demand source is a single statewide program
- official rules are state-level
- county overlays are only justified later by contractor density, local income thresholds, or measured lead behavior

## Core route families
- program inspection-report guide
- project selection guide
- contractor quote and confirmation guide
- improvement-specific decision guides
- what-MSFH-will-pay-for guide
- support and problem-state guides
- selective county overlays later

## Phase 1 launch families
- inspection report guide
- choose-project guide
- contractor quote guide
- what-the-program-will-pay-for guide
- improvement pages:
  - opening protection
  - roof-to-wall
  - roof-deck attachment
  - secondary water resistance
  - roof replacement under SWR logic
- evergreen guides tied to report interpretation and quote preparation

Everything else should start as support-layer or `noindex` inventory until the first wedge proves traction.

## Recommended monetization order
1. Opening protection contractor leads
2. Roofing and SWR-related leads
3. Roof-to-wall and retrofit-specialist leads
4. Metro or county sponsor packages once route-level demand is proven

## Portfolio position
This is a strong `priority-2` build candidate behind `BuriedOilTankVerdict`.

Why:

- speed to first cash can be strong
- lead values are meaningful
- but branded SERPs and trust burden are slightly harsher than buried oil tank

## Agent read order
1. `AGENT_START_HERE.md`
2. `ops/context_tracker.md`
3. `ops/persona_council_2026-04-13.md`
4. This file
5. `spec/00_strategy.md` through `spec/10_acceptance_test_matrix.md`

## Build principles
- This is a post-inspection decision product, not a general MSFH explainer.
- The winning moment starts after the homeowner sees the report.
- Program facts and contractor routing must stay visibly separate.
- Every page must answer:
  - what this report or recommendation probably means
  - what the homeowner should verify next
  - what the program will and will not pay for
  - which contractor type to talk to first
  - what mistake to avoid
- Old PDFs can be useful for background, but current Support Center rules win when the program changes.
