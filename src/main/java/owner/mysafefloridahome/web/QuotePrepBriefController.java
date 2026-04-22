package owner.mysafefloridahome.web;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import owner.mysafefloridahome.AppProperties;
import owner.mysafefloridahome.pages.BreadcrumbItem;
import owner.mysafefloridahome.pages.PageMeta;
import owner.mysafefloridahome.pages.QuotePrepBriefDocumentPageView;
import owner.mysafefloridahome.pages.QuotePrepBriefPageService;
import owner.mysafefloridahome.pages.QuotePrepBriefResultPageView;
import owner.mysafefloridahome.quoteprep.OpeningProtectionBriefNarrative;
import owner.mysafefloridahome.quoteprep.OpeningProtectionBriefRecord;
import owner.mysafefloridahome.quoteprep.OpeningProtectionBriefRequest;
import owner.mysafefloridahome.quoteprep.OpeningProtectionQuotePrepService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class QuotePrepBriefController {

    private final QuotePrepBriefPageService quotePrepBriefPageService;
    private final OpeningProtectionQuotePrepService quotePrepService;
    private final AppProperties appProperties;

    public QuotePrepBriefController(
            QuotePrepBriefPageService quotePrepBriefPageService,
            OpeningProtectionQuotePrepService quotePrepService,
            AppProperties appProperties) {
        this.quotePrepBriefPageService = quotePrepBriefPageService;
        this.quotePrepService = quotePrepService;
        this.appProperties = appProperties;
    }

    @GetMapping("/tools/opening-protection/quote-prep-brief/")
    public String openingProtectionWorkflowEntry(HttpServletRequest request, Model model) {
        model.addAttribute("page", quotePrepBriefPageService.openingProtectionWorkflowEntry(baseUrl(request)));
        return "quotePrepBriefEntry";
    }

    @GetMapping("/tools/opening-protection/quote-prep-brief/build/")
    public String openingProtectionPreQuote(
            @RequestParam(required = false) String blockingError,
            HttpServletRequest request,
            Model model) {
        model.addAttribute("page", quotePrepBriefPageService.openingProtectionPreQuote(
                baseUrl(request),
                blockingMessage(blockingError)));
        return "quotePrepBriefBuilder";
    }

    @PostMapping("/tools/opening-protection/quote-prep-brief/create")
    public String createBrief(OpeningProtectionBriefRequest request) {
        List<String> blockingItems = quotePrepService.publicBriefBlockingItems(request);
        if (!blockingItems.isEmpty()) {
            return "redirect:" + preQuoteRedirect(request);
        }
        OpeningProtectionBriefRecord brief = quotePrepService.create(request);
        return "redirect:" + quotePrepService.resultPath(brief.internalToken());
    }

    @GetMapping("/tools/opening-protection/quote-prep-brief/result/{internalToken}/")
    public String briefResult(@PathVariable String internalToken, HttpServletRequest request, Model model) {
        OpeningProtectionBriefRecord brief = quotePrepService.findByInternalToken(internalToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        OpeningProtectionBriefNarrative narrative = quotePrepService.narrative(brief);
        String resultPath = quotePrepService.resultPath(brief.internalToken());
        String publicBriefPath = quotePrepService.publicBriefPath(brief.publicToken());
        String pdfExportPath = quotePrepService.publicBriefPdfExportPath(brief.publicToken());
        String shareUrl = quotePrepService.absoluteUrl(publicBriefPath, baseUrl(request));
        String outboundMessage = quotePrepService.outboundMessage(brief, shareUrl);
        model.addAttribute("page", new QuotePrepBriefResultPageView(
                meta(
                        "Opening Protection Quote-Prep Brief Ready",
                        "Result console for a shareable opening-protection quote-prep brief.",
                        resultPath,
                        request),
                brief,
                narrative,
                quotePrepService.metricsFor(brief.briefId()),
                shareUrl,
                publicBriefPath,
                pdfExportPath,
                outboundMessage,
                quotePrepService.replyGuidance(brief)));
        return "quotePrepBriefResult";
    }

    @GetMapping("/tools/opening-protection/quote-prep-brief/share/{publicToken}/")
    public String publicBrief(@PathVariable String publicToken, HttpServletRequest request, Model model) {
        OpeningProtectionBriefRecord brief = quotePrepService.findByPublicToken(publicToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        String publicBriefPath = quotePrepService.publicBriefPath(brief.publicToken());
        model.addAttribute("page", new QuotePrepBriefDocumentPageView(
                meta(
                        "Opening Protection Quote-Prep Brief",
                        "Shareable opening-protection brief created for quote-prep scope narrowing.",
                        publicBriefPath,
                        request),
                brief,
                quotePrepService.narrative(brief),
                publicBriefPath,
                quotePrepService.publicBriefPdfExportPath(brief.publicToken()),
                quotePrepService.replyGuidance(brief)));
        return "quotePrepBriefPublic";
    }

    @GetMapping("/tools/opening-protection/quote-prep-brief/share/{publicToken}/export/pdf/")
    public String publicBriefPdfExport(@PathVariable String publicToken, HttpServletRequest request, Model model) {
        OpeningProtectionBriefRecord brief = quotePrepService.findByPublicToken(publicToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        String publicBriefPath = quotePrepService.publicBriefPath(brief.publicToken());
        String pdfExportPath = quotePrepService.publicBriefPdfExportPath(brief.publicToken());
        model.addAttribute("page", new QuotePrepBriefDocumentPageView(
                meta(
                        "Opening Protection Quote-Prep Brief PDF Export",
                        "PDF-first export view for the opening-protection quote-prep brief.",
                        pdfExportPath,
                        request),
                brief,
                quotePrepService.narrative(brief),
                publicBriefPath,
                pdfExportPath,
                quotePrepService.replyGuidance(brief)));
        return "quotePrepBriefPdf";
    }

    private String blockingMessage(String blockingError) {
        if (!"missing_required_scope".equals(blockingError)) {
            return "";
        }
        return "Cannot create the shareable brief yet. Confirm the report recommendation, home type, and first quote focus or exact openings first.";
    }

    private PageMeta meta(String title, String description, String path, HttpServletRequest request) {
        return new PageMeta(
                title + " | " + appProperties.getSiteName(),
                description,
                appProperties.absoluteUrl(path, baseUrl(request)),
                "noindex,nofollow",
                List.of(
                        new BreadcrumbItem("Home", "/"),
                        new BreadcrumbItem("Opening-protection quote-prep brief", quotePrepService.workflowEntryPath()),
                        new BreadcrumbItem(title, path)),
                appProperties.resolvedBaseUrl(baseUrl(request)),
                "");
    }

    private String preQuoteRedirect(OpeningProtectionBriefRequest request) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(quotePrepService.briefBuilderPath())
                .queryParam("blockingError", "missing_required_scope");
        appendIfNotBlank(builder, "siteLabel", request.getSiteLabel());
        appendIfNotBlank(builder, "countyZip", request.getCountyZip());
        appendIfNotBlank(builder, "homeType", request.getHomeType());
        appendIfNotBlank(builder, "scopeLane", request.getScopeLane());
        appendIfNotBlank(builder, "recommendationLine", request.getRecommendationLine());
        appendIfNotBlank(builder, "scopeOpenings", request.getScopeOpenings());
        appendIfTrue(builder, "reportPageReceived", request.isReportPageReceived());
        appendIfTrue(builder, "photosReceived", request.isPhotosReceived());
        appendIfTrue(builder, "broadPackageRequested", request.isBroadPackageRequested());
        appendIfTrue(builder, "compareQuotesRequested", request.isCompareQuotesRequested());
        appendIfTrue(builder, "reimbursementAssumed", request.isReimbursementAssumed());
        appendIfTrue(builder, "hoaReviewLikely", request.isHoaReviewLikely());
        return builder.build().encode().toUriString();
    }

    private void appendIfNotBlank(UriComponentsBuilder builder, String key, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        builder.queryParam(key, value);
    }

    private void appendIfTrue(UriComponentsBuilder builder, String key, boolean value) {
        if (value) {
            builder.queryParam(key, true);
        }
    }

    private String baseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        int port = request.getServerPort();
        boolean defaultPort = ("http".equalsIgnoreCase(scheme) && port == 80)
                || ("https".equalsIgnoreCase(scheme) && port == 443);
        return defaultPort
                ? scheme + "://" + request.getServerName()
                : scheme + "://" + request.getServerName() + ":" + port;
    }
}
