# 10 Acceptance Test Matrix

## Goal
This matrix defines what must work before the first public release can be considered credible.

## Rendering tests
- Home route renders with post-report entry modules
- Every phase-1 program route renders without missing source or CTA blocks
- Every phase-1 improvement route renders without missing source or CTA blocks
- Guide pages render with at least one program-entry path

## Metadata tests
- Every indexable route emits a canonical URL
- Every held route emits `noindex`
- Sitemap includes only indexable routes
- Robots file blocks admin and API paths
- Utility surfaces such as quote-prep entry, builder, result, share, and export stay `noindex` and stay out of the sitemap

## Content-quality tests
- Every public page contains:
  - quick answer
  - what-not-to-assume section
  - official source stack
  - primary CTA
- No page claims grant certainty or official affiliation

## Lead-flow tests
- Lead form opens from every core program and improvement route
- Lead submit success writes storage
- Lead submit error state renders cleanly
- Improvement type changes partner-type routing correctly

## Analytics tests
- CTA click logging records route family and improvement type
- Lead events store scenario and route context
- Admin summary shows lead counts and click counts
- Route-status review shows whether held routes are `hold`, `recommend_promote`, `recommend_build`, or `recommend_demote`

## Mobile and UX tests
- home works on mobile width
- program and improvement pages keep primary CTA visible without layout breakage
- forms stay email-first and do not require phone
- home provides a clear path into the opening-protection quote-prep tool
- contractor-quotes program route provides a clear path into the same quote-prep tool when the narrow first quote is really opening protection
- opening-protection improvement route provides a clear path into the same quote-prep tool
- quote-prep builder, result, and share surfaces remain readable and usable without exposing internal office tools on the public-facing path

## Source freshness tests
- stale official sources block index promotion
- stale route records appear in admin review
- promotion recommendations fail closed when source freshness is stale

## Launch pass condition
Release is acceptable only if:

1. all phase-1 public pages pass rendering and metadata tests
2. all public pages contain source stacks and next-action CTAs
3. lead capture is working end to end
4. no route family depends on unsupported or vague claims
