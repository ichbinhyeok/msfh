# 04 Commercial Model

## Commercial thesis
This project should monetize from **high-intent improvement and contractor routing**, not from display ads and not from a giant directory.

## Primary revenue paths

### Opening-protection leads
Best when:

- report recommends opening protection
- user is comparing windows, shutters, doors, or garage-door scope

Why it matters:

- high contractor value
- direct homeowner urgency
- quote comparison is a real pain point

### Roofing and SWR leads
Best when:

- the report or project scope points toward SWR or reroof-related work
- the user needs to understand whether roof replacement is really part of the eligible scope

### Roof-retrofit leads
Best when:

- roof-to-wall or roof-deck work is the real recommendation
- user needs a specialized contractor path rather than a generic roofer

## Revenue paths to avoid first
- generic state-wide contractor directory
- phone-heavy grant consulting
- `free roof` or `free windows` bait
- pre-inspection eligibility lead capture

## Phase 1 commercial scope
For the first public release, the commercial center of gravity should stay on:

- report received
- recommendation interpretation
- improvement-specific quote and contractor routing

Group-5, no-recommendations, RFI, and deep disbursement support should exist in the model, but they should stay support-layer until the first wedge proves that report and improvement routes can earn clicks and submissions cleanly.

## CTA matrix by scenario

### Report received but unsure
Primary CTA:

- `Get the right contractor type for this recommendation`

Secondary CTA:

- `See what the program will and will not pay for`

### Opening protection recommendation
Primary CTA:

- `Get the opening-protection quote checklist`

Secondary CTA:

- `Find a licensed window or shutter contractor`

### Roof-related recommendation
Primary CTA:

- `Get the roof-related quote checklist`

Secondary CTA:

- `Find the right roofing or retrofit contractor type`

### Support-state frustration
Primary CTA:

- `See the corrective next step`

Secondary CTA:

- none by default

## Recommended lead form fields
- county
- zip
- scenario
- improvement type
- report received yes or no
- attached or detached home
- budget range
- timeline or urgency
- email
- phone optional, not required
- consent checkbox

## Routing logic
- `report_received_need_next_step` -> decision page plus improvement selector
- `attached_home_scope_constraint` -> opening-protection interpretation first
- `opening_protection_decision` -> checklist first, contractor path second
- `roof_related_decision` -> checklist first, contractor path second
- `contractor_confirmation_needed` -> contractor path first

Commercial rule:

- if the home is attached or treated like a townhouse, do not route the user into overbroad roof-replacement messaging before the program-scope caveat is made explicit

## Sponsor packaging hypothesis
Do not lock pricing too early, but structure the inventory as:

1. improvement-category sponsorship
2. selective metro or county sponsorship later
3. premium placement for roofing or opening-protection high-intent routes

## Early economics hypothesis
Assumption only, not fact:

- opening-protection lead value: high
- roofing or SWR lead value: high
- roof-retrofit specialist lead value: medium to high

The business does not need huge volume if lead quality is sharp and scoped to recommendation type.

## Commercial quality gates
- no sponsor placement on pages that do not yet have route clarity
- no quote CTA on pages that still need pure procedural explanation
- no `free roof` or `guaranteed reimbursement` language anywhere

## First-cash model
The first-cash model should be:

1. a small number of improvement-specific routed leads
2. sponsor outreach once a few routes show demand and clicks
3. later improvement-category or metro packages if route-family traction appears

## Biggest commercial trap
Do not turn every MSFH page into a contractor pitch.

The value is that the site helps the homeowner decide first, then route.
