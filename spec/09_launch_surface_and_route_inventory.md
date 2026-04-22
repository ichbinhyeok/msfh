# 09 Launch Surface And Route Inventory

## Launch rule
The first public version should feel sharp and useful after the inspection report, not broad and procedural.

## Core route families
- program decision
- improvement decision
- evergreen guide
- trust

## Phase 1 index rule
Only these route families should index in the first release:

- program decision
- improvement decision
- selected evergreen guides
- trust

`no-recommended-improvements`, `group-5`, `RFI/status`, `final inspection and draw-request`, and `county overlays` routes should exist only as support inventory or later launch work until the first wedge proves traction.

## Initial indexable surface

### Home and trust
1. `/`
2. `/about/`
3. `/methodology/`
4. `/contact/`
5. `/not-government-affiliated/`

### Core program routes
6. `/program/inspection-report/`
7. `/program/choose-project/`
8. `/program/contractor-quotes/`
9. `/program/what-msfh-will-pay-for/`

### Improvement routes
10. `/improvements/opening-protection/`
11. `/improvements/roof-to-wall/`
12. `/improvements/roof-deck-attachment/`
13. `/improvements/secondary-water-resistance/`
14. `/improvements/roof-replacement-under-swr/`

### Evergreen guides
15. `/guides/msfh-inspection-report-what-next/`
16. `/guides/impact-windows-vs-shutters/`
17. `/guides/msfh-contractor-quote-checklist/`
18. `/guides/roof-replacement-through-msfh/`
19. `/guides/opening-protection-quote-checklist/`
20. `/guides/roof-to-wall-quote-checklist/`
21. `/guides/swr-roof-quote-checklist/`
22. `/guides/attached-home-scope-under-msfh/`
23. `/guides/no-recommended-improvements-what-next/`
24. `/guides/msfh-rfi-response-checklist/`
25. `/guides/msfh-group-5-what-next/`
26. `/guides/msfh-portal-statuses-explained/`
27. `/guides/final-inspection-draw-request-checklist/`

## First `noindex` support surface
- `/program/no-recommended-improvements/`
- `/program/group-5/`
- `/program/rfi/`
- `/program/portal-statuses/`
- `/program/final-inspection-and-draw-request/`
- `/counties/{county}/opening-protection/`
- `/counties/{county}/roofing/`

## First `noindex` utility surface
These routes are part of the product, not part of the indexed SERP surface.

- `/tools/opening-protection/quote-prep-brief/`
- `/tools/opening-protection/quote-prep-brief/build/`
- `/tools/opening-protection/quote-prep-brief/result/{internalToken}/`
- `/tools/opening-protection/quote-prep-brief/share/{publicToken}/`
- `/tools/opening-protection/quote-prep-brief/share/{publicToken}/export/pdf/`

### Utility-surface purpose
- home can send a homeowner into quote prep without turning the tool into a search landing page
- the `contractor-quotes` program route can catch search traffic and hand opening-protection users into the same tool without indexing the tool itself
- the opening-protection improvement route can route users into the same tool when the recommendation is already clear
- the public-facing utility stays narrow while tokenized result surfaces remain out of search
- tokenized and operational pages stay out of the sitemap and out of index coverage

## Why this launch surface is enough
- captures the strongest post-report trigger states
- stays out of pure eligibility clutter
- gives enough pages to test internal linking
- supports multiple homeowner decision paths without bloating the surface

## Expansion order after launch
1. Promote the best-performing support route if evidence appears
2. Add selective county overlays for opening protection or roofing
3. Add one support-state guide family
4. Add more homeowner-facing decision or support guidance only if real route-level demand exists
