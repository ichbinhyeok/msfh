@import owner.mysafefloridahome.pages.HomePageView
@param HomePageView page
!{var verdictTitle = page.decisionResult() != null ? page.decisionResult().title() : "Use the report, the home type, and the recommendation together";}
!{var verdictSummary = page.decisionResult() != null ? page.decisionResult().summary() : "The goal is to leave this step with one clear route, one likely contractor type, and one mistake you will not make.";}
!{var contractorType = page.decisionResult() != null ? page.decisionResult().contractorTypeLabel() : "Determined after the four inputs";}
!{var priorityRoute = page.decisionResult() != null ? page.decisionResult().primaryAction().label() : "Interpret before quote prep";}
!{var officialSummary = page.decisionResult() != null ? page.decisionResult().evidence().summary() : "Current program guidance, support-center rules, and route logic keep the decision path narrow.";}
!{var mistakeTitle = page.decisionResult() != null ? page.decisionResult().mistakeToAvoid() : "Do not let quote shopping outrun recommendation clarity.";}
!{var sourcePreviewCount = Math.min(3, page.sourceStack().size());}
!{var decisionMode = page.decisionInput().userInput() ? "Personalized route" : "Default route";}
!{var routeState = page.decisionResult() != null ? "Next move selected" : "Ready for four inputs";}
!{var sourceCoverage = sourcePreviewCount + (sourcePreviewCount == 1 ? " verified source" : " verified sources");}
!{var branchTitle = page.decisionResult() != null ? "Why this route is next" : "How the route guide branches";}
!{var branchIntro = page.decisionResult() != null
        ? "Read the branch in order: act now, hold the companion route nearby, and avoid the expensive detour."
        : "The default wedge is simple: read the report, choose the first project, then enter quote prep only after scope is clear.";}
