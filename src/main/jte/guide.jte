@import owner.mysafefloridahome.pages.GuidePageView
@param GuidePageView page
!{var slug = page.page().slug();}
!{var isQuoteGuide = "msfh-contractor-quote-checklist".equals(slug)
        || "opening-protection-quote-checklist".equals(slug)
        || "roof-to-wall-quote-checklist".equals(slug)
        || "swr-roof-quote-checklist".equals(slug);}
!{var isSupportGuide = "no-recommended-improvements-what-next".equals(slug)
        || "msfh-rfi-response-checklist".equals(slug)
        || "msfh-group-5-what-next".equals(slug)
        || "msfh-portal-statuses-explained".equals(slug);}
!{var isCloseoutGuide = "final-inspection-draw-request-checklist".equals(slug);}
!{var decisionStage = isCloseoutGuide ? "closeout"
        : isQuoteGuide ? "quote"
        : (isSupportGuide || "msfh-inspection-report-what-next".equals(slug)) ? "interpret"
        : "choose";}
!{var stageLabel = "quote".equals(decisionStage) ? "Quote preparation"
        : "choose".equals(decisionStage) ? "Project choice"
        : "closeout".equals(decisionStage) ? "Closeout"
        : "Report interpretation";}
!{var routePath = page.route().path();}
!{var canonicalUrl = page.meta().canonicalUrl();}
!{var baseUrl = canonicalUrl.substring(0, canonicalUrl.length() - routePath.length());}
!{var guideEyebrow = isQuoteGuide ? "Quote checklist"
        : isCloseoutGuide ? "Closeout guide"
        : isSupportGuide ? "Support guide"
        : "Decision guide";}
!{var panelTitle = "msfh-inspection-report-what-next".equals(slug) ? "Read the report before you call anyone"
        : "impact-windows-vs-shutters".equals(slug) ? "Do not price every opening the same way"
        : "msfh-contractor-quote-checklist".equals(slug) ? "A safe quote explains eligible scope"
        : "roof-replacement-through-msfh".equals(slug) ? "A full reroof needs a real eligibility link"
        : "opening-protection-quote-checklist".equals(slug) ? "Break the quote out opening by opening"
        : "roof-to-wall-quote-checklist".equals(slug) ? "Keep retrofit work from turning into a reroof"
        : "swr-roof-quote-checklist".equals(slug) ? "Ask what part of the roof work is actually required"
        : "attached-home-scope-under-msfh".equals(slug) ? "Attached homes need a scope check earlier"
        : "no-recommended-improvements-what-next".equals(slug) ? "No recommendation is usually a stop sign"
        : "msfh-rfi-response-checklist".equals(slug) ? "Clear the file before you move the project"
        : "msfh-group-5-what-next".equals(slug) ? "Waiting status is not permission to start work"
        : "msfh-portal-statuses-explained".equals(slug) ? "A portal label only matters if it changes your next move"
        : "final-inspection-draw-request-checklist".equals(slug) ? "Finished work still needs a clean closeout"
        : "Use the guide, then return to the route";}
