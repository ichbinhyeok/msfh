package owner.mysafefloridahome.pages;

import owner.mysafefloridahome.vendor.OpeningProtectionHandoffNarrative;
import owner.mysafefloridahome.vendor.OpeningProtectionHandoffRecord;
import java.util.List;

public record VendorHandoffDocumentPageView(
        PageMeta meta,
        OpeningProtectionHandoffRecord handoff,
        OpeningProtectionHandoffNarrative narrative,
        String resultPath,
        String shareUrl,
        String workflowEntryPath,
        String improvementGuidePath,
        String quoteChecklistPath,
        List<RouteDetailCard> recoveryCards,
        String prefilledEstimatorSheetPath,
        String prefilledQuoteBoundaryPath,
        String publicBriefPath,
        String pdfExportPath,
        String replyGuidance) {
}
