@param String partnerStatus = null
<section class="section lead-section" id="partner-inquiry">
    <div class="section-shell two-column partner-section">
        <div class="info-panel emphasis partner-panel">
            <p class="eyebrow">Partner pilot</p>
            <p class="big-answer">Use this lane for a paid pilot conversation tied to one improvement route, not for broad directory placement.</p>
            <ul class="bullet-list">
                <li>Licensed Florida contractors only.</li>
                <li>County coverage, route focus, and license details come first.</li>
                <li>No promised volume, no approval language, and no generic reroof lead packaging.</li>
            </ul>
        </div>
        <div>
            <div class="section-heading">
                <p class="eyebrow">Commercial inquiry</p>
                <h2>Request a route-specific pilot review</h2>
                <p>Best fit for contractors who can stay disciplined around one recommendation type and one county footprint.</p>
                <p class="meta-line">Paid pilot reviews start at $390 for a 30-day test. This inbox is not an official program channel and it does not guarantee placement or volume.</p>
            </div>
            <div class="partner-offer-grid">
                <article class="partner-offer-card">
                    <p class="micro-tag">Starting pilot</p>
                    <h3>$390 for 30 days</h3>
                    <p>One route focus, one narrow decision surface, and a live review window before any broader expansion.</p>
                </article>
                <article class="partner-offer-card">
                    <p class="micro-tag">Coverage</p>
                    <h3>Up to 3 counties</h3>
                    <p>Start only where your crew can actually service work without widening the homeowner recommendation.</p>
                </article>
                <article class="partner-offer-card">
                    <p class="micro-tag">Acceptance rule</p>
                    <h3>Scope discipline first</h3>
                    <p>Good fit if your team can explain what you handle, what you do not handle, and why the route is still narrow.</p>
                </article>
            </div>
            @if("success".equals(partnerStatus))
                <div class="flash success">The pilot inquiry was saved. Review will stay tied to route fit, county coverage, and license clarity.</div>
            @elseif("error".equals(partnerStatus))
                <div class="flash error">The inquiry is missing a required field. Review the inputs and submit again.</div>
            @endif
            <form class="lead-form partner-form" method="post" action="/api/contact/capture" data-partner-form="true" data-route-path="/contact/" data-route-family="trust" data-scenario="partner_pilot" data-improvement-type="">
                <input type="hidden" name="originPath" value="/contact/">
                <input type="hidden" name="inquiryType" value="partner_pilot">
                <div class="field-trap" aria-hidden="true">
                    <label>
                        Website
                        <input type="text" name="website" tabindex="-1" autocomplete="off">
                    </label>
                </div>
                <label>
                    Route focus
                    <select name="routeFocus" required>
                        <option value="opening-protection">Opening protection</option>
                        <option value="roof-to-wall">Roof-to-wall retrofit</option>
                        <option value="roof-deck-attachment">Roof deck attachment</option>
                        <option value="secondary-water-resistance">Secondary water resistance</option>
                        <option value="roof-replacement-under-swr">Roof replacement under SWR</option>
                        <option value="general-quote-route">General quote route</option>
                    </select>
                </label>
                <label>
                    Contact name
                    <input type="text" name="contactName" required placeholder="Jordan Lee">
                </label>
                <label>
                    Company
                    <input type="text" name="company" required placeholder="Coastal Retrofit Group">
                </label>
                <label>
                    Email
                    <input type="email" name="email" required placeholder="hello@example.com">
                </label>
                <label>
                    Phone
                    <input type="tel" name="phone" placeholder="Optional">
                </label>
                <label>
                    License number
                    <input type="text" name="licenseNumber" required placeholder="CCC1333333">
                </label>
                <label class="field-span-two">
                    Counties served
                    <input type="text" name="countiesServed" required placeholder="Miami-Dade, Broward, Palm Beach">
                </label>
                <label class="field-span-two">
                    Why this route fits your team
                    <textarea name="message" required rows="5" placeholder="Explain the route you want to sponsor, the scope you actually handle, and how your team avoids widening the recommendation."></textarea>
                </label>
                <label class="consent-row">
                    <input type="checkbox" name="consent" value="true" required>
                    <span>I understand this is an independent editorial product, not the program, and this inquiry only requests a paid pilot review.</span>
                </label>
                <button class="primary-button" type="submit">Request pilot review</button>
            </form>
        </div>
    </div>
</section>
