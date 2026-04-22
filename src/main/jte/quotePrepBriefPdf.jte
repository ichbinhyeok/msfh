@import owner.mysafefloridahome.pages.QuotePrepBriefDocumentPageView
@param QuotePrepBriefDocumentPageView page
!{var caseReference = page.brief().briefId().substring(0, 8).toUpperCase();}
!{var preparedDate = page.brief().createdAt().toLocalDate().toString();}
!{var replyGuidance = page.replyGuidance();}
!{var missingItems = page.narrative().missingItems();}
!{var scenarioTitles = String.join("; ", page.narrative().scenarioModules().stream().map(module -> module.title()).toList());}
@template.layout.page(
    meta = page.meta(),
    siteName = "My Safe Florida Home Verdict",
    currentPath = "/tools/opening-protection/quote-prep-brief/",
    bodyClass = "quote-prep-brief-page quote-prep-brief-page--public quote-prep-brief-page--pdf",
    content = @`
        <main class="brief-export">
            <div class="section-shell brief-export-shell"
                 data-track-view="quote_prep_brief_pdf_export_open"
                 data-route-path="${page.meta().canonicalUrl().replace(page.meta().siteBaseUrl(), "")}"
                 data-route-family="quote-prep-brief"
                 data-scenario="opening-protection"
                 data-improvement-type="opening-protection"
                 data-detail="briefId=${page.brief().briefId()};surface=pdf-export">
                <div class="brief-export-toolbar">
                    <a class="secondary-button" href="${page.publicBriefPath()}">Back to Web Brief</a>
                    <button class="primary-button" type="button" data-print-page="true">Print or Save PDF</button>
                </div>

                <article class="brief-export-sheet">
                    <header class="brief-export-header">
                        <div class="brief-export-header__topline">
                            <p class="brief-export-kicker">Opening protection quote-prep brief</p>
                            <p class="brief-export-case">Case ${caseReference}</p>
                        </div>
                        <div class="brief-export-header__title-row">
                            <div class="brief-export-header__title">
                                <h1>${page.brief().siteLabel()}</h1>
                                <p>This is a first-pass quote request tied to one MSFH report recommendation. Keep the first quote narrow before it turns into whole-house pricing.</p>
                            </div>
                            <div class="brief-export-stamp">
                                <span>Prepared ${preparedDate}</span>
                                <span>${page.narrative().scopeLaneLabel()}</span>
                            </div>
                        </div>
                        <div class="brief-export-meta">
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
                            <p class="brief-export-label">What stays outside it</p>
                            <h2>Not included yet</h2>
                            <ul class="brief-export-list">
                                <li>${page.brief().boundaryScopeNote().isBlank() ? "Any opening not named in this brief stays outside the first quote path until the scope is restated clearly in writing." : page.brief().boundaryScopeNote()}</li>
                                @if(page.brief().broadPackageRequested())
                                    <li>Do not compare this brief to a broad whole-house package until the narrower opening-protection scope is restated clearly in writing.</li>
                                @endif
                            </ul>
                            <p class="brief-export-mini-label">Important limits</p>
                            <ul class="brief-export-compact-list">
                                <li>${page.brief().permitHandlingNote().isBlank() ? "Permit and inspection handling still need to be confirmed before signing." : page.brief().permitHandlingNote()}</li>
                                <li>This brief does not approve work or reimbursement.</li>
                                <li>Any window or door not named for this first quote stays outside scope.</li>
                                @if(!scenarioTitles.isBlank())
                                    <li>${scenarioTitles}.</li>
                                @endif
                            </ul>
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
                        @if(!missingItems.isEmpty())
                            <p class="brief-export-mini-label">If you still need something before pricing</p>
                            <ul class="brief-export-compact-list">
                                @for(var item : missingItems)
                                    <li>${item}</li>
                                @endfor
                            </ul>
                        @endif
                    </section>

                </article>
            </div>
        </main>
    `)
