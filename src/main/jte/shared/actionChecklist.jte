@import owner.mysafefloridahome.data.ActionChecklist
@param ActionChecklist checklist
<section class="section">
    <div class="section-shell">
        <div class="section-heading">
            <p class="eyebrow">${checklist.eyebrow()}</p>
            <h2>${checklist.title()}</h2>
            @if(checklist.intro() != null && !checklist.intro().isBlank())
                <p>${checklist.intro()}</p>
            @endif
        </div>
        <div class="checklist-grid">
        @for(int i = 0; i < checklist.items().size(); ++i)
            <article class="checklist-item">
                <p class="card-tag">Check ${i + 1}</p>
                <p>${checklist.items().get(i)}</p>
            </article>
        @endfor
        </div>
    </div>
</section>
