@param String currentStage = ""
@param String title = "Interpret the recommendation, then choose the next move"
@param String intro = "Move in order: understand what the recommendation means, choose the first project, then prepare the quote path."
@param boolean showCloseout = false
!{var interpretTag = "interpret".equals(currentStage) ? "Current focus" : "Step 1";}
!{var chooseTag = "choose".equals(currentStage) ? "Current focus" : "Step 2";}
!{var quoteTag = "quote".equals(currentStage) ? "Current focus" : "Step 3";}
!{var closeoutTag = "closeout".equals(currentStage) ? "Current focus" : "Step 4";}
<section class="section">
    <div class="section-shell">
        <div class="section-heading">
            <p class="eyebrow">Decision engine</p>
            <h2>${title}</h2>
            <p>${intro}</p>
        </div>
        <div class="card-grid decision-grid">
            <article class="route-card decision-card">
                <p class="card-tag">${interpretTag}</p>
                <h3>Interpret the recommendation</h3>
                <p>Read what the report or route actually means before you treat it like a project brief.</p>
            </article>
            <article class="route-card decision-card">
                <p class="card-tag">${chooseTag}</p>
                <h3>Choose the first project</h3>
                <p>Keep scope tight and decide which improvement deserves the first contractor conversation.</p>
            </article>
            <article class="route-card decision-card">
                <p class="card-tag">${quoteTag}</p>
                <h3>Prepare the quote path</h3>
                <p>Use the checklist and contractor type only after the recommendation and scope are clear.</p>
            </article>
            @if(showCloseout)
                <article class="route-card decision-card">
                    <p class="card-tag">${closeoutTag}</p>
                    <h3>Close the file cleanly</h3>
                    <p>After the work is done, make sure permits, inspections, and paperwork are really ready before you expect the draw request.</p>
                </article>
            @endif
        </div>
    </div>
</section>
