# 04 Public Growth Model

## Public thesis
This project should grow from **clear homeowner decisions and reusable quote-prep support**, not from display ads, broad directories, or contractor-first framing.

## Primary value paths

### Opening-protection guidance
Best when:

- the report recommends opening protection
- the homeowner is comparing windows, shutters, doors, or garage-door scope

Why it matters:

- direct homeowner urgency
- quote comparison is a real pain point
- a reusable brief reduces confusion before outreach begins

### Roofing and SWR guidance
Best when:

- the report or project scope points toward SWR or reroof-related work
- the homeowner needs to understand whether roof replacement is actually part of the eligible scope

### Roof-retrofit guidance
Best when:

- roof-to-wall or roof-deck work is the real recommendation
- the homeowner needs a narrower contractor-type explanation rather than a generic roofer page

## Paths to avoid first
- generic statewide contractor directory
- phone-heavy grant consulting positioning
- `free roof` or `free windows` bait
- pre-inspection eligibility capture

## Phase 1 scope
For the first public release, the center of gravity should stay on:

- report received
- recommendation interpretation
- improvement-specific quote-prep and contractor-type routing

Group-5, no-recommendations, RFI, and deep disbursement support should exist in the model, but they should stay support-layer until the first wedge proves that report and improvement routes are truly helping homeowners finish the next step cleanly.

## CTA matrix by scenario

### Report received but unsure
Primary CTA:

- `Get the right contractor type for this recommendation`

Secondary CTA:

- `See what the program will and will not pay for`

### Opening protection recommendation
Primary CTA:

- `Build the opening-protection quote-prep brief`

Secondary CTA:

- `See the right contractor type for this work`

### Roof-related recommendation
Primary CTA:

- `Build the roof-related quote-prep brief`

Secondary CTA:

- `See the right roofing or retrofit contractor type`

### Support-state frustration
Primary CTA:

- `See the corrective next step`

Secondary CTA:

- none by default

## Recommended intake fields
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
- `opening_protection_decision` -> quote-prep brief first, contractor-type path second
- `roof_related_decision` -> quote-prep brief first, contractor-type path second
- `contractor_confirmation_needed` -> contractor-type path first

Public rule:

- if the home is attached or treated like a townhouse, do not route the user into overbroad roof-replacement messaging before the program-scope caveat is made explicit

## Public quality gates
- no promoted placement assumptions on pages that still lack route clarity
- no quote CTA on pages that still need pure procedural explanation
- no `free roof` or `guaranteed reimbursement` language anywhere
- no page should read like a contractor pitch before the homeowner understands the recommendation

## Early success signal
The earliest success signal should be:

1. homeowners reach a narrow improvement path after reading the report
2. the quote-prep brief gives them a reusable first-send asset
3. route-family demand is clear enough to justify deeper homeowner support

## Biggest trap
Do not turn every MSFH page into a contractor pitch.

The value is that the site helps the homeowner decide first, then prepare, then route.
