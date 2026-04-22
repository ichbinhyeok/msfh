@import java.util.List
@import owner.mysafefloridahome.data.SourceRecord
@param List<SourceRecord> sources
@param String reviewedOn = null
<section class="section source-stack">
    <div class="section-shell">
        <div class="two-column">
            <div class="info-panel emphasis">
                <p class="eyebrow">Why this page is careful</p>
                <h2>Built from current official pages, then narrowed to one safer next step</h2>
                <ul class="bullet-list">
                    <li><strong>Fresh rules first:</strong> current official pages beat stale PDFs and old flyers.</li>
                    <li><strong>One page, one question:</strong> each page should solve one homeowner decision cleanly.</li>
                    <li><strong>Risk check:</strong> when scope is fuzzy, the page slows the decision down instead of pushing a sale.</li>
                </ul>
            </div>
            <div class="info-panel">
                <p class="eyebrow">What this page is not</p>
                <h3>Independent guidance, not official approval</h3>
                <p>This page is an independent guide. It is not the program, not a government agency, and not legal, insurance, or contractor advice.</p>
                @if(reviewedOn != null)
                    <p class="meta-line">Last reviewed against the source stack: ${reviewedOn}</p>
                @endif
            </div>
        </div>
        <div class="section-heading">
            <p class="eyebrow">Official source stack</p>
            <h2>Current official sources behind this page</h2>
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
