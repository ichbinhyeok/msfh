@import owner.mysafefloridahome.pages.VendorHandoffDocumentPageView
@param VendorHandoffDocumentPageView page
!{var caseReference = page.handoff().handoffId().substring(0, 8).toUpperCase();}
!{var preparedDate = page.handoff().createdAt().toLocalDate().toString();}
!{var preparedBy = page.handoff().officeLabel().isBlank() ? "Homeowner-prepared brief" : page.handoff().officeLabel();}
!{var senderName = page.handoff().senderName().isBlank() ? "Homeowner" : page.handoff().senderName();}
!{var replyGuidance = page.replyGuidance();}
!{var missingItems = page.narrative().missingItems();}
!{var scenarioModules = page.narrative().scenarioModules().stream().limit(2).toList();}
!{var watchouts = page.narrative().watchouts().stream().limit(3).toList();}
@template.layout.page(
    meta = page.meta(),
    siteName = "My Safe Florida Home Verdict",
    currentPath = "/tools/opening-protection/quote-prep-brief/",
    bodyClass = "vendor-handoff-page vendor-handoff-page--public",
    content = @`
        <main class="brief-export brief-export--web">
            <div class="section-shell brief-export-shell"
                 data-track-view="vendor_handoff_brief_open"
                 data-route-path="${page.meta().canonicalUrl().replace(page.meta().siteBaseUrl(), "")}"
                 data-route-family="vendor-handoff"
                 data-scenario="opening-protection"
                 data-improvement-type="opening-protection"
                 data-detail="handoffId=${page.handoff().handoffId()};surface=public-brief">
                <div class="brief-export-toolbar brief-export-toolbar--web">
                    <div class="brief-export-toolbar__intro">
                        <p class="micro-tag">Shareable quote-prep brief</p>
                        <h1>${page.narrative().headline()}</h1>
                        <p>${page.narrative().customerSummary()}</p>
                    </div>
                    <a class="primary-button" href="${page.pdfExportPath()}">Open PDF Export</a>
                </div>

                <article class="brief-export-sheet brief-export-sheet--web">
                    <header class="brief-export-header">
                        <div class="brief-export-header__topline">
                            <p class="brief-export-kicker">Opening protection quote-prep brief</p>
                            <p class="brief-export-case">Case ${caseReference}</p>
                        </div>
                        <div class="brief-export-header__title-row">
                            <div class="brief-export-header__title">
                                <h2>${page.handoff().siteLabel()}</h2>
                                <p>This is a first-pass quote request tied to one MSFH report recommendation. It is meant to keep the first quote narrow, readable, and out of whole-house pricing drift.</p>
                            </div>
                            <div class="brief-export-stamp">
                                <span>Prepared ${preparedDate}</span>
                                <span>${page.narrative().scopeLaneLabel()}</span>
                            </div>
                        </div>
                        <div class="brief-export-meta">
                            <div class="brief-export-meta__item">
                                <span>Property</span>
                                <strong>${page.handoff().siteLabel()}</strong>
                            </div>
                            <div class="brief-export-meta__item">
                                <span>Prepared by</span>
                                <strong>${preparedBy}</strong>
                            </div>
                            <div class="brief-export-meta__item">
                                <span>Sender</span>
                                <strong>${senderName}</strong>
                            </div>
                            <div class="brief-export-meta__item">
                                <span>First quote focus</span>
                                <strong>${page.narrative().scopeLaneLabel()}</strong>
                            </div>
                        </div>
                        <p class="brief-export-trust">This brief helps narrow the first quote. It does not approve work, confirm reimbursement, or replace official program instructions.</p>
                    </header>

                    <div class="brief-export-grid">
                        <section class="brief-export-section brief-export-section--primary">
                            <p class="brief-export-label">What this first quote is for</p>
                            <h2>Scope for this first quote</h2>
                            <p>${page.narrative().openingsLine()}</p>
                            <p class="brief-export-note">${page.narrative().scopeLaneSummary()}</p>
                        </section>
                        <section class="brief-export-section">
                            <p class="brief-export-label">What I need back from you</p>
                            <h2>How to reply to this quote request</h2>
                            <p>${replyGuidance}</p>
                        </section>
                    </div>

                    <section class="brief-export-section">
                        <p class="brief-export-label">Reply quality</p>
                        <h2>Please answer these 3 points</h2>
                        <ol class="brief-export-list brief-export-list--ordered">
                            @for(var item : page.narrative().homeownerReplyChecklist())
                                <li>${item}</li>
                            @endfor
                        </ol>
                        <p class="brief-export-example">${page.narrative().homeownerReplyExample()}</p>
                    </section>

                    @if(!missingItems.isEmpty())
                        <section class="brief-export-section">
                            <p class="brief-export-label">If you still need something before pricing</p>
                            <h2>What can still be sent if needed</h2>
                            <ul class="brief-export-list">
                                @for(var item : missingItems)
                                    <li>${item}</li>
                                @endfor
                            </ul>
                        </section>
                    @endif

                    <div class="brief-export-grid">
                        <section class="brief-export-section">
                            <p class="brief-export-label">What is not included yet</p>
                            <h2>What stays outside this first quote for now</h2>
                            <ul class="brief-export-list">
                                <li>${page.handoff().boundaryScopeNote().isBlank() ? "Any opening not named in this brief stays outside the first quote path until the scope is restated clearly in writing." : page.handoff().boundaryScopeNote()}</li>
                                @if(page.handoff().broadPackageRequested())
                                    <li>Do not compare this brief to a broad whole-house package until the narrower opening-protection scope is restated clearly in writing.</li>
                                @endif
                            </ul>
                        </section>
                        <section class="brief-export-section">
                            <p class="brief-export-label">What still needs confirmation</p>
                            <h2>What this brief does not ask you to assume yet</h2>
                            <p>${page.handoff().permitHandlingNote().isBlank() ? "Permit and inspection handling still need to be confirmed before signing." : page.handoff().permitHandlingNote()}</p>
                            <ul class="brief-export-list">
                                @for(var item : watchouts)
                                    <li>${item}</li>
                                @endfor
                            </ul>
                        </section>
                    </div>

                    @if(!scenarioModules.isEmpty())
                        <section class="brief-export-section">
                            <p class="brief-export-label">Case watchouts</p>
                            <h2>What to slow down in this quote request</h2>
                            <div class="brief-export-module-list">
                                @for(var module : scenarioModules)
                                    <article class="brief-export-module">
                                        <p class="brief-export-module__eyebrow">${module.eyebrow()}</p>
                                        <h3>${module.title()}</h3>
                                        <p>${module.body()}</p>
                                    </article>
                                @endfor
                            </div>
                        </section>
                    @endif

                    <footer class="brief-export-footer">
                        <span>${preparedBy}${page.handoff().senderName().isBlank() ? "" : " | " + page.handoff().senderName()}</span>
                        <span>Opening protection quote-prep brief | Case ${caseReference}</span>
                    </footer>
                </article>
            </div>
        </main>
    `)