!{var decisionUse = "msfh-inspection-report-what-next".equals(slug)
        ? "Use this when the report just landed and you still need to figure out what to read first, what to ignore, and what project should actually go first."
        : "impact-windows-vs-shutters".equals(slug)
                ? "Use this when opening protection is recommended but every salesperson is pushing a different answer."
                : "msfh-contractor-quote-checklist".equals(slug)
                        ? "Use this once the recommendation is clear and a real quote is in front of you."
                        : "roof-replacement-through-msfh".equals(slug)
                                ? "Use this when a contractor is turning the recommendation into a much bigger roof conversation than the report seems to support."
                                : "opening-protection-quote-checklist".equals(slug)
                                        ? "Use this when a window, shutter, or door quote is getting broad fast and you need to pull it back to the report."
                                        : "roof-to-wall-quote-checklist".equals(slug)
                                                ? "Use this when a roof-to-wall recommendation starts sounding like a generic roofing sale."
                                                : "swr-roof-quote-checklist".equals(slug)
                                                        ? "Use this when SWR is being used to justify a much bigger roof job than you expected."
                                                        : "attached-home-scope-under-msfh".equals(slug)
                                                                ? "Use this when your home is attached and you need to know whether detached-home advice is leading you in the wrong direction."
                                                                : "no-recommended-improvements-what-next".equals(slug)
                                                                        ? "Use this when the report does not open a project path and you need to know whether you should stop, appeal, or keep shopping."
                                                                        : "msfh-rfi-response-checklist".equals(slug)
                                                                                ? "Use this when the file is blocked by missing information and you need to know what has to be fixed before anything else."
                                                                                : "msfh-group-5-what-next".equals(slug)
                                                                                        ? "Use this when Group 5 is all you see and you are trying to decide whether to wait, follow up, or change plans."
                                                                                        : "msfh-portal-statuses-explained".equals(slug)
                                                                                                ? "Use this when the portal label is the only thing you have and you need to know whether it actually changes the next step."
                                                                                                : "final-inspection-draw-request-checklist".equals(slug)
                                                                                                        ? "Use this when the work is done and you are trying to get final inspection and draw-request paperwork right the first time."
                                                                                                        : "Use this guide to reduce ambiguity, then return to the route that makes the next decision.";}
!{var decisionPathTitle = "closeout".equals(decisionStage) ? "Finish the job, then close the file cleanly"
        : "quote".equals(decisionStage) ? "Interpret first, then stress-test the quote"
        : "interpret".equals(decisionStage) ? "Clear the blocker before you shop or sign"
        : "Use the guide, then go back to the decision route";}
!{var decisionPathIntro = "closeout".equals(decisionStage)
        ? "This guide helps with the last mile: permits, inspections, paperwork, and draw-request readiness after the work is finished."
        : "quote".equals(decisionStage)
                ? "Use the guide to slow the quote down and check fit, scope, and documentation before you commit."
                : "interpret".equals(decisionStage)
                        ? "Use the guide to clear up the report or file issue before you start shopping, signing, or planning around reimbursement."
                        : "Use the guide to narrow the choice, then return to the route that actually owns the next move.";}
!{var takeawayTitle = "closeout".equals(decisionStage) ? "What has to be true before the draw request"
        : "quote".equals(decisionStage) ? "What to verify before you sign"
        : "interpret".equals(decisionStage) ? "What to clear up first"
        : "What matters before you choose the next move";}
!{var avoidTitle = "closeout".equals(decisionStage) ? "Do not treat finished work as a finished file"
        : "quote".equals(decisionStage) ? "Do not let the quote outrun the recommendation"
        : "interpret".equals(decisionStage) ? "Do not let the blocker create a bad next step"
        : "Skip the expensive shortcuts";}
!{var nextStepTitle = "closeout".equals(decisionStage) ? "Finish the file cleanly"
        : "interpret".equals(decisionStage) ? "Clear the blocker, then return to the main path"
        : "Move back into the core routes";}
!{var relatedTitle = "closeout".equals(decisionStage) ? "Check the routes that still affect closeout"
        : "quote".equals(decisionStage) ? "Compare the routes that shape the quote"
        : "interpret".equals(decisionStage) ? "Check the routes that become relevant after the blocker clears"
        : "Compare the next likely routes";}
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
                        <p class="eyebrow">${guideEyebrow}</p>
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
                                <span>Best used at</span>
                                <strong>${stageLabel}</strong>
                            </div>
                            <div class="route-hero-fact">
                                <span>Do this now</span>
                                <strong>${page.page().primaryCta().label()}</strong>
                            </div>
                            <div class="route-hero-fact route-hero-fact--wide">
                                <span>Then use</span>
                                <p>${page.page().secondaryCta().label()}</p>
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
                        <p class="eyebrow">Use this page when</p>
                        <p>${decisionUse}</p>
                    </div>
                </div>
            </section>

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
                            <p class="eyebrow">Key takeaways</p>
                            <h2>${takeawayTitle}</h2>
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
                            <h2>${avoidTitle}</h2>
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
                            <h2>${nextStepTitle}</h2>
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
                        <h2>${relatedTitle}</h2>
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
