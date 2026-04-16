@import owner.mysafefloridahome.ops.AdminDashboardView
@param AdminDashboardView page
!{var reviewDate = page.promotionReview().reviewDate().toString();}
@template.layout.page(
    meta = page.meta(),
    siteName = "My Safe Florida Home Verdict",
    currentPath = "/admin/",
    content = @`
        <main>
            <section class="page-hero">
                <div class="section-shell">
                    @template.shared.breadcrumbs(page.meta())
                    <p class="eyebrow">Operations</p>
                    <h1>Route health and early demand signals</h1>
                    <p class="lead">Keep the launch wedge narrow while source freshness, route status, and improvement-specific lead signals become visible.</p>
                </div>
            </section>

            <section class="section">
                <div class="section-shell card-grid metrics-grid">
                    <article class="route-card">
                        <p class="card-tag">Leads</p>
                        <h3>${page.leadSummary().leadCount()}</h3>
                        <p>Stored lead submissions</p>
                    </article>
                    <article class="route-card">
                        <p class="card-tag">Partners</p>
                        <h3>${page.leadSummary().partnerInquiryCount()}</h3>
                        <p>Stored pilot inquiries</p>
                    </article>
                    <article class="route-card">
                        <p class="card-tag">Events</p>
                        <h3>${page.leadSummary().eventCount()}</h3>
                        <p>Tracked CTA and form events</p>
                    </article>
                    <article class="route-card">
                        <p class="card-tag">Sources</p>
                        <h3>${page.staleSources().size()}</h3>
                        <p>Sources past the next review date</p>
                    </article>
                </div>
            </section>

            <section class="section">
                <div class="section-shell two-column">
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">Leads by improvement</p>
                            <h2>What the form is capturing</h2>
                        </div>
                        <div class="stacked-links compact">
                        @for(var item : page.leadSummary().leadsByImprovement().entrySet())
                            <div class="text-link-card static-row"><span>${item.getKey()}</span><strong>${item.getValue()}</strong></div>
                        @else
                            <div class="text-link-card static-row"><span>No leads yet</span><strong>0</strong></div>
                        @endfor
                        </div>
                    </div>
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">Leads by partner type</p>
                            <h2>Commercial routing output</h2>
                        </div>
                        <div class="stacked-links compact">
                        @for(var item : page.leadSummary().leadsByPartnerType().entrySet())
                            <div class="text-link-card static-row"><span>${item.getKey()}</span><strong>${item.getValue()}</strong></div>
                        @else
                            <div class="text-link-card static-row"><span>No partner routing yet</span><strong>0</strong></div>
                        @endfor
                        </div>
                    </div>
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">Partner inquiries by route focus</p>
                            <h2>What contractors want to sponsor</h2>
                        </div>
                        <div class="stacked-links compact">
                        @for(var item : page.leadSummary().partnerInquiriesByRouteFocus().entrySet())
                            <div class="text-link-card static-row"><span>${item.getKey()}</span><strong>${item.getValue()}</strong></div>
                        @else
                            <div class="text-link-card static-row"><span>No pilot inquiries yet</span><strong>0</strong></div>
                        @endfor
                        </div>
                    </div>
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">CTA clicks by route family</p>
                            <h2>Which route family is earning action</h2>
                        </div>
                        <div class="stacked-links compact">
                        @for(var item : page.leadSummary().ctaClicksByRouteFamily().entrySet())
                            <div class="text-link-card static-row"><span>${item.getKey()}</span><strong>${item.getValue()}</strong></div>
                        @else
                            <div class="text-link-card static-row"><span>No click data yet</span><strong>0</strong></div>
                        @endfor
                        </div>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell two-column">
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">Events by type</p>
                            <h2>CTA activity</h2>
                        </div>
                        <div class="stacked-links compact">
                        @for(var item : page.leadSummary().eventsByType().entrySet())
                            <div class="text-link-card static-row"><span>${item.getKey()}</span><strong>${item.getValue()}</strong></div>
                        @else
                            <div class="text-link-card static-row"><span>No events yet</span><strong>0</strong></div>
                        @endfor
                        </div>
                    </div>
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">Stale route review</p>
                            <h2>Promotion should fail closed</h2>
                        </div>
                        <div class="stacked-links compact">
                        @for(var route : page.staleRoutes())
                            <div class="text-link-card static-column">
                                <strong>${route.route().path()}</strong>
                                <span>${route.effectiveRecommendationReason()}</span>
                            </div>
                        @else
                            <div class="text-link-card static-column">
                                <strong>No stale routes</strong>
                                <span>All routed source stacks are currently inside their review window.</span>
                            </div>
                        @endfor
                        </div>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">Promotion review</p>
                        <h2>${reviewDate}</h2>
                        <p>${page.promotionReview().agentSummary()}</p>
                    </div>
                    <div class="stacked-links compact">
                    @for(var note : page.promotionReview().heldRoutesStillNotReady())
                        <div class="text-link-card static-column">
                            <strong>${note.routeId()}</strong>
                            <span>${note.reason()}</span>
                        </div>
                    @else
                        <div class="text-link-card static-column">
                            <strong>No held routes noted</strong>
                            <span>The review file is empty.</span>
                        </div>
                    @endfor
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">Route status</p>
                        <h2>Current route board</h2>
                    </div>
                    <div class="table-wrap">
                        <table>
                            <thead>
                                <tr>
                                    <th>Route</th>
                                    <th>Phase</th>
                                    <th>Index</th>
                                    <th>Freshness</th>
                                    <th>Recommendation</th>
                                </tr>
                            </thead>
                            <tbody>
                            @for(var row : page.routeHealth())
                                <tr>
                                    <td>${row.route().path()}</td>
                                    <td>${row.route().phase()}</td>
                                    <td>${row.route().indexStatus()}</td>
                                    <td>${row.effectiveSourceFreshnessStatus()}</td>
                                    <td>${row.effectivePromotionRecommendation()}</td>
                                </tr>
                            @endfor
                            </tbody>
                        </table>
                    </div>
                </div>
            </section>
        </main>
    `
)
