package owner.mysafefloridahome.web;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import owner.mysafefloridahome.AppProperties;
import owner.mysafefloridahome.pages.BreadcrumbItem;
import owner.mysafefloridahome.pages.PageMeta;
import owner.mysafefloridahome.pages.VendorHandoffDocumentPageView;
import owner.mysafefloridahome.pages.VendorHandoffResultPageView;
import owner.mysafefloridahome.vendor.OpeningProtectionHandoffNarrative;
import owner.mysafefloridahome.vendor.OpeningProtectionHandoffRecord;
import owner.mysafefloridahome.vendor.OpeningProtectionHandoffRequest;
import owner.mysafefloridahome.vendor.OpeningProtectionHandoffService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class VendorHandoffController {

    private final OpeningProtectionHandoffService handoffService;
    private final AppProperties appProperties;

    public VendorHandoffController(OpeningProtectionHandoffService handoffService, AppProperties appProperties) {
        this.handoffService = handoffService;
        this.appProperties = appProperties;
    }

    @PostMapping({"/tools/opening-protection/quote-prep-brief/create", "/vendor-handoffs/opening-protection"})
    public String createHandoff(OpeningProtectionHandoffRequest request) {
        List<String> blockingItems = handoffService.publicBriefBlockingItems(request);
        if (!blockingItems.isEmpty()) {
            return "redirect:" + preQuoteRedirect(request);
        }
        OpeningProtectionHandoffRecord handoff = handoffService.create(request);
        return "redirect:" + handoffService.resultPath(handoff.internalToken());
    }

    @GetMapping({"/tools/opening-protection/quote-prep-brief/result/{internalToken}/", "/vendor-handoffs/opening-protection/{internalToken}/"})
    public String handoffResult(@PathVariable String internalToken, HttpServletRequest request, Model model) {
        OpeningProtectionHandoffRecord handoff = handoffService.findByInternalToken(internalToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        OpeningProtectionHandoffNarrative narrative = handoffService.narrative(handoff);
        String resultPath = handoffService.resultPath(handoff.internalToken());
        String publicBriefPath = handoffService.publicBriefPath(handoff.publicToken());
        String printablePacketPath = handoffService.publicBriefPdfExportPath(handoff.publicToken());
        String shareUrl = handoffService.absoluteUrl(publicBriefPath, baseUrl(request));
        String outboundMessage = handoffService.outboundMessage(handoff, shareUrl);
        model.addAttribute("page", new VendorHandoffResultPageView(
                meta(
                        "Opening Protection Quote-Prep Brief Ready",
                        "Result console for a shareable opening-protection quote-prep brief.",
                        resultPath,
                        request),
                handoff,
                narrative,
                handoffService.metricsFor(handoff.handoffId()),
                shareUrl,
                publicBriefPath,
                "",
                "",
                "",
                "",
                List.of(),
                List.of(),
                printablePacketPath,
                "",
                "",
                "",
                outboundMessage,
                "",
                ""));
        return "vendorHandoffResult";
    }

    @GetMapping({"/tools/opening-protection/quote-prep-brief/share/{publicToken}/", "/vendor-handoffs/opening-protection/brief/{publicToken}/"})
    public String publicBrief(@PathVariable String publicToken, HttpServletRequest request, Model model) {
        OpeningProtectionHandoffRecord handoff = handoffService.findByPublicToken(publicToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        String publicBriefPath = handoffService.publicBriefPath(handoff.publicToken());
        model.addAttribute("page", new VendorHandoffDocumentPageView(
                meta(
                        "Opening Protection Quote-Prep Brief",
                        "Shareable opening-protection brief created for quote-prep scope narrowing.",
                        publicBriefPath,
                        request),
                handoff,
                handoffService.narrative(handoff),
                "",
                "",
                "",
                "",
                "",
                List.of(),
                "",
                "",
                publicBriefPath,
                handoffService.publicBriefPdfExportPath(handoff.publicToken()),
                handoffService.replyGuidance(handoff)));
        return "vendorHandoffBrief";
    }

    @GetMapping({"/tools/opening-protection/quote-prep-brief/share/{publicToken}/export/pdf/", "/vendor-handoffs/opening-protection/brief/{publicToken}/export/pdf/"})
    public String publicBriefPdfExport(@PathVariable String publicToken, HttpServletRequest request, Model model) {
        OpeningProtectionHandoffRecord handoff = handoffService.findByPublicToken(publicToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        String publicBriefPath = handoffService.publicBriefPath(handoff.publicToken());
        String pdfExportPath = handoffService.publicBriefPdfExportPath(handoff.publicToken());
        model.addAttribute("page", new VendorHandoffDocumentPageView(
                meta(
                        "Opening Protection Quote-Prep Brief PDF Export",
                        "PDF-first export view for the opening-protection quote-prep brief.",
                        pdfExportPath,
                        request),
                handoff,
                handoffService.narrative(handoff),
                "",
                "",
                "",
                "",
                "",
                List.of(),
                "",
                "",
                publicBriefPath,
                pdfExportPath,
                handoffService.replyGuidance(handoff)));
        return "vendorHandoffBriefPdf";
    }

    @GetMapping({"/tools/opening-protection/quote-prep-brief/internal/{internalToken}/", "/vendor-handoffs/opening-protection/record/{internalToken}/"})
    public String officeRecord(@PathVariable String internalToken, HttpServletRequest request, Model model) {
        OpeningProtectionHandoffRecord handoff = handoffService.findByInternalToken(internalToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("page", new VendorHandoffDocumentPageView(
                meta(
                        "Opening Protection Office Record",
                        "Internal record that stays behind the shareable quote-prep brief in the opening-protection workflow.",
                        handoffService.officeRecordPath(handoff.internalToken()),
                        request),
                handoff,
                handoffService.narrative(handoff),
                handoffService.resultPath(handoff.internalToken()),
                handoffService.absoluteUrl(handoffService.publicBriefPath(handoff.publicToken()), baseUrl(request)),
                handoffService.workflowEntryPath(),
                handoffService.improvementGuidePath(),
                handoffService.quoteChecklistPath(),
                List.of(),
                handoffService.prefilledEstimatorSheetPath(handoff),
                handoffService.prefilledQuoteBoundaryPath(handoff),
                handoffService.publicBriefPath(handoff.publicToken()),
                handoffService.publicBriefPdfExportPath(handoff.publicToken()),
                handoffService.replyGuidance(handoff)));
        return "vendorHandoffRecord";
    }

    private PageMeta meta(String title, String description, String path, HttpServletRequest request) {
        return new PageMeta(
                title + " | " + appProperties.getSiteName(),
                description,
                appProperties.absoluteUrl(path, baseUrl(request)),
                "noindex,nofollow",
                List.of(
                        new BreadcrumbItem("Home", "/"),
                        new BreadcrumbItem("Opening-protection quote-prep brief", handoffService.workflowEntryPath()),
                        new BreadcrumbItem(title, path)),
                appProperties.resolvedBaseUrl(baseUrl(request)),
                "");
    }

    private String preQuoteRedirect(OpeningProtectionHandoffRequest request) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(handoffService.packetBuilderPath())
                .queryParam("blockingError", "missing_required_scope");
        appendIfNotBlank(builder, "siteLabel", request.getSiteLabel());
        appendIfNotBlank(builder, "countyZip", request.getCountyZip());
        appendIfNotBlank(builder, "homeType", request.getHomeType());
        appendIfNotBlank(builder, "scopeLane", request.getScopeLane());
        appendIfNotBlank(builder, "recommendationLine", request.getRecommendationLine());
        appendIfNotBlank(builder, "scopeOpenings", request.getScopeOpenings());
        appendIfNotBlank(builder, "officeLabel", request.getOfficeLabel());
        appendIfNotBlank(builder, "senderName", request.getSenderName());
        appendIfNotBlank(builder, "replyInstructions", request.getReplyInstructions());
        appendIfNotBlank(builder, "serviceAreaNote", request.getServiceAreaNote());
        appendIfNotBlank(builder, "permitHandlingNote", request.getPermitHandlingNote());
        appendIfNotBlank(builder, "attachedScopeNote", request.getAttachedScopeNote());
        appendIfNotBlank(builder, "boundaryScopeNote", request.getBoundaryScopeNote());
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
