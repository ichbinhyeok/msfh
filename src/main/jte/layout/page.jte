@import gg.jte.Content
@import owner.mysafefloridahome.pages.PageMeta
@param PageMeta meta
@param String siteName
@param String currentPath
@param Content content
@param String bodyClass = ""
!{var programActive = currentPath.startsWith("/program/");}
!{var improvementActive = currentPath.startsWith("/improvements/");}
!{var guideActive = currentPath.startsWith("/guides/");}
!{var trustActive = currentPath.startsWith("/about/") || currentPath.startsWith("/methodology/") || currentPath.startsWith("/contact/") || currentPath.startsWith("/not-government-affiliated/") || currentPath.startsWith("/privacy/") || currentPath.startsWith("/terms/");}
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${meta.title()}</title>
    <meta name="description" content="${meta.description()}">
    <meta name="robots" content="${meta.robots()}">
    <link rel="canonical" href="${meta.canonicalUrl()}">
    <link rel="icon" href="/favicon.svg" type="image/svg+xml">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&family=Manrope:wght@600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/site.css">
    <script async src="https://www.googletagmanager.com/gtag/js?id=G-1C3Q631V0G"></script>
    <script>
        window.dataLayer = window.dataLayer || [];
        function gtag() {dataLayer.push(arguments);}
        gtag('js', new Date());

        gtag('config', 'G-1C3Q631V0G', { send_page_view: false });
    </script>
    <script defer src="/site.js"></script>
    <script defer src="/analytics.js"></script>
</head>
<body class="${bodyClass}">
    <header class="site-nav">
        <div class="nav-shell">
            <a class="brand" href="/">My Safe Florida Home Verdict</a>
            <nav class="nav-links" aria-label="Primary">
                <a class="${"/".equals(currentPath) ? "is-active" : ""}" href="/">Home</a>
                <a class="${programActive ? "is-active" : ""}" href="/program/">Program</a>
                <a class="${improvementActive ? "is-active" : ""}" href="/improvements/">Improvements</a>
                <a class="${guideActive ? "is-active" : ""}" href="/guides/">Guides</a>
                <a class="${trustActive ? "is-active" : ""}" href="/methodology/">Methodology</a>
            </nav>
            <a class="nav-utility" href="/#decision-tool">Open Tool</a>
        </div>
    </header>
    ${content}
    <footer class="site-footer">
        <div class="footer-shell">
            <div>
                <p class="footer-title">${siteName}</p>
                <p class="footer-copy">Florida-only decision support for the report stage, project choice, and quote preparation.</p>
            </div>
            <div class="footer-links">
                <a href="/about/">About</a>
                <a href="/methodology/">Methodology</a>
                <a href="/contact/">Contact</a>
                <a href="/not-government-affiliated/">Not government affiliated</a>
                <a href="/privacy/">Privacy</a>
                <a href="/terms/">Terms</a>
            </div>
        </div>
    </footer>
</body>
</html>
