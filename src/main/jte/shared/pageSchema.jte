@import owner.mysafefloridahome.pages.PageMeta
@param PageMeta meta
@param String siteName
@param String about
@param String audience = "Florida homeowners using a My Safe Florida Home inspection report"
!{var siteBaseUrl = meta.siteBaseUrl() == null ? "" : meta.siteBaseUrl();}
!{var siteRoot = siteBaseUrl.isBlank() ? "/" : siteBaseUrl + "/";}
!{var methodologyUrl = siteBaseUrl.isBlank() ? "/methodology/" : siteBaseUrl + "/methodology/";}
!{var organizationId = siteRoot + "#organization";}
!{var websiteId = siteRoot + "#website";}
<script type="application/ld+json">
{
  "@context": "https://schema.org",
  "@type": "Organization",
  "@id": "${organizationId}",
  "name": "${siteName}",
  "url": "${siteRoot}",
  "description": "Independent editorial decision support for Florida homeowners after a My Safe Florida Home inspection report.",
  "publishingPrinciples": "${methodologyUrl}"
}
</script>
<script type="application/ld+json">
{
  "@context": "https://schema.org",
  "@type": "WebSite",
  "@id": "${websiteId}",
  "url": "${siteRoot}",
  "name": "${siteName}",
  "publisher": {
    "@id": "${organizationId}"
  }
}
</script>
<script type="application/ld+json">
{
  "@context": "https://schema.org",
  "@type": "WebPage",
  "@id": "${meta.canonicalUrl()}#webpage",
  "name": "${meta.title()}",
  "description": "${meta.description()}",
  "url": "${meta.canonicalUrl()}",
  "isPartOf": {
    "@id": "${websiteId}"
  },
  "publisher": {
    "@id": "${organizationId}"
  },@if(meta.reviewedOn() != null)
  "dateModified": "${meta.reviewedOn()}",
  @endif
  "about": "${about}",
  "audience": {
    "@type": "Audience",
    "audienceType": "${audience}"
  }
}
</script>
