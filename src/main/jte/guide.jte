@import owner.mysafefloridahome.pages.GuidePageView
@param GuidePageView page
!{var decisionStage = page.route().path().contains("contractor-quote-checklist") ? "quote" : page.route().path().contains("inspection-report-what-next") ? "interpret" : "choose";}
!{var stageLabel = "quote".equals(decisionStage) ? "Quote preparation" : "choose".equals(decisionStage) ? "Project choice" : "Report interpretation";}
!{var slug = page.page().slug();}
!{var routePath = page.route().path();}
!{var canonicalUrl = page.meta().canonicalUrl();}
!{var baseUrl = canonicalUrl.substring(0, canonicalUrl.length() - routePath.length());}
!{var panelTitle = "msfh-inspection-report-what-next".equals(slug) ? "Use the report to choose the first project"
        : "impact-windows-vs-shutters".equals(slug) ? "Do not price every opening the same way"
        : "msfh-contractor-quote-checklist".equals(slug) ? "A safe quote explains eligible scope"
        : "roof-replacement-through-msfh".equals(slug) ? "Roof replacement needs an eligibility link"
        : "Use the guide, then return to the route";}
!{var decisionUse = "msfh-inspection-report-what-next".equals(slug)
        ? "Use this when the report just landed and the homeowner still needs to narrow the first project before quote shopping."
        : "impact-windows-vs-shutters".equals(slug)
                ? "Use this when opening protection is recommended but the safest window-versus-shutter path is still unclear."
                : "msfh-contractor-quote-checklist".equals(slug)
                        ? "Use this once the recommendation is clear and the homeowner needs to pressure-test a real quote."
                        : "roof-replacement-through-msfh".equals(slug)
                                ? "Use this when roof-heavy messaging appears before the eligibility link is actually proven."
                                : "Use this guide to reduce ambiguity, then return to the route that makes the next decision.";}
@template.layout.page(
    meta = page.meta(),
    siteName = "My Safe Florida Home Verdict",
    currentPath = page.route().path(),
    content = @`
        <main>
            @template.shared.pageSchema(
                meta = page.meta(),
                siteName = "My Safe Florida Home Verdict",
                about = "Decision checklist guidance that supports a core My Safe Florida Home route"
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
                        <p class="eyebrow">Decision guide</p>
                        <h1>${page.page().title()}</h1>
                        <p class="lead">${page.page().description()}</p>
                        <div class="action-row">
                            <a class="primary-button" href="${page.page().primaryCta().href()}" data-track-event="lead_form_open" data-route-path="${page.route().path()}" data-route-family="${page.route().family()}" data-scenario="${page.leadForm().scenario()}" data-improvement-type="${page.leadForm().improvementType()}">${page.page().primaryCta().label()}</a>
                            <a class="secondary-button" href="${page.page().secondaryCta().href()}" data-track-event="route_cta_click" data-route-path="${page.route().path()}" data-route-family="${page.route().family()}" data-scenario="${page.leadForm().scenario()}" data-improvement-type="${page.leadForm().improvementType()}">${page.page().secondaryCta().label()}</a>
                        </div>
                    </div>
                    <aside class="route-hero-panel">
                        <p class="micro-tag">Guide summary</p>
                        <h2>${panelTitle}</h2>
                        <p class="route-hero-panel__summary">${page.page().quickAnswer()}</p>
                        <div class="route-hero-facts">
                            <div class="route-hero-fact">
                                <span>Decision stage</span>
                                <strong>${stageLabel}</strong>
                            </div>
                            <div class="route-hero-fact">
                                <span>Primary move</span>
                                <strong>${page.page().primaryCta().label()}</strong>
                            </div>
                            <div class="route-hero-fact route-hero-fact--wide">
                                <span>Return path</span>
                                <p>${page.page().secondaryCta().label()}</p>
                            </div>
                        </div>
                    </aside>
                </div>
            </section>

            @template.shared.decisionPath(
                currentStage = decisionStage,
                intro = "Guides should always send the homeowner back into the core decision routes instead of becoming standalone FAQ pages."
            )

            <section class="section">
                <div class="section-shell two-column">
                    <div class="info-panel">
                        <p class="eyebrow">Quick answer</p>
                        <p class="big-answer">${page.page().quickAnswer()}</p>
                    </div>
                    <div class="info-panel">
                        <p class="eyebrow">Decision use</p>
                        <p>${decisionUse}</p>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell two-column">
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">Key takeaways</p>
                            <h2>What matters most before the quote step</h2>
                        </div>
                        <ul class="bullet-list">
                        @for(var item : page.page().keyTakeaways())
                            <li>${item}</li>
                        @endfor
                        </ul>
                    </div>
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">What not to assume</p>
                            <h2>Skip the expensive shortcuts</h2>
                        </div>
                        <ul class="bullet-list danger-list">
                        @for(var item : page.page().whatNotToAssume())
                            <li>${item}</li>
                        @endfor
                        </ul>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell two-column">
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">Next steps</p>
                            <h2>Move back into the core routes</h2>
                        </div>
                        <ul class="bullet-list">
                        @for(var item : page.page().nextSteps())
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
