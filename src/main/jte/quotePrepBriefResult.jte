@import owner.mysafefloridahome.pages.QuotePrepBriefResultPageView
@param QuotePrepBriefResultPageView page
!{var clarificationState = "clarification".equals(page.narrative().sendState());}
!{var blockedState = "blocked".equals(page.narrative().sendState());}
!{var publicBriefOpened = page.metrics().publicBriefOpenCount() > 0;}
!{var sendPrepStarted = page.metrics().sendNoteCopyCount() > 0 || page.metrics().briefCopyCount() > 0;}
!{var replySignalsLogged = page.metrics().replyNarrowedCount() > 0 || page.metrics().replyReportPageCount() > 0 || page.metrics().replyPhotoCount() > 0;}
@template.layout.page(
    meta = page.meta(),
    siteName = "My Safe Florida Home Verdict",
    currentPath = "/tools/opening-protection/quote-prep-brief/",
    bodyClass = "quote-prep-page quote-prep-page--result quote-prep-brief-page--public",
    content = @`
        <main>
            <section class="page-hero route-hero quote-prep-hero quote-prep-hero--result">
                <div class="section-shell route-hero-shell">
                    <div class="route-hero-main"
                         data-track-view="quote_prep_brief_result_open"
                         data-route-path="${page.meta().canonicalUrl().replace(page.meta().siteBaseUrl(), "")}"
                        data-route-family="quote-prep-brief"
                        data-scenario="opening-protection"
                        data-improvement-type="opening-protection"
                        data-detail="briefId=${page.brief().briefId()};surface=result">
                        <div class="quote-prep-result-mark" aria-hidden="true">
                            <span>Ready</span>
                        </div>
                        <p class="eyebrow">${page.narrative().sendStateLabel()}</p>
                        <h1>${page.narrative().headline()}</h1>
                        <p class="lead">${page.narrative().sendStateSummary()}</p>
                        <div class="action-row">
                            <a class="primary-button"
                               href="${page.publicBriefPath()}"
                               data-track-event="quote_prep_brief_recovery_click"
                               data-route-path="${page.meta().canonicalUrl().replace(page.meta().siteBaseUrl(), "")}"
                               data-route-family="quote-prep-brief"
                               data-scenario="opening-protection"
                                    data-improvement-type="opening-protection"
                                    data-detail="briefId=${page.brief().briefId()};surface=result;cta=open-public-brief">Open Shareable Brief</a>
                            <button class="secondary-button"
                                    type="button"
                                    data-copy-target="brief-share-url"
                                    data-track-event="quote_prep_brief_copy"
                                    data-route-path="${page.meta().canonicalUrl().replace(page.meta().siteBaseUrl(), "")}"
                                    data-route-family="quote-prep-brief"
                                    data-scenario="opening-protection"
                                    data-improvement-type="opening-protection"
                                    data-detail="briefId=${page.brief().briefId()};surface=result;target=brief-url">Copy Brief Link</button>
                            <a class="secondary-button" href="${page.pdfExportPath()}">Open PDF Export</a>
                        </div>
                        <div class="quote-prep-result-context">
                            <div class="quote-prep-result-context__item">
                                <span>Property</span>
                                <strong>${page.brief().siteLabel()}</strong>
                            </div>
                            <div class="quote-prep-result-context__item">
                                <span>First quote focus</span>
                                <strong>${page.narrative().scopeLaneLabel()}</strong>
                            </div>
                            <div class="quote-prep-result-context__item">
                                <span>Share state</span>
                                <strong>${clarificationState ? "Clarification pass first" : blockedState ? "Missing anchors first" : "Ready for first send"}</strong>
                            </div>
                        </div>
                    </div>
                    <aside class="route-hero-panel ${blockedState ? "route-hero-panel--blocked" : "route-hero-panel--clarification"}">
                        <p class="micro-tag">${clarificationState ? "Narrow first" : blockedState ? "Not ready yet" : "Share this first"}</p>
                        <h2>${clarificationState ? "Use this to narrow the first reply first" : blockedState ? "Fill the missing anchors before any share" : "One clear scope brief before the first quote widens"}</h2>
                        <p class="route-hero-panel__summary">${page.narrative().nextReason()}</p>
                        <div class="route-hero-facts">
                            <div class="route-hero-fact">
                                <span>Property</span>
                                <strong>${page.brief().siteLabel()}</strong>
                            </div>
                            <div class="route-hero-fact route-hero-fact--wide">
                                <span>Send goal</span>
                                <p>${clarificationState ? "Get one narrower reply before pricing or measurements." : blockedState ? "Fill the missing anchors before sharing." : "Get one clean reply before the conversation widens."}</p>
                            </div>
                        </div>
                    </aside>
                </div>
            </section>

            <section class="section">
                <div class="section-shell two-column">
                    <div class="info-panel emphasis">
                        <p class="eyebrow">What you have now</p>
                        <p class="big-answer">${clarificationState ? "Share the contractor-facing brief as a clarification pass first. The quality bar is a narrower, more organized reply before time gets spent." : "Share the contractor-facing quote-prep brief first. The quality bar is that the first reply comes back narrower and more organized, not broader and more confusing."}</p>
                    </div>
                    <div class="info-panel">
                        <p class="eyebrow">Prepared for</p>
                        <p>${page.brief().siteLabel()}</p>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell two-column">
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">Share this brief</p>
                            <h2>${clarificationState ? "Use this for the clarification pass" : "Use this for the first share"}</h2>
                            <p>${clarificationState ? "Keep the link and contractor message clean. The goal is a narrower reply before pricing or scheduling." : "Keep the link and contractor message clean. The goal is a narrower reply before any visit or measurement gets scheduled."}</p>
                        </div>
                        <div class="packet-list-card quote-prep-share-card quote-prep-share-card--link">
                            <label class="console-field">
                                <span>Shareable brief link</span>
                                <input data-brief-share-url="true" type="text" readonly value="${page.shareUrl()}">
                            </label>
                            <div class="action-row packet-tool-actions">
                                <button class="secondary-button"
                                        type="button"
                                        data-copy-target="brief-share-url"
                                        data-track-event="quote_prep_brief_copy"
                                        data-route-path="${page.meta().canonicalUrl().replace(page.meta().siteBaseUrl(), "")}"
                                        data-route-family="quote-prep-brief"
                                        data-scenario="opening-protection"
                                        data-improvement-type="opening-protection"
                                        data-detail="briefId=${page.brief().briefId()};surface=result;target=brief-url">Copy Brief Link</button>
                                <a class="secondary-button" href="${page.publicBriefPath()}">Open Shareable Brief</a>
                            </div>
                        </div>
                        <div class="packet-list-card quote-prep-share-card quote-prep-share-card--message">
                            <label class="console-field">
                                <span>Message you can send with the brief</span>
                                <textarea class="packet-output packet-output--compact" data-brief-outbound-message="true" readonly>${page.outboundMessage()}</textarea>
                            </label>
                            <div class="action-row packet-tool-actions">
                                <button class="secondary-button"
                                        type="button"
                                        data-copy-target="brief-outbound-message"
                                        data-track-event="quote_prep_message_copy"
                                        data-route-path="${page.meta().canonicalUrl().replace(page.meta().siteBaseUrl(), "")}"
                                        data-route-family="quote-prep-brief"
                                        data-scenario="opening-protection"
                                        data-improvement-type="opening-protection"
                                        data-detail="briefId=${page.brief().briefId()};surface=result;target=send-note">Copy Message</button>
                                <a class="secondary-button" href="${page.pdfExportPath()}">Open PDF Export</a>
                            </div>
                        </div>
                    </div>
                    <div>
                        <div class="section-heading">
                            <p class="eyebrow">Reply target</p>
                            <h2>What a useful contractor reply should answer</h2>
                            <p>Use these 3 points to judge whether the return stayed narrow enough to move forward.</p>
                        </div>
                        <div class="packet-list-card quote-prep-reply-card">
                            <ol class="packet-list">
                                @for(var item : page.narrative().homeownerReplyChecklist())
                                    <li>${item}</li>
                                @endfor
                            </ol>
                            <p>${page.narrative().homeownerReplyExample()}</p>
                        </div>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell packet-section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">Share signals</p>
                        <h2>Watch the first public response before you add anything else</h2>
                        <p>Do not widen this into more workflow until the public brief is actually getting used.</p>
                    </div>
                    <div class="card-grid quote-prep-signal-grid">
                        <article class="detail-card detail-card--quiet">
                            <p class="micro-tag">Message copies</p>
                            <p class="big-answer" data-signal-counter="send-note-copy">${page.metrics().sendNoteCopyCount()}</p>
                        </article>
                        <article class="detail-card detail-card--quiet">
                            <p class="micro-tag">Brief link copies</p>
                            <p class="big-answer" data-signal-counter="brief-link-copy">${page.metrics().briefCopyCount()}</p>
                        </article>
                        <article class="detail-card detail-card--quiet">
                            <p class="micro-tag">Public brief opens</p>
                            <p class="big-answer" data-signal-counter="public-brief-open">${page.metrics().publicBriefOpenCount()}</p>
                        </article>
                    </div>
                    <div class="packet-list-card packet-list-card--warning">
                        <p class="micro-tag">What to watch next</p>
                        @if(replySignalsLogged)
                            <p>A return-quality signal is now logged. Check whether the scope is actually narrower enough for estimator review and whether the report page plus opening photos are now covered.</p>
                        @elseif(publicBriefOpened)
                            <p>The brief has opened. The next signal is whether the reply comes back narrower, with the right report page, named openings, and only the relevant opening photos still in play.</p>
                        @elseif(sendPrepStarted)
                            <p>Outward share prep happened, but there is still no public brief open. Send or resend the link before you widen the workflow.</p>
                        @else
                            <p>No first-share signal yet. Copy the share note, send the brief, and wait for the first public brief open before you worry about anything downstream.</p>
                        @endif
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell packet-section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">Return quality signals</p>
                        <h2>Log what came back after sharing</h2>
                        <p>Use one click per real reply so you can tell whether the public brief is improving return quality before estimator time gets spent.</p>
                    </div>
                    <div class="card-grid quote-prep-signal-grid">
                        <article class="detail-card detail-card--quiet">
                            <p class="micro-tag">Narrower replies</p>
                            <p class="big-answer" data-signal-counter="reply-narrowed">${page.metrics().replyNarrowedCount()}</p>
                        </article>
                        <article class="detail-card detail-card--quiet">
                            <p class="micro-tag">Report pages returned</p>
                            <p class="big-answer" data-signal-counter="reply-report-page">${page.metrics().replyReportPageCount()}</p>
                        </article>
                        <article class="detail-card detail-card--quiet">
                            <p class="micro-tag">Opening photos returned</p>
                            <p class="big-answer" data-signal-counter="reply-photos">${page.metrics().replyPhotoCount()}</p>
                        </article>
                    </div>
                    <div class="packet-list-card">
                        <p class="micro-tag">Mark reply outcome</p>
                        <div class="action-row packet-tool-actions">
                            <button class="secondary-button"
                                    type="button"
                                    data-track-event="quote_prep_reply_narrowed"
                                    data-route-path="${page.meta().canonicalUrl().replace(page.meta().siteBaseUrl(), "")}"
                                    data-route-family="quote-prep-brief"
                                    data-scenario="opening-protection"
                                    data-improvement-type="opening-protection"
                                    data-detail="briefId=${page.brief().briefId()};surface=result;signal=reply-narrowed"
                                    data-signal-increment="reply-narrowed"
                                    data-signal-status-message="Narrower response logged for this brief.">Mark Narrower Response</button>
                            <button class="secondary-button"
                                    type="button"
                                    data-track-event="quote_prep_reply_report_page"
                                    data-route-path="${page.meta().canonicalUrl().replace(page.meta().siteBaseUrl(), "")}"
                                    data-route-family="quote-prep-brief"
                                    data-scenario="opening-protection"
                                    data-improvement-type="opening-protection"
                                    data-detail="briefId=${page.brief().briefId()};surface=result;signal=report-page"
                                    data-signal-increment="reply-report-page"
                                    data-signal-status-message="Report-page signal logged for this brief.">Mark Report Page Signal</button>
                            <button class="secondary-button"
                                    type="button"
                                    data-track-event="quote_prep_reply_photos"
                                    data-route-path="${page.meta().canonicalUrl().replace(page.meta().siteBaseUrl(), "")}"
                                    data-route-family="quote-prep-brief"
                                    data-scenario="opening-protection"
                                    data-improvement-type="opening-protection"
                                    data-detail="briefId=${page.brief().briefId()};surface=result;signal=opening-photos"
                                    data-signal-increment="reply-photos"
                                    data-signal-status-message="Opening-photo signal logged for this brief.">Mark Opening Photo Signal</button>
                        </div>
                        <p class="console-field-note" data-signal-status="true">${replySignalsLogged ? "Return-quality signal already logged for this brief. Keep checking whether all three pieces are now covered." : "No return-quality signal logged yet for this brief."}</p>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell packet-section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">After the first reply</p>
                        <h2>Keep the next move narrow</h2>
                        <p>Once the reply comes back narrower, any contractor-side follow-up can stay behind the scenes. Keep internal documents off this first share.</p>
                    </div>
                    <div class="packet-list-card">
                        <p class="micro-tag">Next move</p>
                        <p>${clarificationState ? "Review the narrower reply first, then decide whether the case is finally tight enough for estimator review." : blockedState ? "Fill the missing anchors first, then create the brief again." : "Review the narrower reply first, then decide whether any contractor-side follow-up is justified."}</p>
                    </div>
                    <div class="packet-list-card packet-list-card--warning">
                        <p class="micro-tag">Keep the free layer small</p>
                        <ul class="bullet-list danger-list">
                            <li>Do not jump into estimator scheduling before the public brief comes back clean.</li>
                            <li>Do not send internal notes or worksheets before the outward brief is readable on its own.</li>
                            <li>Do not add more documents just because they exist. Keep the free layer to the brief, the link, and the reply target.</li>
                        </ul>
                    </div>
                </div>
            </section>
        </main>
    `)
