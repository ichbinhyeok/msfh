# 03 Data And Operations

## Data philosophy
The moat is not broad MSFH prose. The moat is a verified, current decision dataset that stays aligned with the live program.

## Recommended raw data files
- `data/raw/program/program-facts.csv`
- `data/raw/improvements/improvement-rules.csv`
- `data/raw/guides/guides.json`
- `data/raw/counties/county-overlays.csv`
- `data/raw/partners/partner-types.csv`
- `data/raw/sources/*.json`

## Recommended normalized outputs
- `data/normalized/program/current.json`
- `data/normalized/improvements/{slug}.json`
- `data/normalized/counties/{county}.json`
- `data/normalized/sources/{sourceId}.json`
- `data/derived/routes.json`

## Recommended ops files
- `data/ops/route-status.csv`
- `data/ops/promotion-review.json`
- `data/ops/admin-metrics-snapshot.json`

These exist so a future agent can review route status and recommend promotion without relying on human memory.

## Program record schema
Each program record should capture:

- active public workflow notes
- current inspection-grant separation notes
- current matching-grant notes
- current low-income notes
- contractor-responsibility disclaimer summary
- prioritization notes
- source ids
- verified date
- next review date

## Improvement record schema
Each improvement record should capture:

- improvement slug
- display label
- what the recommendation means
- grant-alignment rules
- attached-home or townhouse caveats
- common confusion points
- contractor type
- directional cost notes if official support exists
- source ids
- verified date
- next review date

## County overlay schema
Only use when needed.

Fields:

- county name
- why the overlay exists
- what answer changes locally
- contractor density score
- route families allowed

## Partner type schema
Fields:

- partner type slug
- display label
- what problem it solves
- what trigger state it matches
- lead priority

## Route-status schema
Each route record should capture:

- route id
- route path
- route family
- state or county scope
- phase:
  - phase_1_public
  - held_support
  - future_candidate
- index status:
  - index
  - noindex
  - not_built
- source freshness status
- last 28-day impressions
- last 28-day clicks
- last 28-day ctr
- last 28-day cta clicks
- last 28-day lead opens
- last 28-day lead submissions
- dominant improvement type
- promotion recommendation:
  - hold
  - recommend_promote
  - recommend_build
  - recommend_demote
- recommendation reason
- reviewed on
- next review on

## Promotion-review schema
Each review artifact should capture:

- review date
- data window
- agent summary
- promoted candidate routes
- held routes still not ready
- routes to demote or merge
- blockers:
  - source gap
  - no decision value
  - weak CTA behavior
  - weak lead evidence
  - weak partner coverage

## Source workflow
1. Capture raw official source URLs and notes
2. Normalize into structured source records
3. Link source ids to program and improvement records
4. Render routes only from normalized records
5. Fail route promotion when `verifiedOn` or `nextReviewOn` rules are stale

## Source hierarchy
1. Current official program and Support Center pages
2. Florida Senate or DFS/CFO official updates
3. Legacy homeowner guides and PDFs
4. Secondary SERP and contractor pages for market checks only

## Review cadence
- core program routes: every `30` days
- held support routes: every `45` days
- partner roster: every `30` days once launched
- county overlays: only after statewide traction

## Operational principles
- Do not create data records for support routes you are not ready to support editorially.
- Do not create county overlays without a written `why this changes the answer` note.
- If a program rule changed and the source stack is mixed, current support articles win over older PDFs.

## Launch data backlog
Phase 1 should complete:

- one current program record
- five improvement records
- at least four evergreen guide records
- one route manifest covering program routes and core improvement routes

## Ops review needs
The eventual admin or ops page should expose:

- stale source records
- routes blocked from index
- leads by improvement type
- CTA click counts by route family
- support routes with repeated demand
- county overlay gaps
