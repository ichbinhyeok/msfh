# 01 Query And User Map

## Primary user segments

### Homeowner with report in hand
Questions:

- What does this inspection report actually mean?
- Which recommendation should I do first?
- What is the smartest project if I cannot do everything?

Commercial value:

- very high

### Homeowner moving into contractor confirmation
Questions:

- What contractor type do I need?
- What should I ask in quotes?
- What should I avoid signing too early?

Commercial value:

- very high

### Opening-protection homeowner
Questions:

- Windows or shutters?
- Do I need every opening?
- What if some openings are already compliant?
- Does my attached home or townhouse change what can be funded?

Commercial value:

- high

### Roof-related homeowner
Questions:

- Does this mean full roof replacement?
- Is SWR the real reason roof work is being covered?
- Can roof-to-wall be done without full reroofing?

Commercial value:

- high to very high

### Frustrated support-state homeowner
Questions:

- Why do I have no recommendations?
- Why is my case group 5?
- What do I do with an RFI?

Commercial value:

- low to medium

## Trigger states
The site should always classify the user into one of these trigger states:

1. `report_received_need_next_step`
2. `comparing_recommended_improvements`
3. `contractor_confirmation_needed`
4. `opening_protection_decision`
5. `roof_related_decision`
6. `support_state_problem`
7. `attached_home_scope_constraint`

## Query families

### Report understanding
- my safe florida home inspection report what next
- my safe florida home recommended improvements
- where do i find recommended improvements msfh
- what does my safe florida home report mean

### Project choice
- which msfh project should i do first
- my safe florida home windows or roof first
- best improvement for my safe florida home grant
- what will msfh actually pay for

### Contractor and quote prep
- my safe florida home contractor confirmation
- my safe florida home contractor quotes
- my safe florida home choose contractor
- my safe florida home quote checklist

### Opening protection
- my safe florida home opening protection
- my safe florida home impact windows or shutters
- my safe florida home doors and garage doors
- my safe florida home all openings
- my safe florida home townhouse opening protection

### Roof-related
- my safe florida home roof replacement
- my safe florida home roof to wall
- my safe florida home roof deck attachment
- my safe florida home secondary water resistance

### Support and problem states
- my safe florida home no recommended improvements
- my safe florida home group 5
- my safe florida home rfi
- my safe florida home draw request

## Query strategy rules
- Always prefer `post-report decision` over generic branded process phrasing.
- The first SEO wins should come from long-tail judgment questions, not exact FAQ restatements.
- If a query can be answered completely by a single official support article and does not create a real next-step ambiguity, it is not a phase-1 priority route.

## Priority page families

### Tier 1
- inspection-report page
- choose-project page
- contractor-quote page
- what-the-program-pays-for page
- opening-protection page
- roof-related improvement pages

### Tier 2
- no-recommended-improvements page
- group-5 page
- RFI and status pages
- roof-replacement-under-swr guide variants
- improvement comparison guides

### Tier 3
- county overlays
- final inspection and draw-request help pages

## Query-to-route mapping
- `What does my report mean?` -> inspection-report page
- `Which project should I do first?` -> choose-project page
- `How do I choose a contractor safely?` -> contractor-quote page
- `What will the program pay for?` -> what-the-program-pays-for page
- `Windows, shutters, doors?` -> opening-protection page
- `Roof, SWR, clips, deck?` -> roof-related improvement page

Phase 1 rule:

- support-state routes are support routes first
- they should not displace the launch wedge built around report, project choice, and contractor-choice pages

## The user map the site should support

### Report path
1. Report arrives
2. User reads recommended improvements
3. User tries to interpret what matters most
4. User moves into project choice and quote preparation

### Opening-protection path
1. User sees opening-protection recommendation
2. User needs to know what openings qualify
3. User compares windows, shutters, doors, garage-door scope, and attached-home constraints
4. User moves into contractor quote checklist

### Roof path
1. User sees roof-related recommendation
2. User needs to know if reroofing is truly part of the eligible scope
3. User verifies SWR, roof deck, and roof-to-wall logic
4. User moves into roofing or retrofit contractor path

### Attached-home path
1. User realizes the home is attached or treated like a townhouse
2. User needs to know whether the recommendation scope narrows to opening protection
3. User is routed away from overbroad roof assumptions
4. User moves into the correct quote checklist

### Support-state path
1. User hits a dead end or confusing status
2. User needs explanation and corrective next step
3. User is routed back to the right program or decision path

## First keyword rule
If a page does not answer a `what should I do next after seeing this report or recommendation` question, it probably should not be in the launch surface.
