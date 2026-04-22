@import owner.mysafefloridahome.pages.QuotePrepBriefEntryPageView
@param QuotePrepBriefEntryPageView page
@template.layout.page(
    meta = page.meta(),
    siteName = "My Safe Florida Home Verdict",
    currentPath = "/tools/opening-protection/quote-prep-brief/",
    bodyClass = "quote-prep-page quote-prep-page--entry",
    content = @`
        <main>
            <section class="page-hero route-hero quote-prep-hero">
                <div class="section-shell route-hero-shell">
                    <div class="route-hero-main">
                        @template.shared.breadcrumbs(page.meta())
                        <p class="eyebrow">${page.eyebrow()}</p>
                        <h1>${page.title()}</h1>
                        <p class="lead">${page.description()}</p>
                        <div class="action-row">
                            <a class="primary-button" href="${page.artifactCards().get(0).href()}">Build the Quote-Prep Brief</a>
                            <a class="secondary-button" href="/improvements/opening-protection/">Read the Opening-Protection Route</a>
                        </div>
                    </div>
                    <aside class="route-hero-panel">
                        <p class="micro-tag">Homeowner tool</p>
                        <h2>Turn one report recommendation into one clean first contractor request</h2>
                        <p class="route-hero-panel__summary">${page.quickAnswer()}</p>
                        <div class="route-hero-facts">
                            <div class="route-hero-fact">
                                <span>Do now</span>
                                <strong>Build one shareable first request</strong>
                            </div>
                            <div class="route-hero-fact">
                                <span>Have ready</span>
                                <strong>The report wording, named openings, and your current quote focus</strong>
                            </div>
                            <div class="route-hero-fact route-hero-fact--wide">
                                <span>Do not do yet</span>
                                <p>Do not let the first request drift into a whole-house package before the report-backed opening scope is readable on its own.</p>
                            </div>
                        </div>
                    </aside>
                </div>
            </section>

            <section class="section">
                <div class="section-shell two-column">
                    <div class="info-panel emphasis">
                        <p class="eyebrow">Quick answer</p>
                        <p class="big-answer">${page.quickAnswer()}</p>
                    </div>
                    <div class="info-panel">
                        <p class="eyebrow">What this is</p>
                        <p>${page.laneSummary()}</p>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">Before you ask for price</p>
                        <h2>Five checks that keep the first request clear</h2>
                        <p>Do not share a fuzzy request. These five checks keep the first send tied to the report, the current quote focus, the home type, and the openings that actually belong in the first quote.</p>
                    </div>
                    <div class="quote-prep-anchor-board">
                        @for (var index = 0; index < page.quoteReadinessCriteria().size(); index++)
                            !{var item = page.quoteReadinessCriteria().get(index);}
                            <article class="quote-prep-anchor">
                                <span class="quote-prep-anchor__number">${"0" + (index + 1)}</span>
                                <p class="micro-tag">Ready check</p>
                                <p class="big-answer">${item}</p>
                            </article>
                        @endfor
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell quote-prep-entry-grid">
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">What you send</p>
                            <h2>Start with one shareable brief, not a bigger process</h2>
                            <p>The win here is not more process. The win is that the first contractor reply comes back narrower and easier to compare.</p>
                        </div>
                        <a class="route-detail-card quote-prep-entry-card" href="${page.artifactCards().get(0).href()}">
                            <p class="micro-tag">${page.artifactCards().get(0).eyebrow()}</p>
                            <h3>${page.artifactCards().get(0).title()}</h3>
                            <p>${page.artifactCards().get(0).summary()}</p>
                            <span class="route-detail-card__footer">${page.artifactCards().get(0).footer()}</span>
                        </a>
                    </div>
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">What you want back</p>
                            <h2>Judge this by the first reply</h2>
                            <p>The quality bar is simple: the homeowner sends one readable brief, the contractor replies inside the same narrow scope, and only then does anything downstream matter.</p>
                        </div>
                        <div class="quote-prep-step-list">
                            @for (var step : page.workflowSteps())
                                <article class="quote-prep-step">
                                    <p class="micro-tag">Reply step</p>
                                    <p>${step}</p>
                                </article>
                            @endfor
                        </div>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell two-column">
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">When this helps</p>
                            <h2>Use it when the first contractor conversation keeps getting wider too early</h2>
                        </div>
                        <div class="packet-list-card">
                            <ul class="bullet-list">
                                @for (var item : page.fitPoints())
                                    <li>${item}</li>
                                @endfor
                            </ul>
                        </div>
                    </div>
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">Keep this outside the first pass</p>
                            <h2>Do not let the opening-protection ask turn into a bigger package</h2>
                        </div>
                        <div class="packet-list-card packet-list-card--warning">
                            <ul class="bullet-list danger-list">
                                @for (var item : page.guardrails())
                                    <li>${item}</li>
                                @endfor
                            </ul>
                        </div>
                    </div>
                </div>
            </section>

            <section class="section quote-prep-bridge quote-prep-bridge--entry">
                <div class="section-shell quote-prep-bridge__layout">
                    <div class="section-heading">
                        <p class="eyebrow">Next move</p>
                        <h2>Ready to build the first request?</h2>
                        <p>Use the builder once the report recommendation, the openings, and the first quote focus are all narrow enough to share without extra explanation.</p>
                        <div class="action-row">
                            <a class="primary-button" href="${page.artifactCards().get(0).href()}">Open the Builder</a>
                            <a class="secondary-button" href="/guides/opening-protection-quote-checklist/">Review the Quote Checklist</a>
                        </div>
                    </div>
                    <div class="quote-prep-bridge__list-wrap">
                        <ul class="quote-prep-bridge__list">
                            <li>Start from one report recommendation line, not a bigger quote bundle or a whole-house package.</li>
                            <li>Name only the openings that belong in the first quote, so every contractor is reacting to the same scope.</li>
                            <li>Use the reply to narrow the next conversation before anyone spends time on measurement or broader pricing.</li>
                        </ul>
                    </div>
                </div>
            </section>
        </main>
    `)
