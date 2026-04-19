@import owner.mysafefloridahome.pages.ProgramPageView
@param ProgramPageView page
!{var primaryEvent = page.page().primaryCta().href().startsWith("#") ? "lead_form_open" : "route_cta_click";}
!{var slug = page.page().slug();}
!{var isSupportRoute = "no-recommended-improvements".equals(slug) || "group-5".equals(slug) || "rfi".equals(slug) || "portal-statuses".equals(slug);}
!{var decisionStage = "final-inspection-and-draw-request".equals(slug) ? "closeout"
        : "contractor-quotes".equals(slug) ? "quote"
        : "choose-project".equals(slug) || "what-msfh-will-pay-for".equals(slug) ? "choose"
        : "interpret";}
!{var stageLabel = "quote".equals(decisionStage) ? "Quote preparation"
        : "choose".equals(decisionStage) ? "Project choice"
        : "closeout".equals(decisionStage) ? "Closeout"
        : "Report interpretation";}
!{var routePath = page.route().path();}
!{var canonicalUrl = page.meta().canonicalUrl();}
!{var baseUrl = canonicalUrl.substring(0, canonicalUrl.length() - routePath.length());}
!{var heroEyebrow = "closeout".equals(decisionStage) ? "Closeout route"
        : "quote".equals(decisionStage) ? "Quote route"
        : "choose".equals(decisionStage) ? "Project-choice route"
        : isSupportRoute ? "Support route"
        : "Program route";}
!{var panelTitle = "inspection-report".equals(slug) ? "Read the report before you price anything"
        : "choose-project".equals(slug) ? "Choose one project before scope drifts"
        : "contractor-quotes".equals(slug) ? "Check license and scope before you sign"
        : "what-msfh-will-pay-for".equals(slug) ? "Budget only around eligible work"
        : "no-recommended-improvements".equals(slug) ? "No recommendation means stop and verify"
        : "group-5".equals(slug) ? "Status confusion is not a project plan"
        : "rfi".equals(slug) ? "Resolve the missing item before moving"
        : "portal-statuses".equals(slug) ? "A portal label changes timing, not eligibility"
        : "final-inspection-and-draw-request".equals(slug) ? "Close the file before expecting the draw"
        : "quote".equals(decisionStage) ? "Quote only after scope is locked"
        : "choose".equals(decisionStage) ? "Choose the first project before price shopping"
        : "Confirm the recommendation before any quote";}
!{var decisionPathTitle = "closeout".equals(decisionStage) ? "Finish the work, then close the file cleanly"
        : isSupportRoute ? "Clear the blocker before you price or sign anything"
        : "Interpret first, then choose, then quote";}
!{var decisionPathIntro = "closeout".equals(decisionStage)
        ? "This route exists for the last step: permits, inspections, and file readiness before the draw request."
        : isSupportRoute
                ? "This route helps you figure out whether the blocker is about timing, missing information, or the report itself before money moves."
                : "Use this route to keep the decision moving in order instead of jumping straight into generic quote shopping.";}
!{var rulesTitle = "closeout".equals(decisionStage) ? "What has to be true before the draw request"
        : isSupportRoute ? "What this blocker actually changes"
        : "Keep the next move tied to the report";}
!{var riskTitle = "closeout".equals(decisionStage) ? "Do not treat finished work as a finished file"
        : isSupportRoute ? "Do not let the blocker push you into the wrong next step"
        : "Avoid the expensive shortcuts";}
