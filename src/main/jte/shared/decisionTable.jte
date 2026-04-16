@import owner.mysafefloridahome.data.DecisionTable
@param DecisionTable table
<section class="section">
    <div class="section-shell">
        <div class="section-heading">
            <p class="eyebrow">${table.eyebrow()}</p>
            <h2>${table.title()}</h2>
            @if(table.intro() != null && !table.intro().isBlank())
                <p>${table.intro()}</p>
            @endif
        </div>
        <div class="table-wrap decision-table-wrap">
            <table class="decision-table">
                <thead>
                    <tr>
                    @for(var column : table.columns())
                        <th scope="col">${column}</th>
                    @endfor
                    </tr>
                </thead>
                <tbody>
                @for(var row : table.rows())
                    <tr>
                        <td><strong>${row.first()}</strong></td>
                        <td>${row.second()}</td>
                        <td>${row.third()}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>
    </div>
</section>
