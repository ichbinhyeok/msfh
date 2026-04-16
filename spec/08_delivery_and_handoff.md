# 08 Delivery And Handoff

## Delivery objective
Ship a narrow but commercially credible first version that can:

- render program and improvement pages
- route users by report or recommendation state
- capture leads
- log enough route analytics to learn quickly

## Workstreams

### Workstream 1 - Scaffold
- create Spring Boot plus jte app
- create package root
- create data loading skeleton
- create public trust pages

### Workstream 2 - Data and routes
- create current program record
- create improvement records
- generate route inventory
- wire canonical and robots behavior

### Workstream 3 - Commercial routing
- create CTA logic
- create lead form and persistence
- create event logging
- create minimal admin view
- create route-status and promotion-review surfaces

### Workstream 4 - QA and launch
- render checks
- metadata checks
- lead form smoke checks
- mobile and desktop visual QA

## Implementation order
1. scaffold app and route inventory
2. implement home and trust rendering
3. implement core program routes
4. implement improvement routes
5. implement guides
6. implement CTA routing and lead capture
7. implement admin review and route-promotion review
8. run end-to-end QA

## Phase gates

### Gate 1
- home renders
- one program route renders
- one improvement route renders
- route inventory exists

### Gate 2
- all phase-1 program routes render
- all phase-1 improvement routes render
- source stack visible

### Gate 3
- lead capture works
- CTA tracking works
- trust pages exist
- route-status review exists

### Gate 4
- first launch surface passes acceptance matrix

## Handoff checklist for any future agent
- update `ops/context_tracker.md`
- update route inventory if route families change
- update source audit when official anchor sources change
- note any route families intentionally held `noindex`
- review `ops/route_promotion_board.md` if metrics exist
- record whether any held route should be promoted, built, or still held

## Scope discipline
If implementation pressure appears, cut breadth before quality:

- fewer support routes
- fewer guides
- later county overlays

Never do the reverse.
