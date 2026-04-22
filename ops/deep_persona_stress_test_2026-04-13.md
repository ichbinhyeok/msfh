# Deep Persona Stress Test - 2026-04-13

## Purpose
Pressure-test the `MySafeFloridaHomeVerdict` package as if multiple hard-edged specialists were trying to break it before implementation.

## Personas in the room
- Demand realist
- SERP realist
- Program operator
- Legal-risk realist
- Funnel operator
- Content strategist
- Trust designer
- Technical architect
- Ops pessimist
- Portfolio allocator
- Judge

## Findings

### P1 - Attached-home and townhouse scope needed stronger treatment
The source stack already said attached single-family homes are treated as townhouses and townhouses are only eligible for opening protection funding, but the core package did not force this fork early enough.

Risk:

- a future agent could overbuild roof-related flows for attached homes
- public pages could overstate eligible scope
- lead routing could become noisy and lower trust

Action taken:

- added explicit attached-home branching to the agent rules
- added an `attached_home_scope_constraint` trigger state
- updated architecture, editorial, and growth docs so attached-home cases are surfaced early

### P1 - Roof-replacement route name was too broad
The public route was still written as `/improvements/roof-replacement/`.

Risk:

- the route can drift into generic reroof demand
- SEO surface becomes broader and less truthful than the source stack supports

Action taken:

- renamed the route concept to `/improvements/roof-replacement-under-swr/`
- kept the route tightly tied to recommended work and SWR logic

### P3 - Source-audit text had encoding noise
A few lines in the source audit had broken characters.

Risk:

- low direct strategic risk
- but it weakens trust in a homeowner support packet

Action taken:

- cleaned the broken strings

## Debate

### Demand realist
- Pass.
- The main pain is still real and urgent after the report.

### SERP realist
- Pass.
- Narrowing roof replacement makes the search surface more defensible.

### Program operator
- Pass.
- The package now respects the attached-home constraint instead of leaving it as a footnote.

### Legal-risk realist
- Pass.
- This is safer now because eligibility-like overstatement risk is lower.

### Funnel operator
- Pass.
- Wrong-home-type users are less likely to fall into the wrong CTA path.

### Content strategist
- Pass.
- The docs still preserve the post-report wedge while reducing generic roofing drift.

### Trust designer
- Pass.
- The package better matches the anti-scam and non-affiliation posture.

### Technical architect
- Pass.
- The route system is still simple enough to implement.

### Ops pessimist
- Pass with discipline.
- County overlays and support routes still need to stay held.

### Portfolio allocator
- Pass.
- This remains a real build packet, not just a brainstorm.

### Judge
- Final verdict: `ready`
- Confidence: `8.9 / 10`

## Final conclusion
The `MySafeFloridaHomeVerdict` package is implementation-ready.

The main structural risks have been closed.
