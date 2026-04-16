@import owner.mysafefloridahome.pages.FamilyPageView
@param FamilyPageView page
!{var routePath = page.route().path();}
!{var canonicalUrl = page.meta().canonicalUrl();}
!{var baseUrl = canonicalUrl.substring(0, canonicalUrl.length() - routePath.length());}
@template.layout.page(
    meta = page.meta(),
    siteName = "My Safe Florida Home Verdict",
    currentPath = routePath,
    content = @`
        <main>
            @template.shared.pageSchema(
                meta = page.meta(),
                siteName = "My Safe Florida Home Verdict",
                about = "Topic hub for My Safe Florida Home decision routes and supporting pages"
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
                        <p class="eyebrow">${page.eyebrow()}</p>
                        <h1>${page.route().title()}</h1>
                        <p class="lead">${page.route().summary()}</p>
                        <div class="action-row">
                            <a class="primary-button" href="${page.primaryCards().get(0).href()}">${page.primaryCards().get(0).title()}</a>
                            <a class="secondary-button" href="/#decision-tool">Open the route guide</a>
                        </div>
                    </div>
                    <aside class="route-hero-panel">
                        <p class="micro-tag">Family summary</p>
                        <h2>${page.route().title()}</h2>
                        <p class="route-hero-panel__summary">${page.quickAnswer()}</p>
                        <div class="route-hero-facts">
                            <div class="route-hero-fact">
                                <span>Current family</span>
                                <strong>${page.eyebrow()}</strong>
                            </div>
                            <div class="route-hero-fact">
                                <span>Public routes</span>
                                <strong>${page.primaryCards().size()}</strong>
                            </div>
                            <div class="route-hero-fact route-hero-fact--wide">
                                <span>Search owner</span>
                                <p>${page.primaryIntro()}</p>
                            </div>
                        </div>
                    </aside>
                </div>
            </section>

            <section class="section">
                <div class="section-shell two-column">
                    <div class="info-panel">
                        <p class="eyebrow">Quick answer</p>
                        <p class="big-answer">${page.quickAnswer()}</p>
                    </div>
                    <div class="info-panel">
                        <p class="eyebrow">What not to assume</p>
                        <ul class="bullet-list danger-list">
                        @for(var item : page.whatNotToAssume())
                            <li>${item}</li>
                        @endfor
                        </ul>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell two-column">
                    <div class="info-panel emphasis">
                        <p class="eyebrow">Hub ownership</p>
                        <h2>Use one owner page before you compare adjacent routes</h2>
                        <p>${page.intro()}</p>
                    </div>
                    <div class="info-panel">
                        <p class="eyebrow">How to use this hub</p>
                        <p>Start with the page that owns your exact question. Use sibling routes only when the answer changes the next step, the contractor type, or the eligibility logic.</p>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">Core cluster</p>
                        <h2>${page.primaryHeading()}</h2>
                        <p>${page.primaryIntro()}</p>
                    </div>
                    <div class="route-detail-grid route-detail-grid--related">
                    @for(var card : page.primaryCards())
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

            @if(!page.secondaryCards().isEmpty())
                <section class="section">
                    <div class="section-shell">
                        <div class="section-heading">
                            <p class="eyebrow">Secondary cluster</p>
                            <h2>${page.secondaryHeading()}</h2>
                            <p>${page.secondaryIntro()}</p>
                        </div>
                        <div class="route-detail-grid route-detail-grid--related">
                        @for(var card : page.secondaryCards())
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
            @endif

            @template.shared.sourceStack(sources = page.sourceStack(), reviewedOn = page.meta().reviewedOn())
        </main>
    `
)
