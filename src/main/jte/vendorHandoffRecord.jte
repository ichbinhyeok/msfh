@import owner.mysafefloridahome.pages.VendorHandoffDocumentPageView
@param VendorHandoffDocumentPageView page
@template.layout.page(
    meta = page.meta(),
    siteName = "My Safe Florida Home Verdict",
    currentPath = "/tools/opening-protection/quote-prep-brief/",
    bodyClass = "vendor-handoff-page vendor-handoff-page--record",
    content = @`
        <main>
            <section class="page-hero packet-hero">
                <div class="section-shell packet-hero-shell"
                     data-track-view="vendor_handoff_record_open"
                     data-route-path="${page.meta().canonicalUrl().replace(page.meta().siteBaseUrl(), "")}"
                     data-route-family="vendor-handoff"
                     data-scenario="opening-protection"
                     data-improvement-type="opening-protection"
                     data-detail="handoffId=${page.handoff().handoffId()};surface=office-record">
                    <div class="route-hero-main">
                        @template.shared.breadcrumbs(page.meta())
                        <p class="eyebrow">Office record</p>
                        <h1>${page.narrative().headline()}</h1>
                        <p class="lead">Keep this sheet behind the shareable quote-prep brief. This is the office-facing copy for the intake anchor, missing items, and next internal move.</p>
                        <div class="action-row">
                            <a class="primary-button" href="${page.resultPath()}">Back to result console</a>
                            <a class="secondary-button" href="${page.prefilledEstimatorSheetPath()}">Open Prefilled Estimator Handoff</a>
                            <a class="secondary-button" href="${page.prefilledQuoteBoundaryPath()}">Open Prefilled Quote Boundary</a>
                            <button class="secondary-button"
                                    type="button"
                                    data-copy-target="office-record-url"
                                    data-track-event="vendor_handoff_record_copy"
                                    data-route-path="${page.meta().canonicalUrl().replace(page.meta().siteBaseUrl(), "")}"
                                    data-route-family="vendor-handoff"
                                    data-scenario="opening-protection"
                                    data-improvement-type="opening-protection"
                                    data-detail="handoffId=${page.handoff().handoffId()};surface=office-record;target=record-url">Copy Office Record URL</button>
                        </div>
                    </div>
                    <aside class="route-hero-panel">
                        <p class="micro-tag">Front surface stays first</p>
                        <h2>Use only after the brief exists</h2>
                        <p class="route-hero-panel__summary">${page.narrative().nextReason()}</p>
                    </aside>
                </div>
            </section>

            <section class="section">
                <div class="section-shell packet-section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">Office record URL</p>
                        <h2>Internal copy for the team</h2>
                    </div>
                    <div class="packet-list-card">
                        <label class="console-field">
                            <span>Office record URL</span>
                            <input data-office-record-url="true" type="text" readonly value="${page.meta().canonicalUrl()}">
                        </label>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell two-column">
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">Stored intake</p>
                            <h2>What this handoff is anchored to</h2>
                        </div>
                        <div class="packet-list-card">
                            <ul class="bullet-list">
                                <li>Site: ${page.handoff().siteLabel()}</li>
                                <li>County or ZIP: ${page.handoff().countyZip().isBlank() ? "still needed" : page.handoff().countyZip()}</li>
                                <li>Home type: ${"attached".equals(page.handoff().homeType()) ? "Attached or townhouse-like" : "Detached"}</li>
                                <li>Current focus: ${page.narrative().scopeLaneLabel()}</li>
                                <li>Recommendation line: ${page.handoff().recommendationLine().isBlank() ? "still needed" : page.handoff().recommendationLine()}</li>
                                <li>Openings inside first quote: ${page.handoff().scopeOpenings().isBlank() ? "still needed" : page.handoff().scopeOpenings()}</li>
                                <li>Office label: ${page.handoff().officeLabel().isBlank() ? "not set" : page.handoff().officeLabel()}</li>
                                <li>Sender name: ${page.handoff().senderName().isBlank() ? "not set" : page.handoff().senderName()}</li>
                                <li>Reply instructions: ${page.handoff().replyInstructions().isBlank() ? "not set" : page.handoff().replyInstructions()}</li>
                                <li>Service area note: ${page.handoff().serviceAreaNote().isBlank() ? "not set" : page.handoff().serviceAreaNote()}</li>
                                <li>Report page received: ${page.handoff().reportPageReceived() ? "yes" : "no"}</li>
                                <li>Photos received: ${page.handoff().photosReceived() ? "yes" : "no"}</li>
                                <li>Broad package requested: ${page.handoff().broadPackageRequested() ? "yes" : "no"}</li>
                                <li>Created at: ${page.handoff().createdAt().toString()}</li>
                            </ul>
                        </div>
                    </div>
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">Recommendation anchor</p>
                            <h2>What the office should keep repeating</h2>
                        </div>
                        <div class="packet-list-card">
                            <p>${page.narrative().customerSummary()}</p>
                            <p>${page.narrative().scopeLaneSummary()}</p>
                            <p>${page.narrative().requestLine()}</p>
                            <p>${page.narrative().openingsLine()}</p>
                            @if(!page.narrative().missingItems().isEmpty())
                                <ul class="bullet-list">
                                    @for(var item : page.narrative().missingItems())
                                        <li>Still missing: ${item}</li>
                                    @endfor
                                </ul>
                            @endif
                        </div>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell two-column">
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">Office workflow</p>
                            <h2>What the team still needs to do</h2>
                        </div>
                        <div class="packet-list-card">
                            <ul class="bullet-list">
                                @for(var item : page.narrative().officeSteps())
                                    <li>${item}</li>
                                @endfor
                            </ul>
                        </div>
                    </div>
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">Continue this brief</p>
                            <h2>Carry the same case into the next office layer</h2>
                        </div>
                        <div class="route-detail-grid route-detail-grid--related">
                            <a class="route-detail-card" href="${page.prefilledEstimatorSheetPath()}">
                                <p class="micro-tag">Office layer</p>
                                <h3>Estimator handoff</h3>
                                <p>Open the go-or-no-go worksheet with the current brief details already carried forward.</p>
                                <span class="route-detail-card__footer">Continue into estimator triage</span>
                            </a>
                            <a class="route-detail-card" href="${page.prefilledQuoteBoundaryPath()}">
                                <p class="micro-tag">Quote layer</p>
                                <h3>Quote boundary sheet</h3>
                                <p>Open the front-page note with the included opening scope already carried forward.</p>
                                <span class="route-detail-card__footer">Continue into draft-quote control</span>
                            </a>
                            <a class="route-detail-card"
                               href="${page.improvementGuidePath()}"
                               data-track-event="vendor_handoff_recovery_click"
                               data-route-path="${page.meta().canonicalUrl().replace(page.meta().siteBaseUrl(), "")}"
                               data-route-family="vendor-handoff"
                               data-scenario="opening-protection"
                               data-improvement-type="opening-protection"
                               data-detail="handoffId=${page.handoff().handoffId()};surface=office-record;cta=opening-protection-guide">
                                <p class="micro-tag">Guide rail</p>
                                <h3>Opening protection guide</h3>
                                <p>Use the public guide when the office needs a deeper explanation behind the recommendation.</p>
                                <span class="route-detail-card__footer">Open guide</span>
                            </a>
                            <a class="route-detail-card"
                               href="${page.quoteChecklistPath()}"
                               data-track-event="vendor_handoff_recovery_click"
                               data-route-path="${page.meta().canonicalUrl().replace(page.meta().siteBaseUrl(), "")}"
                               data-route-family="vendor-handoff"
                               data-scenario="opening-protection"
                               data-improvement-type="opening-protection"
                               data-detail="handoffId=${page.handoff().handoffId()};surface=office-record;cta=quote-checklist">
                                <p class="micro-tag">Guide rail</p>
                                <h3>Quote checklist</h3>
                                <p>Use this before the office lets a broader or fuzzier draft quote move forward.</p>
                                <span class="route-detail-card__footer">Open checklist</span>
                            </a>
                        </div>
                    </div>
                </div>
            </section>
        </main>
    `)
