@import owner.mysafefloridahome.pages.PageMeta
@param PageMeta meta
<nav class="breadcrumbs" aria-label="Breadcrumb">
@for(var crumb : meta.breadcrumbs())
    @if(crumb.href() != null)
        <a href="${crumb.href()}">${crumb.label()}</a>
    @else
        <span>${crumb.label()}</span>
    @endif
@endfor
</nav>
