@import owner.mysafefloridahome.pages.VendorPacketPageView
@param VendorPacketPageView page
!{var preQuoteTool = page.meta().canonicalUrl().contains("/quote-prep-brief/build/") || page.meta().canonicalUrl().contains("/quote-ready-packet/") || page.meta().canonicalUrl().contains("/pre-quote/");}
!{var estimatorTool = page.meta().canonicalUrl().contains("/estimator-handoff/");}
!{var quoteTool = page.meta().canonicalUrl().contains("/quote-boundary/");}
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
                            <a class="secondary-button" href="${page.vendorEntryPath()}">Back to Quote-Prep Brief</a>
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
                            <form class="packet-tool-form quote-prep-workbench__form" data-vendor-prequote-form="true" method="post" action="/tools/opening-protection/quote-prep-brief/create">
                                <input name="replyInstructions" type="hidden" value="">
                                <input name="serviceAreaNote" type="hidden" value="">
                                <input name="permitHandlingNote" type="hidden" value="">
                                <input name="attachedScopeNote" type="hidden" value="">
                                <input name="boundaryScopeNote" type="hidden" value="">
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
                                <div class="packet-list-card quote-prep-form-group quote-prep-form-group--optional">
                                    <p class="micro-tag">Optional contractor label</p>
                                    <h3>Add this only if a contractor already asked for it</h3>
                                    <p>Leave this blank if you are sending the brief yourself. Use it only when you already know the label or contact a contractor wants on the brief.</p>
                                    <label class="console-field">
                                        <span>Contractor label (optional)</span>
                                        <input name="officeLabel" type="text" value="" placeholder="Palm Coast Openings Desk">
                                    </label>
                                    <label class="console-field">
                                        <span>Contact name (optional)</span>
                                        <input name="senderName" type="text" value="" placeholder="Amanda from intake">
                                    </label>
                                    <p class="console-field-note">The free brief works without this. It only helps when one contractor wants the same label reused.</p>
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

            @if(estimatorTool)
                <section class="section">
                    <div class="section-shell packet-section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">Internal review</p>
                        <h2>Estimator handoff worksheet</h2>
                        <p>Use this only after the first reply comes back narrower. The goal is to protect estimator time, not to expand the free layer into an office suite.</p>
                    </div>
                    <div class="packet-tool-grid">
                        <form class="packet-tool-form" data-vendor-handoff-form="true">
                            <label class="console-field">
                                <span>Home type</span>
                                    <select name="homeType">
                                        <option value="detached">Detached</option>
                                        <option value="attached">Attached or townhouse-like</option>
                                    </select>
                                </label>
                                <label class="console-field">
                                    <span>Recommendation clarity</span>
                                    <select name="recommendationClarity">
                                        <option value="clear">Opening protection is clear in the report</option>
                                        <option value="unclear">Likely opening protection but still fuzzy</option>
                                        <option value="missing">Report wording or page is still missing</option>
                                    </select>
                                </label>
                                <label class="packet-check">
                                    <input type="checkbox" name="reportPageReceived" checked>
                                    <span>Relevant report page received</span>
                                </label>
                            <label class="packet-check">
                                <input type="checkbox" name="openingPhotosReceived" checked>
                                <span>Opening photos received</span>
                            </label>
                            <label class="packet-check">
                                <input type="checkbox" name="scopeListed" checked>
                                <span>Relevant openings are explicitly listed</span>
                            </label>
                            <label class="packet-check">
                                <input type="checkbox" name="broadPackageRequested">
                                <span>The inquiry is asking for a broad whole-house package</span>
                            </label>
                                <label class="packet-check">
                                    <input type="checkbox" name="roofDrift">
                                    <span>Inquiry is drifting into roof-heavy or unrelated work</span>
                                </label>
                                <label class="packet-check">
                                    <input type="checkbox" name="reimbursementAssumed">
                                    <span>Someone sounds like reimbursement is already assumed</span>
                                </label>
                            </form>
                            <div class="packet-tool-output">
                                <div class="packet-tool-callout">
                                    <p class="micro-tag">Recommended next action</p>
                                    <h3 data-handoff-decision="true">Ready for short clarification call</h3>
                                    <p data-handoff-reason="true">Use the worksheet to decide whether the inquiry is quote-ready, needs a clarification call, or should be slowed down before scheduling.</p>
                                </div>
                                <div class="packet-list-card">
                                    <p class="micro-tag">Internal summary</p>
                                    <textarea class="packet-output" data-handoff-output="true" readonly></textarea>
                                    <div class="action-row packet-tool-actions">
                                        <button class="secondary-button" type="button" data-copy-target="handoff-output">Copy Summary</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            @endif

            @if(quoteTool)
                <section class="section">
                    <div class="section-shell packet-section-shell">
                        <div class="section-heading">
                            <p class="eyebrow">Internal review</p>
                            <h2>Quote boundary note</h2>
                            <p>Use this only after the first reply is narrow enough to support a draft quote. This is internal quote wording, not the product wedge.</p>
                        </div>
                        <div class="packet-tool-grid">
                            <form class="packet-tool-form" data-vendor-boundary-form="true">
                                <label class="console-field">
                                    <span>Included openings</span>
                                    <textarea name="includedOpenings" rows="3">List only the openings this quote actually covers.</textarea>
                                </label>
                                <label class="console-field">
                                    <span>Excluded openings or items</span>
                                    <textarea name="excludedOpenings" rows="3">List any opening or item that is outside this quote unless clearly added later.</textarea>
                                </label>
                                <label class="console-field">
                                    <span>Primary product or path</span>
                                    <select name="productPath">
                                        <option value="opening protection work">Opening protection work</option>
                                        <option value="window replacement path">Window replacement path</option>
                                        <option value="shutter path">Shutter path</option>
                                        <option value="mixed opening-protection path">Mixed opening-protection path</option>
                                    </select>
                                </label>
                                <label class="console-field">
                                    <span>Permit and inspection handling</span>
                                    <select name="permitHandlingMode">
                                        <option value="included">Included in the quote</option>
                                        <option value="confirm">Still needs confirmation</option>
                                        <option value="excluded">Not included unless stated</option>
                                    </select>
                                </label>
                                <label class="packet-check">
                                    <input type="checkbox" name="attachedCaution">
                                    <span>Attached-home caution should stay visible on the front page</span>
                                </label>
                                <label class="packet-check">
                                    <input type="checkbox" name="optionalUpgrades">
                                    <span>Optional upgrades are present and must stay separated</span>
                                </label>
                                <label class="packet-check">
                                    <input type="checkbox" name="excludeOtherWork" checked>
                                    <span>Unrelated roof or other work should be stated as outside this quote</span>
                                </label>
                            </form>
                            <div class="packet-tool-output">
                                <div class="packet-list-card">
                                    <p class="micro-tag">Front-page quote note</p>
                                    <textarea class="packet-output packet-output--tall" data-boundary-output="true" readonly></textarea>
                                    <div class="action-row packet-tool-actions">
                                        <button class="secondary-button" type="button" data-copy-target="boundary-output">Copy Boundary Note</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            @endif

            <section class="section">
                <div class="section-shell">
                    @if(preQuoteTool)
                        <div class="section-heading">
                            <p class="eyebrow">Keep it narrow</p>
                            <h2>Keep the next step off this first send</h2>
                            <p>The free product is the public brief. Once the homeowner replies more narrowly, the office can review that reply internally before estimator time gets spent.</p>
                        </div>
                        <div class="packet-list-card packet-list-card--warning">
                            <ul class="bullet-list danger-list">
                                <li>Do not send internal worksheets or office notes with this first brief.</li>
                                <li>Do not move into estimator scheduling until the first reply comes back narrower and cleaner.</li>
                                <li>Do not widen the job just because internal sheets exist behind this page.</li>
                            </ul>
                        </div>
                    @else
                        <div class="section-heading">
                            <p class="eyebrow">Keep it internal</p>
                            <h2>This page exists after the brief, not before it</h2>
                            <p>Use this only after the homeowner-facing brief has already done its job. Do not let this internal page become the visible product.</p>
                        </div>
                        <div class="packet-list-card packet-list-card--warning">
                            <ul class="bullet-list danger-list">
                                <li>Do not send this internal worksheet or note before the public brief.</li>
                                <li>Do not treat internal helper pages as the product wedge.</li>
                                <li>Do not add more layers just because this page exists.</li>
                            </ul>
                        </div>
                    @endif
                </div>
            </section>
        </main>
    `
)
