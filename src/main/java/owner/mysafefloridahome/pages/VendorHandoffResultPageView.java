package owner.mysafefloridahome.pages;

import java.util.List;
import owner.mysafefloridahome.vendor.OpeningProtectionHandoffMetrics;
import owner.mysafefloridahome.vendor.OpeningProtectionHandoffNarrative;
import owner.mysafefloridahome.vendor.OpeningProtectionHandoffRecord;

public record VendorHandoffResultPageView(
        PageMeta meta,
        OpeningProtectionHandoffRecord handoff,
        OpeningProtectionHandoffNarrative narrative,
        OpeningProtectionHandoffMetrics metrics,
        String shareUrl,
        String publicBriefPath,
        String officeRecordPath,
        String workflowEntryPath,
        String estimatorSheetPath,
        String quoteBoundaryPath,
        List<RouteDetailCard> supportCards,
        List<RouteDetailCard> workflowCards,
        String printablePacketPath,
        String prefilledEstimatorSheetPath,
        String prefilledQuoteBoundaryPath,
        String outboundSubject,
        String outboundMessage,
        String outboundBundle,
        String replyGuidance) {
}
