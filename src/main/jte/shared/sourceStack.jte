@import java.util.List
@import owner.mysafefloridahome.data.SourceRecord
@param List<SourceRecord> sources
@param String reviewedOn = null
<section class="section source-stack">
    <div class="section-shell">
        <div class="two-column">
            <div class="info-panel emphasis">
                <p class="eyebrow">Virtual editorial team</p>
                <h2>Three internal review lanes check this page</h2>
                <ul class="bullet-list">
                    <li><strong>Rule freshness desk:</strong> current official pages outrank stale PDFs.</li>
                    <li><strong>Route clarity desk:</strong> one homeowner question should map to one owner page.</li>
                    <li><strong>Homeowner risk desk:</strong> quote shopping slows down when scope or eligibility is still unclear.</li>
                </ul>
            </div>
            <div class="info-panel">
                <p class="eyebrow">Transparency</p>
                <h3>Internal framework, not licensed reviewers</h3>
                <p>This virtual team is an internal editorial QA system. It is not the program, not a government office, and not legal, insurance, or contractor advice.</p>
                @if(reviewedOn != null)
                    <p class="meta-line">Latest page review signal: ${reviewedOn}</p>
                @endif
            </div>
        </div>
        <div class="section-heading">
            <p class="eyebrow">Official source stack</p>
            <h2>Current sources behind this page</h2>
        </div>
        <div class="source-list">
        @for(var source : sources)
            !{var verifiedOn = source.verifiedOn().toString();}
            !{var nextReviewOn = source.nextReviewOn().toString();}
            <article class="source-item">
                <h3><a href="${source.url()}" target="_blank" rel="noopener noreferrer">${source.title()}</a></h3>
                <p>${source.note()}</p>
                <p class="meta-line">Verified ${verifiedOn} - Next scheduled review ${nextReviewOn}</p>
            </article>
        @endfor
        </div>
    </div>
</section>
