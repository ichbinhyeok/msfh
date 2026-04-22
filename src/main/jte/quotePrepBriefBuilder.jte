@import owner.mysafefloridahome.pages.QuotePrepBriefBuilderPageView
@param QuotePrepBriefBuilderPageView page
!{var preQuoteTool = page.meta().canonicalUrl().contains("/quote-prep-brief/build/");}
@template.layout.page(
    meta = page.meta(),
    siteName = "My Safe Florida Home Verdict",
    currentPath = "/tools/opening-protection/quote-prep-brief/build/",
    bodyClass = "quote-prep-page quote-prep-page--builder",
    content = @`
        <main>
            <section class="page-hero route-hero quote-prep-hero quote-prep-hero--builder">
                <div class="section-shell route-hero-shell">
                    <div class="route-hero-main">
                        @template.shared.breadcrumbs(page.meta())
                        <p class="eyebrow">${page.eyebrow()}</p>
                        <h1>${page.title()}</h1>
                        <p class="lead">${page.description()}</p>
                        <div class="action-row">
                            <a class="secondary-button" href="${page.entryPath()}">Back to Quote-Prep Brief</a>
                            <a class="secondary-button" href="/improvements/opening-protection/">Opening-Protection Route</a>
                        </div>
                    </div>
                    <aside class="route-hero-panel route-hero-panel--clarification">
                        <p class="micro-tag">What you finish with</p>
                        <h2>One shareable brief before the conversation drifts</h2>
                        <p class="route-hero-panel__summary">${page.quickAnswer()}</p>
                        <div class="route-hero-facts">
                            <div class="route-hero-fact">
                                <span>Best used at</span>
                                <strong>Before first contractor pricing</strong>
                            </div>
                            <div class="route-hero-fact">
                                <span>What you export</span>
                                <strong>Share link plus PDF</strong>
                            </div>
                            <div class="route-hero-fact route-hero-fact--wide">
                                <span>Why this helps</span>
                                <p>${preQuoteTool ? "Share one public brief before the first contractor reply widens the job." : page.roleSummary()}</p>
                            </div>
                        </div>
                    </aside>
                </div>
            </section>

            @if(!preQuoteTool)
                <section class="section">
                    <div class="section-shell two-column">
                        <div class="info-panel emphasis">
                            <p class="eyebrow">Quick answer</p>
                            <p class="big-answer">${page.quickAnswer()}</p>
                        </div>
                        <div class="info-panel">
                            <p class="eyebrow">${page.roleTitle()}</p>
                            <p>${page.roleSummary()}</p>
                        </div>
                    </div>
                </section>
            @endif

            @if(preQuoteTool)
                <section class="section">
                    <div class="section-shell packet-section-shell">
                        <div class="section-heading">
                            <p class="eyebrow">Build your shareable brief</p>
                            <h2>Turn the report into one contractor-ready first request</h2>
                            <p>Use this before pricing so one plain-language scope brief goes to the contractor instead of scattered screenshots, paraphrased recommendation text, or a whole-house package too early.</p>
                        </div>
                        <div class="packet-tool-grid quote-prep-workbench">
                            <form class="packet-tool-form quote-prep-workbench__form" data-quote-prep-form="true" method="post" action="/tools/opening-protection/quote-prep-brief/create">
                                @if(!page.blockingMessage().isBlank())
                                    <div class="packet-list-card packet-list-card--warning" data-prequote-blocking-message="true">
                                        <p class="micro-tag">Cannot create the public brief yet</p>
                                        <p>${page.blockingMessage()}</p>
                                    </div>
                                @endif
                                <label class="console-field">
                                    <span>Home label</span>
                                    <input name="siteLabel" type="text" value="" placeholder="123 Palm Avenue home">
                                </label>
                                <label class="console-field">
                                    <span>County or ZIP</span>
                                    <input name="countyZip" type="text" value="" placeholder="Miami-Dade or 33176">
                                </label>
                                <label class="console-field">
                                    <span>Home type</span>
                                    <select name="homeType">
                                        <option value="detached">Detached</option>
                                        <option value="attached">Attached or townhouse-like</option>
                                    </select>
                                </label>
                                <label class="console-field">
                                    <span>Current quote focus</span>
                                    <select name="scopeLane">
                                        <option value="">Choose the narrowest focus first</option>
                                        <option value="windows">Windows only</option>
                                        <option value="shutters">Shutters only</option>
                                        <option value="doors">Doors or garage-door protection only</option>
                                        <option value="mixed">A smaller mixed opening-protection scope</option>
                                        <option value="broad">Still too broad or unclear</option>
                                    </select>
                                </label>
                                <label class="console-field">
                                    <span>Report recommendation line</span>
                                    <input name="recommendationLine" type="text" value="" placeholder="Opening protection recommendation from the report">
                                </label>
                                <label class="console-field">
                                    <span>Specific openings inside the first quote</span>
                                    <textarea name="scopeOpenings" rows="3" placeholder="Front windows (6) and rear slider only, not every opening in the house."></textarea>
                                </label>
                                <div class="packet-list-card quote-prep-form-group">
                                    <p class="micro-tag">What you already have ready</p>
                                    <p>Check only what can already be shared with the contractor in the same first message.</p>
                                    <div class="quote-prep-check-grid">
                                        <label class="packet-check">
                                            <input type="checkbox" name="reportPageReceived">
                                            <span>I already have the relevant report page ready to share</span>
                                        </label>
                                        <label class="packet-check">
                                            <input type="checkbox" name="photosReceived">
                                            <span>I already have opening photos ready to share</span>
                                        </label>
                                        <label class="packet-check">
                                            <input type="checkbox" name="broadPackageRequested">
                                            <span>This conversation is already drifting into a broad whole-house package</span>
                                        </label>
                                        <label class="packet-check">
                                            <input type="checkbox" name="compareQuotesRequested">
                                            <span>I want to compare contractors against the same narrow scope</span>
                                        </label>
                                        <label class="packet-check">
                                            <input type="checkbox" name="reimbursementAssumed">
                                            <span>I am worried someone will assume approval or reimbursement is already settled</span>
                                        </label>
                                        <label class="packet-check">
                                            <input type="checkbox" name="hoaReviewLikely">
                                            <span>HOA or condo review may still matter before scope is final</span>
                                        </label>
                                    </div>
                                </div>
                                <div class="packet-list-card packet-list-card--warning quote-prep-form-group quote-prep-form-group--warning">
                                    <p class="micro-tag">Keep the first ask narrow</p>
                                    <ul class="bullet-list danger-list">
                                        <li>Do not use this to count every opening or build a room-by-room takeoff.</li>
                                        <li>Do not turn this into a broad whole-house request before the first quote is framed.</li>
                                        <li>Do not answer permit, reimbursement, or whole-house pricing questions here.</li>
                                    </ul>
                                </div>
                                <div class="action-row packet-tool-actions">
                                    <button class="primary-button" type="submit" data-prequote-submit="true">Create Quote-Prep Brief</button>
                                </div>
                            </form>
                            <div class="packet-tool-output">
                                <div class="packet-preview-sheet quote-prep-document-preview">
                                    <div class="quote-prep-document-preview__topline">
                                        <p class="micro-tag">Shareable brief preview</p>
                                        <span class="quote-prep-document-preview__status">Draft for first send</span>
                                    </div>
                                    <h3 data-prequote-preview-title="true">Opening protection quote-prep brief</h3>
                                    <div class="quote-prep-document-preview__meta">
                                        <div class="quote-prep-document-preview__meta-item">
                                            <span>Document use</span>
                                            <strong>First contractor request</strong>
                                        </div>
                                        <div class="quote-prep-document-preview__meta-item">
                                            <span>Primary goal</span>
                                            <strong>Narrow the first reply</strong>
                                        </div>
                                    </div>
                                    <section class="quote-prep-document-preview__section">
                                        <p class="micro-tag">Purpose</p>
                                        <p data-prequote-preview-summary="true">Use this brief to explain what the first quote can cover now, what focus it is in, what is still missing, and what not to compare yet.</p>
                                    </section>
                                    <section class="quote-prep-document-preview__section">
                                        <p class="micro-tag">Included in this first send</p>
                                        <ul class="packet-preview-sheet__list" data-prequote-preview-list="true"></ul>
                                    </section>
                                    <div class="quote-prep-document-preview__footer">
                                        <span>Share link plus message</span>
                                        <span>Before pricing or measurement</span>
                                    </div>
                                </div>
                                <div class="packet-list-card quote-prep-reply-card">
                                    <p class="micro-tag">Good contractor reply</p>
                                    <h3>A useful reply should stay inside 3 things</h3>
                                    <ol class="packet-list">
                                        <li data-prequote-return-report="true">Say whether the report page is still needed before pricing</li>
                                        <li data-prequote-return-photos="true">Say whether clearer opening photos are still needed before pricing</li>
                                        <li data-prequote-return-focus="true">Confirm the first quote can stay inside this focus and these openings</li>
                                    </ol>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            @endif

            <section class="section">
                <div class="section-shell packet-section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">Core checklist</p>
                        <h2>${page.checklistTitle()}</h2>
                        <p>${page.checklistIntro()}</p>
                    </div>
                    <div class="packet-list-card">
                        <ol class="packet-list">
                        @for(var item : page.checklistItems())
                            <li>${item}</li>
                        @endfor
                        </ol>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell two-column">
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">Second pass</p>
                            <h2>${page.secondaryTitle()}</h2>
                            <p>${page.secondaryIntro()}</p>
                        </div>
                        <div class="packet-list-card">
                            <ul class="bullet-list">
                            @for(var item : page.secondaryItems())
                                <li>${item}</li>
                            @endfor
                            </ul>
                        </div>
                    </div>
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">Keep it narrow</p>
                            <h2>${page.cautionTitle()}</h2>
                        </div>
                        <div class="packet-list-card packet-list-card--warning">
                            <ul class="bullet-list danger-list">
                            @for(var item : page.cautionItems())
                                <li>${item}</li>
                            @endfor
                            </ul>
                        </div>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">Keep it narrow</p>
                        <h2>Keep the next step off this first send</h2>
                        <p>The product here is one homeowner-ready brief. Use the contractor reply to narrow the conversation before you do anything broader.</p>
                    </div>
                    <div class="packet-list-card packet-list-card--warning">
                        <ul class="bullet-list danger-list">
                            <li>Do not turn this first message into a whole-house package request.</li>
                            <li>Do not treat the brief as approval, reimbursement confirmation, or a signed scope.</li>
                            <li>Do not compare bids until the replies are addressing the same named openings and the same limits.</li>
                        </ul>
                    </div>
                </div>
            </section>
        </main>
    `
)
