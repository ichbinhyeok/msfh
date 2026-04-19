@import owner.mysafefloridahome.pages.VendorPresetPageView
@param VendorPresetPageView page
@template.layout.page(
    meta = page.meta(),
    siteName = "My Safe Florida Home Verdict",
    currentPath = "/vendor-packets/",
    bodyClass = "vendor-packet-page",
    content = @`
        <main>
            <section class="page-hero packet-hero">
                <div class="section-shell packet-hero-shell">
                    <div class="route-hero-main">
                        @template.shared.breadcrumbs(page.meta())
                        <p class="eyebrow">${page.eyebrow()}</p>
                        <h1>${page.title()}</h1>
                        <p class="lead">${page.description()}</p>
                        <div class="action-row">
                            <a class="primary-button" href="${page.packetBuilderPath()}">Open Quote-Prep Brief Builder</a>
                            <a class="secondary-button" href="${page.vendorEntryPath()}">Back to Quote-Prep Brief</a>
                        </div>
                    </div>
                    <aside class="route-hero-panel">
                        <p class="micro-tag">Optional after repeat sends</p>
                        <h2>Only after the brief repeats</h2>
                        <p class="route-hero-panel__summary">Save sender wording only if the same office keeps sending the public pre-quote brief and wants faster first-send setup.</p>
                    </aside>
                </div>
            </section>

            @if(!page.statusMessage().isBlank())
                <section class="section">
                    <div class="section-shell">
                        <div class="flash success">${page.statusMessage()}</div>
                    </div>
                </section>
            @endif

            <section class="section">
                <div class="section-shell packet-section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">Optional internal reuse</p>
                        <h2>Save office wording only if the first send is already working</h2>
                        <p>Keep the office label and repeated reply wording in one reusable preset so the pre-quote brief can relaunch without retyping it. This page should stay behind the public product.</p>
                    </div>
                    <div class="packet-tool-grid">
                        <form class="packet-tool-form" data-vendor-preset-form="true" method="post" action="${page.savePresetPath()}">
                            <input name="presetId" type="hidden" value="${page.form().presetId()}">
                            <label class="console-field">
                                <span>Preset name</span>
                                <input name="presetName" type="text" value="${page.form().presetName()}" placeholder="Palm Coast default">
                            </label>
                            <label class="console-field">
                                <span>Office label</span>
                                <input name="officeLabel" type="text" value="${page.form().officeLabel()}" placeholder="Palm Coast Openings Desk">
                            </label>
                            <label class="console-field">
                                <span>Sender name</span>
                                <input name="senderName" type="text" value="${page.form().senderName()}" placeholder="Amanda from intake">
                            </label>
                            <label class="console-field">
                                <span>Reply instructions</span>
                                <textarea name="replyInstructions" rows="3" placeholder="Reply with the report page and opening photos in one email before scheduling.">${page.form().replyInstructions()}</textarea>
                            </label>
                            <label class="console-field">
                                <span>Service area note</span>
                                <input name="serviceAreaNote" type="text" value="${page.form().serviceAreaNote()}" placeholder="Serving Miami-Dade and Broward only.">
                            </label>
                            <label class="console-field">
                                <span>Permit and inspection default</span>
                                <textarea name="permitHandlingNote" rows="3">${page.form().permitHandlingNote()}</textarea>
                            </label>
                            <label class="console-field">
                                <span>Attached-home caution</span>
                                <textarea name="attachedScopeNote" rows="3" placeholder="Attached-home jobs stay inside the narrower attached-home path until the scope is restated clearly.">${page.form().attachedScopeNote()}</textarea>
                            </label>
                            <label class="console-field">
                                <span>Boundary reminder</span>
                                <textarea name="boundaryScopeNote" rows="3" placeholder="Anything not named in the packet stays outside the first quote.">${page.form().boundaryScopeNote()}</textarea>
                            </label>
                            <div class="action-row packet-tool-actions">
                                <button class="primary-button" type="submit">${page.form().presetId().isBlank() ? "Save Office Preset" : "Update Office Preset"}</button>
                                @if(!page.form().presetId().isBlank())
                                    <a class="secondary-button" href="${page.meta().canonicalUrl().replace(page.meta().siteBaseUrl(), "")}">Start Fresh</a>
                                    <button class="secondary-button" type="submit" formaction="${page.deletePresetPath()}">Delete Preset</button>
                                @endif
                            </div>
                        </form>
                        <div class="packet-tool-output">
                            <div class="packet-preview-sheet">
                                <p class="micro-tag">Optional setup preview</p>
                                <h3 data-preset-preview-title="true">Opening protection quote-prep brief setup</h3>
                                <p data-preset-preview-summary="true">Use one reusable setup only if the next public brief should start with the same sender wording and reply rules.</p>
                                <ul class="packet-preview-sheet__list" data-preset-preview-list="true"></ul>
                            </div>
                            <div class="packet-list-card">
                                <p class="micro-tag">Pre-quote brief launch URL</p>
                                <textarea class="packet-output" data-preset-packet-url="true" readonly></textarea>
                                <div class="action-row packet-tool-actions">
                                    <button class="secondary-button" type="button" data-copy-target="preset-packet-url">Copy Brief Setup URL</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <section class="section">
                <div class="section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">Saved office wording</p>
                        <h2>Load reusable office wording back into the quote-prep brief</h2>
                        <p>Load a saved preset back into the quote-prep brief builder. Keep estimator and quote-boundary work behind the first reply.</p>
                    </div>
                    @if(page.savedPresets().isEmpty())
                        <div class="packet-list-card">
                            <p>No office setup is saved yet. Save one only if the same public brief wording keeps repeating.</p>
                        </div>
                    @else
                        <div class="route-detail-grid route-detail-grid--related">
                            @for(var preset : page.savedPresets())
                                <div class="route-detail-card">
                                    <p class="micro-tag">${preset.loaded() ? "Loaded office preset" : "Saved office preset"}</p>
                                    <h3>${preset.presetName()}</h3>
                                    <p>${preset.officeLabel()}${preset.senderName().isBlank() ? "" : " | " + preset.senderName()}</p>
                                    <p>Saved ${preset.createdOn()}</p>
                                    <div class="action-row">
                                        <a class="secondary-button" href="${preset.loadPath()}">${preset.loaded() ? "Editing Now" : "Edit Preset"}</a>
                                        <a class="secondary-button" href="${preset.packetBuilderPath()}">Open Quote-Prep Brief</a>
                                    </div>
                                    <form method="post" action="${page.deletePresetPath()}">
                                        <input name="presetId" type="hidden" value="${preset.presetId()}">
                                        <button class="secondary-button" type="submit">Delete Preset</button>
                                    </form>
                                </div>
                            @endfor
                        </div>
                    @endif
                </div>
            </section>

            <section class="section">
                <div class="section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">Recent public brief sends</p>
                        <h2>${page.recentHandoffsTitle()}</h2>
                        <p>${page.recentHandoffsDescription()}</p>
                    </div>
                    @if(page.recentHandoffs().isEmpty())
                        <div class="packet-list-card">
                            <p>No public brief has been created from this optional setup yet. Once the office sends one, come back here and watch the first-send signal before you tune anything else.</p>
                        </div>
                    @else
                        <div class="route-detail-grid route-detail-grid--related">
                            @for(var handoff : page.recentHandoffs())
                                <div class="route-detail-card">
                                    <p class="micro-tag">Recent first send</p>
                                    <h3>${handoff.siteLabel()}</h3>
                                    <p>${handoff.officeLabel().isBlank() ? "Office wording not saved on this handoff" : handoff.officeLabel()}</p>
                                    <p>Created ${handoff.createdOn()}</p>
                                    <p>${handoff.nextAction()}</p>
                                    <div class="packet-activity-stats">
                                        <div class="packet-activity-stat">
                                            <span>Public opens</span>
                                            <strong>${handoff.publicOpens()}</strong>
                                        </div>
                                        <div class="packet-activity-stat">
                                            <span>Link copies</span>
                                            <strong>${handoff.linkCopies()}</strong>
                                        </div>
                                    </div>
                                    <p>${handoff.statusLine()}</p>
                                    <div class="action-row">
                                        <a class="secondary-button" href="${handoff.resultPath()}">Result Console</a>
                                        <a class="secondary-button" href="${handoff.publicBriefPath()}">Public Packet</a>
                                    </div>
                                </div>
                            @endfor
                        </div>
                    @endif
                </div>
            </section>

            <section class="section">
                <div class="section-shell">
                    <div class="section-heading">
                        <p class="eyebrow">Guardrail</p>
                        <h2>Do not let setup become the visible product</h2>
                        <p>Preset reuse is internal convenience only. The homeowner-facing wedge is still one clear quote-prep brief.</p>
                    </div>
                    <div class="packet-list-card packet-list-card--warning">
                        <ul class="bullet-list danger-list">
                            <li>Do not lead with preset management before the first-send brief is being used.</li>
                            <li>Do not expose estimator or quote-boundary launch surfaces from this page.</li>
                            <li>Do not turn repeat internal wording into a public-facing suite.</li>
                        </ul>
                    </div>
                </div>
            </section>
        </main>
    `)
