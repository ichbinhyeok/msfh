# Context Tracker

## Current status
- Spring Boot plus jte app scaffold created.
- File-backed content, route seeds, lead capture, event logging, and admin summary are implemented.
- Wedge locked around Florida homeowners who already reached the initial-inspection or immediate post-inspection stage of the MSFH workflow.
- Core public and held-support routes render from normalized JSON source records.

## Latest decisions
- Canonical page unit is `Florida + post-inspection decision route`.
- Phase 1 public focus is not eligibility. It is `report understanding + project choice + quote preparation`.
- Current program facts should be grounded in the live website, Support Center, and recent official state materials before old PDFs.
- Initial monetization should prioritize opening protection and roof-related contractor routing, not generic directories.
- County overlays should only ship when contractor density, local threshold differences, or measured demand justify them.
- Phase 1 implementation will stay file-backed with `storage/leads/*.csv` for submissions and events before any database is introduced.
- Held support routes can render now, but they remain `noindex` until promotion evidence exists.
- Purchased production domain is `scopeverdict.com`, and production canonical URLs should resolve through `APP_BASE_URL=https://scopeverdict.com`.

## What changed this session
- Scaffolded the Spring Boot `4.0.5` plus `jte` application with Maven wrapper under `owner.mysafefloridahome`.
- Added normalized source, program, improvement, guide, trust, route, and ops seed files under `src/main/resources/data`.
- Implemented server-rendered home, program, improvement, guide, trust, sitemap, robots, and admin routes.
- Implemented file-backed lead capture and event logging to `storage/leads/leads.csv` and `storage/leads/lead_events.csv`.
- Added partner-type routing, stale-route fail-closed review logic, and admin route-health summaries.
- Tightened improvement-route copy so phase-1 public pages now expose the `Quick answer` and `What not to assume` blocks explicitly.
- Expanded MockMvc coverage to sweep all phase-1 public routes for acceptance blocks, metadata, email-first lead forms, admin summaries, and stale-source fail-closed behavior.
- Expanded acceptance coverage again to all non-admin public routes, guide-to-program entry paths, lead error flash rendering, event-context storage, admin signal visibility, and risky redirect rejection.
- Tightened `ContentRepository` so stale official sources now fail closed for all route recommendations, including indexable routes.
- Added a shared `decision engine` route module across home, program, improvement, and guide pages so the product reads as a decision system instead of a FAQ mirror.

## Next recommended tasks
- Start the app and run browser QA on mobile and desktop widths against the rendered public routes.
- Replace seed editorial copy with reviewed production copy route by route, starting with the four public program routes.
- Add more route-status rows for every launch route and wire admin totals to real Search Console or analytics sources later.
- Decide whether the public-facing name should stay close to `My Safe Florida Home` or move one layer more neutral before launch.
- Add county overlay seeds only after statewide routes show demand and partner coverage.

## Open questions
- Should the public-facing brand reference `My Safe Florida Home` directly or stay one level more neutral to reduce trust friction?
- Should county overlays start with South Florida opening-protection demand or remain statewide until real lead data exists?
- Should the first commercial emphasis be `opening protection` or `roof + SWR`?
- Should low-income no-match scenarios get their own UX fork later, or stay merged into one decision engine at launch?
- Should production keep runtime jte compilation enabled or switch to precompiled templates before deployment?
