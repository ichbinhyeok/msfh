@import owner.mysafefloridahome.pages.LeadFormContext
@param LeadFormContext leadForm
@param String leadStatus = null
<section class="section lead-section" id="lead-form">
    <div class="section-shell">
        <div class="section-heading">
            <p class="eyebrow">Next action</p>
            <h2>${leadForm.primaryCtaLabel()}</h2>
            <p>Stay email-first. Use this to lock the next decision and contractor path before generic quote shopping takes over.</p>
            <p class="meta-line">Independent route desk. The form stores limited route inputs and contact details to deliver or improve next-step guidance. Do not submit account credentials or official program documents. <a class="meta-link" href="/privacy/">Privacy</a> <a class="meta-link" href="/terms/">Terms</a></p>
        </div>
        @if("success".equals(leadStatus))
            <div class="flash success">The next-step route was saved with the recommendation context.</div>
        @elseif("error".equals(leadStatus))
            <div class="flash error">The form is missing a required field. Review the inputs and submit again.</div>
        @endif
        <form class="lead-form" method="post" action="/api/leads/capture" data-lead-form="true" data-route-path="${leadForm.routePath()}" data-route-family="${leadForm.routeFamily()}" data-scenario="${leadForm.scenario()}" data-improvement-type="${leadForm.improvementType()}">
            <input type="hidden" name="originPath" value="${leadForm.routePath()}">
            <input type="hidden" name="routePath" value="${leadForm.routePath()}">
            <input type="hidden" name="routeFamily" value="${leadForm.routeFamily()}">
            <input type="hidden" name="scenario" value="${leadForm.scenario()}">
            <input type="hidden" name="improvementType" value="${leadForm.improvementType()}">
            <div class="field-trap" aria-hidden="true">
                <label>
                    Website
                    <input type="text" name="website" tabindex="-1" autocomplete="off">
                </label>
            </div>
            <label>
                County
                <input type="text" name="county" required placeholder="Miami-Dade">
            </label>
            <label>
                ZIP
                <input type="text" name="zip" required placeholder="33101">
            </label>
            <label>
                Report received
                <select name="reportReceived" required>
                    <option value="yes">Yes</option>
                    <option value="almost">Almost</option>
                    <option value="no">Not yet</option>
                </select>
            </label>
            <label>
                Home type
                <select name="homeType" required>
                    <option value="detached">Detached</option>
                    <option value="attached">Attached or townhouse</option>
                </select>
            </label>
            <label>
                Budget range
                <select name="budgetRange" required>
                    <option value="under-5k">Under $5k</option>
                    <option value="5k-10k">$5k to $10k</option>
                    <option value="10k-20k">$10k to $20k</option>
                    <option value="20k-plus">$20k+</option>
                </select>
            </label>
            <label>
                Timeline
                <select name="timeline" required>
                    <option value="within-30-days">Within 30 days</option>
                    <option value="31-90-days">31 to 90 days</option>
                    <option value="later">Later</option>
                </select>
            </label>
            <label>
                Email
                <input type="email" name="email" required placeholder="you@example.com">
            </label>
            <label>
                Phone
                <input type="tel" name="phone" placeholder="Optional">
            </label>
            <label class="consent-row">
                <input type="checkbox" name="consent" value="true" required>
                <span>I understand this site is not the program and may use these details only for next-step guidance, route QA, and follow-up tied to this route.</span>
            </label>
            <button class="primary-button" type="submit">Save this next-step route</button>
        </form>
    </div>
</section>
