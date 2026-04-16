@import owner.mysafefloridahome.pages.ImprovementPageView
@param ImprovementPageView page
!{var verifiedOn = page.page().verifiedOn().toString();}
!{var nextReviewOn = page.page().nextReviewOn().toString();}
!{var decisionStage = "choose";}
!{var slug = page.page().slug();}
!{var routePath = page.route().path();}
!{var canonicalUrl = page.meta().canonicalUrl();}
!{var baseUrl = canonicalUrl.substring(0, canonicalUrl.length() - routePath.length());}
!{var panelTitle = "opening-protection".equals(slug) ? "Identify the openings before you price them"
        : "roof-deck-attachment".equals(slug) ? "Fastening work is not a full reroof by default"
        : "roof-replacement-under-swr".equals(slug) ? "Replacement only works when SWR requires it"
        : "roof-to-wall".equals(slug) ? "Connection retrofit comes before broad roof scope"
        : "secondary-water-resistance".equals(slug) ? "SWR is a mitigation path, not generic reroof permission"
        : "Keep the quote disciplined";}
@template.layout.page(
    meta = page.meta(),
    siteName = "My Safe Florida Home Verdict",
    currentPath = page.route().path(),
    content = @`
        <main>
            @template.shared.pageSchema(
                meta = page.meta(),
                siteName = "My Safe Florida Home Verdict",
                about = "Improvement-specific decision support for a My Safe Florida Home recommendation"
            )
            <script type="application/ld+json">
            {
              "@context": "https://schema.org",
              "@type": "BreadcrumbList",
              "itemListElement": [
              @for(int i = 0; i < page.meta().breadcrumbs().size(); ++i)
                !{var crumb = page.meta().breadcrumbs().get(i);}
                {
                  "@type": "ListItem",
                  "position": ${i + 1},
                  "name": "${crumb.label()}"@if(crumb.href() != null),
                  "item": "${baseUrl}${crumb.href()}"@endif
                }@if(i < page.meta().breadcrumbs().size() - 1),@endif
              @endfor
              ]
            }
            </script>
            <section class="page-hero route-hero">
                <div class="section-shell route-hero-shell">
                    <div class="route-hero-main">
                        @template.shared.breadcrumbs(page.meta())
                        <p class="eyebrow">Improvement decision route</p>
                        <h1>${page.page().title()}</h1>
                        <p class="lead">${page.page().description()}</p>
                        <div class="action-row">
                            <a class="primary-button" href="${page.page().primaryCta().href()}" data-track-event="lead_form_open" data-route-path="${page.route().path()}" data-route-family="${page.route().family()}" data-scenario="${page.leadForm().scenario()}" data-improvement-type="${page.leadForm().improvementType()}">${page.page().primaryCta().label()}</a>
                            <a class="secondary-button" href="${page.page().secondaryCta().href()}" data-track-event="route_cta_click" data-route-path="${page.route().path()}" data-route-family="${page.route().family()}" data-scenario="${page.leadForm().scenario()}" data-improvement-type="${page.leadForm().improvementType()}">${page.page().secondaryCta().label()}</a>
                        </div>
                    </div>
                    <aside class="route-hero-panel">
                        <p class="micro-tag">Improvement summary</p>
                        <h2>${panelTitle}</h2>
                        <p class="route-hero-panel__summary">${page.page().recommendationMeaning()}</p>
                        <div class="route-hero-facts">
                            <div class="route-hero-fact">
                                <span>Contractor type</span>
                                <strong>${page.page().contractorType()}</strong>
                            </div>
                            <div class="route-hero-fact">
                                <span>Review window</span>
                                <strong>${verifiedOn}</strong>
                            </div>
                            <div class="route-hero-fact route-hero-fact--wide">
                                <span>Attached-home caveat</span>
                                <p>${page.page().attachedHomeCaveat()}</p>
                            </div>
                        </div>
                    </aside>
                </div>
            </section>

            @template.shared.decisionPath(
                currentStage = decisionStage,
                intro = "Improvement routes exist to narrow the first project and contractor path, not to let the quote outrun the recommendation."
            )

            <section class="section">
                <div class="section-shell two-column">
                    <div class="info-panel">
                        <p class="eyebrow">Quick answer</p>
                        <p class="big-answer">${page.page().recommendationMeaning()}</p>
                    </div>
                    <div class="info-panel emphasis">
                        <h3>Contractor type to vet first</h3>
                        <p>${page.page().contractorType()}</p>
                        <p class="meta-line">Verified ${verifiedOn} - Next scheduled review ${nextReviewOn}</p>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell two-column">
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">When this is the right first project</p>
                            <h2>Use the report as the filter</h2>
                        </div>
                        <ul class="bullet-list">
                        @for(var item : page.page().rightFirstProjectSignals())
                            <li>${item}</li>
                        @endfor
                        </ul>
                    </div>
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">What scope usually qualifies</p>
                            <h2>Keep the quote disciplined</h2>
                        </div>
                        <ul class="bullet-list">
                        @for(var item : page.page().qualifyingScope())
                            <li>${item}</li>
                        @endfor
                        </ul>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell two-column">
                    <div class="info-panel emphasis">
                        <h3>Attached-home caveat</h3>
                        <p>${page.page().attachedHomeCaveat()}</p>
                    </div>
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">What not to assume</p>
                            <h2>Do not widen the answer too early</h2>
                        </div>
                        <ul class="bullet-list danger-list">
                        @for(var item : page.page().confusionOrDenialRisks())
                            <li>${item}</li>
                        @endfor
                        </ul>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">Related routes</p>
                        <h2>Compare the next likely routes</h2>
                    </div>
                    <div class="route-detail-grid route-detail-grid--related">
                    @for(var card : page.relatedRouteCards())
                        <a class="route-detail-card" href="${card.href()}">
                            <p class="micro-tag">${card.eyebrow()}</p>
                            <h3>${card.title()}</h3>
                            <p>${card.summary()}</p>
                            <span class="route-detail-card__footer">${card.footer()}</span>
                        </a>
                    @endfor
                    </div>
                </div>
            </section>

            @template.shared.sourceStack(sources = page.sourceStack(), reviewedOn = page.meta().reviewedOn())
            @template.shared.leadForm(leadForm = page.leadForm(), leadStatus = page.leadStatus())
        </main>
    `
)
