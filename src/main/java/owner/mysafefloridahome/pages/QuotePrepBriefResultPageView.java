package owner.mysafefloridahome.pages;

import owner.mysafefloridahome.quoteprep.OpeningProtectionBriefMetrics;
import owner.mysafefloridahome.quoteprep.OpeningProtectionBriefNarrative;
import owner.mysafefloridahome.quoteprep.OpeningProtectionBriefRecord;

public record QuotePrepBriefResultPageView(
        PageMeta meta,
        OpeningProtectionBriefRecord brief,
        OpeningProtectionBriefNarrative narrative,
        OpeningProtectionBriefMetrics metrics,
        String shareUrl,
        String publicBriefPath,
        String pdfExportPath,
        String outboundMessage,
        String replyGuidance) {
}
