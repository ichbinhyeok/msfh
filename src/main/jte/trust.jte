@import owner.mysafefloridahome.pages.TrustPageView
@param TrustPageView page
!{var slug = page.page().slug();}
!{var panelTitle = "about".equals(slug) ? "This site exists between the report and the quote"
        : "methodology".equals(slug) ? "Current sources outrank stale PDFs"
        : "contact".equals(slug) ? "Use this inbox for editorial issues, not case handling"
        : "not-government-affiliated".equals(slug) ? "Independent site, not the program"
        : "privacy".equals(slug) ? "Route data helps improve next-step guidance"
        : "terms".equals(slug) ? "Decision aid, not approval or legal advice"
        : "Current sources win";}
@template.layout.page(
    meta = page.meta(),
    siteName = "My Safe Florida Home Verdict",
    currentPath = page.route().path(),
    content = @`
        <main>
            @template.shared.pageSchema(
                meta = page.meta(),
                siteName = "My Safe Florida Home Verdict",
                about = "Editorial standards, transparency, and methodology for the My Safe Florida Home decision site"
            )
            <section class="page-hero route-hero">
                <div class="section-shell route-hero-shell">
                    <div class="route-hero-main">
                        @template.shared.breadcrumbs(page.meta())
                        <p class="eyebrow">Trust page</p>
                        <h1>${page.page().title()}</h1>
                        <p class="lead">${page.page().description()}</p>
                        <div class="action-row">
                            <a class="primary-button" href="${page.page().primaryCta().href()}">${page.page().primaryCta().label()}</a>
                            <a class="secondary-button" href="${page.page().secondaryCta().href()}">${page.page().secondaryCta().label()}</a>
                        </div>
                    </div>
                    <aside class="route-hero-panel">
                        <p class="micro-tag">Method summary</p>
                        <h2>${panelTitle}</h2>
                        <p class="route-hero-panel__summary">${page.page().quickAnswer()}</p>
                        <div class="route-hero-facts">
                            <div class="route-hero-fact">
                                <span>Primary route</span>
                                <strong>${page.page().primaryCta().label()}</strong>
                            </div>
                            <div class="route-hero-fact">
                                <span>Return path</span>
                                <strong>${page.page().secondaryCta().label()}</strong>
                            </div>
                            <div class="route-hero-fact route-hero-fact--wide">
                                <span>Review discipline</span>
                                <p>${page.page().intro()}</p>
                            </div>
                        </div>
                    </aside>
                </div>
            </section>

            <section class="section">
                <div class="section-shell two-column">
                    <div class="info-panel">
                        <p class="eyebrow">Quick answer</p>
                        <p class="big-answer">${page.page().quickAnswer()}</p>
                    </div>
                    <div class="info-panel">
                        <p class="eyebrow">What not to assume</p>
                        <ul class="bullet-list danger-list">
                        @for(var item : page.page().whatNotToAssume())
                            <li>${item}</li>
                        @endfor
                        </ul>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell prose-shell">
                    <p class="big-answer">${page.page().intro()}</p>
                    @for(var paragraph : page.page().bodySections())
                        <p>${paragraph}</p>
                    @endfor
                </div>
            </section>

            @if("contact".equals(slug))
                @template.shared.partnerInquiryForm(partnerStatus = page.partnerStatus())
            @endif

            <section class="section">
                <div class="section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">Related routes</p>
                        <h2>Move back into the main decision path</h2>
                    </div>
                    <div class="route-detail-grid route-detail-grid--related route-detail-grid--compact">
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
        </main>
    `
)