!{var showQuotePrepBridge = "contractor-quotes".equals(slug);}
@template.layout.page(
    meta = page.meta(),
    siteName = "My Safe Florida Home Verdict",
    currentPath = page.route().path(),
    content = @`
        <main>
            @template.shared.pageSchema(
                meta = page.meta(),
                siteName = "My Safe Florida Home Verdict",
                about = "Program decision support for Florida homeowners after a My Safe Florida Home inspection report"
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
                        <p class="eyebrow">${heroEyebrow}</p>
                        <h1>${page.page().title()}</h1>
                        <p class="lead">${page.page().description()}</p>
                        <div class="action-row">
                            <a class="primary-button" href="${page.page().primaryCta().href()}" data-track-event="${primaryEvent}" data-route-path="${page.route().path()}" data-route-family="${page.route().family()}" data-scenario="${page.leadForm().scenario()}" data-improvement-type="${page.leadForm().improvementType()}">${page.page().primaryCta().label()}</a>
                            @if(page.page().secondaryCta() != null)
                                <a class="secondary-button" href="${page.page().secondaryCta().href()}" data-track-event="route_cta_click" data-route-path="${page.route().path()}" data-route-family="${page.route().family()}" data-scenario="${page.leadForm().scenario()}" data-improvement-type="${page.leadForm().improvementType()}">${page.page().secondaryCta().label()}</a>
                            @endif
                        </div>
                    </div>
                    <aside class="route-hero-panel">
                        <p class="micro-tag">Route summary</p>
                        <h2>${panelTitle}</h2>
                        <p class="route-hero-panel__summary">${page.page().quickAnswer()}</p>
                        <div class="route-hero-facts">
                            <div class="route-hero-fact">
                                <span>Best used at</span>
                                <strong>${stageLabel}</strong>
                            </div>
                            <div class="route-hero-fact">
                                <span>Do this now</span>
                                <strong>${page.page().primaryCta().label()}</strong>
                            </div>
                            <div class="route-hero-fact route-hero-fact--wide">
                                <span>Keep in mind</span>
                                <p>${page.page().attachedHomeCaveat()}</p>
                            </div>
                        </div>
                    </aside>
                </div>
            </section>

            @template.shared.decisionPath(
                currentStage = decisionStage,
                title = decisionPathTitle,
                intro = decisionPathIntro,
                showCloseout = "closeout".equals(decisionStage)
            )

            <section class="section">
                <div class="section-shell two-column">
                    <div class="info-panel">
                        <p class="eyebrow">Quick answer</p>
                        <p class="big-answer">${page.page().quickAnswer()}</p>
                    </div>
                    <div class="info-panel">
                        <p class="eyebrow">Why this page matters</p>
                        <p>${page.page().workflowMeaning()}</p>
                    </div>
                </div>
            </section>

            @if(showQuotePrepBridge)
                <section class="section quote-prep-bridge quote-prep-bridge--route">
                    <div class="section-shell quote-prep-bridge__layout">
                        <div class="quote-prep-bridge__copy">
                            <p class="eyebrow">Opening-protection quote prep</p>
                            <h2>When the quote is really about openings, lock the first request before you compare price</h2>
                            <p>This route helps you vet any estimate. When the recommendation points to windows, doors, shutters, or garage openings, use the quote-prep brief so every contractor reacts to the same first scope instead of a broad package.</p>
                            <div class="action-row">
                                <a class="primary-button" href="/tools/opening-protection/quote-prep-brief/">Open the Quote-Prep Brief</a>
                                <a class="secondary-button" href="/improvements/opening-protection/">Read the Opening-Protection Route</a>
                            </div>
                        </div>
                        <div class="quote-prep-bridge__list-wrap">
                            <ul class="quote-prep-bridge__list">
                                <li>Use it when the report already points to opening protection and the first send still needs cleaner boundaries.</li>
                                <li>Keep attached-home limits visible before a whole-house proposal turns into the default.</li>
                                <li>Compare prices only after each contractor is looking at the same openings and the same narrow first ask.</li>
                            </ul>
                        </div>
                    </div>
                </section>
            @endif

            @if(page.page().actionChecklist() != null)
                @template.shared.actionChecklist(checklist = page.page().actionChecklist())
            @endif

            @if(page.page().decisionTable() != null)
                @template.shared.decisionTable(table = page.page().decisionTable())
            @endif

            <section class="section">
                <div class="section-shell two-column">
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">What the current rules suggest</p>
                            <h2>${rulesTitle}</h2>
                        </div>
                        <ul class="bullet-list">
                        @for(var item : page.page().coverageNotes())
                            <li>${item}</li>
                        @endfor
                        </ul>
                    </div>
                    <div class="info-panel emphasis">
                        <h3>Attached-home caveat</h3>
                        <p>${page.page().attachedHomeCaveat()}</p>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell two-column">
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">What not to assume</p>
                            <h2>${riskTitle}</h2>
                        </div>
                        <ul class="bullet-list danger-list">
                        @for(var item : page.page().whatNotToAssume())
                            <li>${item}</li>
                        @endfor
                        </ul>
                    </div>
                    <div>
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
                </div>
            </section>

            @template.shared.sourceStack(sources = page.sourceStack(), reviewedOn = page.meta().reviewedOn())
            @template.shared.leadForm(leadForm = page.leadForm(), leadStatus = page.leadStatus())
        </main>
    `
)
