# 02 Site Architecture

## Canonical entities

### Program snapshot
The current-state operational program record.

Fields:

- program version notes
- current public workflow notes
- key grant rules
- prioritization notes
- contractor responsibility notes
- attached-home and townhouse scope notes

### Improvement type
A repeatable decision unit inside the program.

Core types:

- opening-protection
- roof-to-wall
- roof-deck-attachment
- secondary-water-resistance
- roof-replacement-under-swr-logic

### Route family
A repeatable decision path across the program.

Core families:

- inspection-report
- choose-project
- contractor-quotes
- what-the-program-pays-for
- improvement
- support-state
- county-overlay

### County overlay
A local page that only exists when contractor density, income-threshold interpretation, or real homeowner decision conditions materially change the answer.

### Contractor type
- opening-protection contractor
- roofing contractor
- roof retrofit specialist

## URL graph

### Core public routes
- `/`
- `/program/inspection-report/`
- `/program/choose-project/`
- `/program/contractor-quotes/`
- `/program/what-msfh-will-pay-for/`
- `/improvements/opening-protection/`
- `/improvements/roof-to-wall/`
- `/improvements/roof-deck-attachment/`
- `/improvements/secondary-water-resistance/`
- `/improvements/roof-replacement-under-swr/`

### Evergreen guides
- `/guides/msfh-inspection-report-what-next/`
- `/guides/impact-windows-vs-shutters/`
- `/guides/msfh-contractor-quote-checklist/`
- `/guides/roof-replacement-through-msfh/`

### Support and trust routes
- `/about/`
- `/methodology/`
- `/contact/`
- `/privacy/`
- `/terms/`
- `/not-government-affiliated/`

### Held support routes
- `/program/no-recommended-improvements/`
- `/program/group-5/`
- `/program/rfi/`
- `/program/portal-statuses/`
- `/program/final-inspection-and-draw-request/`
- `/counties/{county}/{route}/`

### Admin and ops
- `/admin/`
- `/admin/exports/*`

## Canonical rules
- Florida statewide program routes are canonical by default.
- County overlay pages are canonical only when they materially differ and have route-level evidence.
- Thin support routes stay `noindex`.
- Query-parameter variants must redirect to clean canonical URLs.

## Page modules

### Home
- wedge headline
- report-stage scenario selector
- attached-home or detached-home selector
- improvement entry points
- strongest trust guides
- clear explanation of what the site does not do

### Program decision page
- quick answer
- what this means in the MSFH workflow
- what the program will and will not cover
- attached-home or townhouse caveat when scope changes
- what not to assume
- next-step CTA
- official source stack

### Improvement page
- what this recommendation means
- when this is the right first project
- what scope usually qualifies
- what attached-home or townhouse cases change
- what can cause denial or confusion
- quote checklist and contractor-type CTA
- official source stack

### Support page
- corrective explanation
- best next step
- link back into the main decision wedge

## Internal linking rules
- Every guide links into at least two program routes and one improvement route.
- Every core program route links into the main improvement routes.
- Every improvement route links:
  - back to a program decision page
  - sideways to the next likely decision
  - down to a CTA
- Held support routes can be linked contextually, but should not displace the primary Phase 1 navigation.
- Opening-protection pages should link to contractor quotes and what-the-program-pays-for pages.
- Roof-related pages should link to contractor quotes and roof-replacement-under-swr explanation pages.

## CTA architecture
- Each page gets one primary CTA based on recommendation or decision state.
- Secondary CTA only exists when the next-best action differs materially from the primary path.
- CTAs should never offer every contractor category at once.

## Structured data
- `WebPage` on all public pages
- `BreadcrumbList` on program and guide routes
- `FAQPage` only when the page genuinely contains decision-specific FAQ blocks
- Avoid fake `LocalBusiness` schema on editorial pages

## Route-quality principle
If a route cannot clearly differ from a current official support article or a generic contractor landing page, it should not ship yet.