@template.layout.page(
    meta = page.meta(),
    siteName = "My Safe Florida Home Verdict",
    currentPath = "/",
    bodyClass = "home-page",
    content = @`
        <main>
            @template.shared.pageSchema(
                meta = page.meta(),
                siteName = "My Safe Florida Home Verdict",
                about = "Florida post-inspection decision support after a My Safe Florida Home inspection report"
            )
            <section class="section console-stage" id="decision-tool">
                <div class="section-shell console-grid ${page.decisionInput().userInput() ? "decision-tool--focused" : ""}">
                    <div class="console-input-column">
                        <div class="console-heading">
                            <p class="console-label">Report decision guide</p>
                            <h1>Route your next step</h1>
                            <p class="lead">Use the report state, the closest recommendation, the home type, and your current question to get the safest next route.</p>
                            <p class="meta-line">Independent decision aid for Florida homeowners. This site is not the program, does not approve grants, and stores limited route inputs to improve next-step guidance. <a class="meta-link" href="/not-government-affiliated/">Why this matters</a></p>
                        </div>
                        <form class="decision-form console-form" method="get" action="/" data-home-tool="true">
                            <label class="console-field">
                                <span>Where are you right now?</span>
                                <select name="reportState">
                                    <option value="received-recommendation" selected="${"received-recommendation".equals(page.decisionInput().reportState())}">I have the report and a recommendation</option>
                                    <option value="received-no-recommendations" selected="${"received-no-recommendations".equals(page.decisionInput().reportState())}">I have the report but no recommendations</option>
                                    <option value="status-problem" selected="${"status-problem".equals(page.decisionInput().reportState())}">I am stuck on Group 5, RFI, or portal status</option>
                                    <option value="not-yet" selected="${"not-yet".equals(page.decisionInput().reportState())}">I do not have the report yet</option>
                                </select>
                            </label>
                            <label class="console-field">
                                <span>What recommendation seems closest?</span>
                                <select name="recommendationType">
                                    <option value="not-sure" selected="${"not-sure".equals(page.decisionInput().recommendationType())}">Not sure yet</option>
                                    <option value="opening-protection" selected="${"opening-protection".equals(page.decisionInput().recommendationType())}">Opening protection</option>
                                    <option value="roof-to-wall" selected="${"roof-to-wall".equals(page.decisionInput().recommendationType())}">Roof-to-wall</option>
                                    <option value="roof-deck-attachment" selected="${"roof-deck-attachment".equals(page.decisionInput().recommendationType())}">Roof deck attachment</option>
                                    <option value="secondary-water-resistance" selected="${"secondary-water-resistance".equals(page.decisionInput().recommendationType())}">Secondary water resistance</option>
                                    <option value="roof-replacement-under-swr" selected="${"roof-replacement-under-swr".equals(page.decisionInput().recommendationType())}">Roof replacement under SWR logic</option>
                                </select>
                            </label>
                            <label class="console-field">
                                <span>Home type</span>
                                <select name="homeType">
                                    <option value="detached" selected="${"detached".equals(page.decisionInput().homeType())}">Detached</option>
                                    <option value="attached" selected="${"attached".equals(page.decisionInput().homeType())}">Attached or townhouse</option>
                                </select>
                            </label>
                            <label class="console-field">
                                <span>What do you need most right now?</span>
                                <select name="priority">
                                    <option value="choose-project" selected="${"choose-project".equals(page.decisionInput().priority())}">Pick the first project</option>
                                    <option value="understand-report" selected="${"understand-report".equals(page.decisionInput().priority())}">Understand the report</option>
                                    <option value="compare-quotes" selected="${"compare-quotes".equals(page.decisionInput().priority())}">Compare quotes safely</option>
                                </select>
                            </label>
                            <button class="primary-button console-submit" type="submit">Show my next step</button>
                            <p class="meta-line console-note">This guide only routes you to the next page. It does not change your application status. Route inputs may be logged to improve the guide. Do not submit official credentials or documents here. <a class="meta-link" href="/privacy/">Privacy</a></p>
                        </form>
                    </div>
                    <div class="console-output-column">
                        <article class="console-verdict">
                            <div class="console-verdict__header">
                                <p class="eyebrow">Recommended next step</p>
                                <span class="console-stamp">Current route</span>
                            </div>
                            <h2>${verdictTitle}</h2>
                            <p class="console-summary">${verdictSummary}</p>
                            <div class="console-verdict__signals">
                                <div class="console-verdict__signal">
                                    <span>Output mode</span>
                                    <strong>${decisionMode}</strong>
                                </div>
                                <div class="console-verdict__signal">
                                    <span>Source stack</span>
                                    <strong>${sourceCoverage}</strong>
                                </div>
                                <div class="console-verdict__signal">
                                    <span>Status</span>
                                    <strong>${routeState}</strong>
                                </div>
                            </div>
                            <div class="console-metrics">
                                <div class="console-metric">
                                    <span class="console-metric__label">First contractor type</span>
                                    <strong>${contractorType}</strong>
                                </div>
                                <div class="console-metric">
                                    <span class="console-metric__label">Priority route</span>
                                    <strong>${priorityRoute}</strong>
                                </div>
                            </div>
                            <div class="action-row console-actions">
                                @if(page.decisionResult() != null)
                                    <a class="primary-button" href="${page.decisionResult().primaryAction().href()}">${page.decisionResult().primaryAction().label()}</a>
                                    <a class="secondary-button" href="${page.decisionResult().secondaryAction().href()}">${page.decisionResult().secondaryAction().label()}</a>
                                @else
                                    <a class="primary-button" href="/program/inspection-report/">Read the report first</a>
                                    <a class="secondary-button" href="/program/choose-project/">Choose the first project</a>
                                @endif
                            </div>
                        </article>
                        <div class="console-aux-grid">
                            <article class="signal-card">
                                <p class="micro-tag">Mistake to avoid</p>
                                <h3>${mistakeTitle}</h3>
                                <ul class="signal-list">
                                @if(page.decisionResult() != null)
                                    @for(var step : page.decisionResult().nextSteps())
                                        <li>${step}</li>
                                    @endfor
                                @else
                                    <li>Report with recommendation: narrow the first project.</li>
                                    <li>Attached home: branch scope early.</li>
                                    <li>Quote urgency: do not outrun the recommendation.</li>
                                @endif
                                </ul>
                            </article>
                            <article class="signal-card signal-card--official">
                                <p class="micro-tag">Source verification</p>
                                <h3>Current program source stack</h3>
                                <p class="signal-card__intro">${officialSummary}</p>
                                <div class="source-check-list">
                                @for(var index = 0; index < sourcePreviewCount; index++)
                                    !{var source = page.sourceStack().get(index);}
                                    <a class="source-check" href="${source.url()}" target="_blank" rel="noopener noreferrer">
                                        <span class="source-check__copy">
                                            <strong>${source.title()}</strong>
                                            <small>${source.note()}</small>
                                        </span>
                                        <em>Verified</em>
                                    </a>
                                @endfor
                                </div>
                                <a class="meta-link" href="/methodology/">Review methodology</a>
                            </article>
                        </div>
                        <section class="verdict-branch-section">
                            <div class="section-heading section-heading--compact">
                                <p class="eyebrow">Route branches</p>
                                <h2>${branchTitle}</h2>
                                <p>${branchIntro}</p>
                            </div>
                            <div class="route-detail-grid route-detail-grid--verdict">
                            @for(var card : page.verdictRouteCards())
                                <a class="route-detail-card route-detail-card--verdict" href="${card.href()}">
                                    <p class="micro-tag">${card.eyebrow()}</p>
                                    <h3>${card.title()}</h3>
                                    <p>${card.summary()}</p>
                                    <span class="route-detail-card__footer">${card.footer()}</span>
                                </a>
                            @endfor
                            </div>
                        </section>
                    </div>
                </div>
            </section>

            <section class="section home-console-lower">
                <div class="section-shell home-console-lower-grid">
                    <aside class="warning-panel">
                        <p class="warning-panel__eyebrow">Scope exclusions</p>
                        <h2 class="warning-panel__title">What this site does not do</h2>
                        <ul class="bullet-list danger-list">
                        @for(var item : page.programSnapshot().contractorResponsibilityNotes())
                            <li>${item}</li>
                        @endfor
                        </ul>
                    </aside>
                    <div class="image-evidence image-evidence--wide">
                        <img src="https://images.unsplash.com/photo-1763665814605-a6489a3bf2a0?auto=format&amp;fit=crop&amp;w=1600&amp;q=80" alt="Roofers installing shingles on a residential roof">
                        <div class="image-evidence__overlay">
                            <span>Inspection route</span>
                            <span>Scope verification</span>
                        </div>
                    </div>
                </div>
            </section>

            <section class="section home-proof">
                <div class="section-shell two-column">
                    <div class="info-panel emphasis">
                        <p class="eyebrow">Quick answer</p>
                        <p class="big-answer">This site is a post-inspection decision engine. Use it to understand the recommendation, choose the first project, and enter quote prep with the right contractor type in mind.</p>
                    </div>
                    <div class="info-panel">
                        <p class="eyebrow">Attached-home scope</p>
                        <h2>Branch attached homes early</h2>
                        <ul class="bullet-list">
                        @for(var item : page.programSnapshot().attachedHomeScopeNotes())
                            <li>${item}</li>
                        @endfor
                        </ul>
                    </div>
                </div>
            </section>

            @template.shared.decisionPath(
                title = "Interpret first, choose second, quote third",
                intro = "The product is strongest when it stays inside one narrow flow: report meaning, project priority, then contractor and quote preparation."
            )

            <section class="section home-alerts">
                <div class="section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">What not to assume</p>
                        <h2>Keep the wedge narrow</h2>
                    </div>
                    <div class="home-alert-grid">
                        <article class="home-alert-card">
                            <p class="micro-tag">Boundary</p>
                            <h3>Not a general eligibility site</h3>
                            <p>Stay inside report interpretation, project choice, and quote preparation.</p>
                        </article>
                        <article class="home-alert-card">
                            <p class="micro-tag">Scope</p>
                            <h3>Do not turn every roof issue into reroof logic</h3>
                            <p>Recommendation clarity comes before any broad roof package conversation.</p>
                        </article>
                        <article class="home-alert-card">
                            <p class="micro-tag">Timing</p>
                            <h3>Do not let quote shopping outrun the report</h3>
                            <p>Contractor outreach should follow the recommendation, not replace it.</p>
                        </article>
                    </div>
                </div>
            </section>

            <section class="section home-route-family home-route-family--primary">
                <div class="section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">Start here</p>
                        <h2>Most important decision routes after the report</h2>
                        <p>These are the first routes that should absorb the homeowner's attention before broader shopping starts.</p>
                    </div>
                    <div class="card-grid route-grid route-grid--home route-grid--primary">
                    @for(var card : page.scenarioCards())
                        <a class="route-card" href="${card.href()}">
                            <p class="card-tag">${card.family()}</p>
                            <h3>${card.title()}</h3>
                            <p>${card.summary()}</p>
                        </a>
                    @endfor
                    </div>
                </div>
            </section>

            <section class="section image-band console-band home-band">
                <div class="section-shell image-band-shell">
                    <div>
                        <p class="eyebrow">Scope discipline</p>
                        <h2>Branch attached homes early</h2>
                        <p>If the home is attached or treated like a townhouse, current support-center rules can narrow grant-backed scope before roof-heavy assumptions get expensive.</p>
                        <ul class="bullet-list">
                        @for(var item : page.programSnapshot().attachedHomeScopeNotes())
                            <li>${item}</li>
                        @endfor
                        </ul>
                    </div>
                    <div class="band-visual">
                        <img src="https://images.unsplash.com/photo-1763665814605-a6489a3bf2a0?auto=format&amp;fit=crop&amp;w=1600&amp;q=80" alt="Roofers installing shingles on a residential roof">
                        <div class="band-visual__tags">
                            <span>Roof scope</span>
                            <span>On-site review</span>
                        </div>
                    </div>
                </div>
            </section>

            <section class="section home-route-family home-route-family--secondary">
                <div class="section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">Improvement routes</p>
                        <h2>Use improvement routes only after the recommendation is clear</h2>
                        <p>The improvement library is useful only once the homeowner already knows which recommendation they are dealing with.</p>
                    </div>
                    <div class="card-grid route-grid route-grid--home">
                    @for(var card : page.improvementCards())
                        <a class="route-card" href="${card.href()}">
                            <p class="card-tag">${card.family()}</p>
                            <h3>${card.title()}</h3>
                            <p>${card.summary()}</p>
                        </a>
                    @endfor
                    </div>
                </div>
            </section>

            <section class="section home-route-family home-route-family--guides">
                <div class="section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">Guides</p>
                        <h2>Decision guides that push you back into the core route system</h2>
                        <p>Guides exist to clarify a stuck moment, then push the homeowner back into the narrower route.</p>
                    </div>
                    <div class="card-grid route-grid route-grid--home">
                    @for(var card : page.guideCards())
                        <a class="route-card" href="${card.href()}">
                            <p class="card-tag">${card.family()}</p>
                            <h3>${card.title()}</h3>
                            <p>${card.summary()}</p>
                        </a>
                    @endfor
                    </div>
                </div>
            </section>
            @template.shared.sourceStack(sources = page.sourceStack(), reviewedOn = page.meta().reviewedOn())
        </main>
    `
)
