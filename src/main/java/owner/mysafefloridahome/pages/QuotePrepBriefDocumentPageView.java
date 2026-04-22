package owner.mysafefloridahome.pages;

import owner.mysafefloridahome.quoteprep.OpeningProtectionBriefNarrative;
import owner.mysafefloridahome.quoteprep.OpeningProtectionBriefRecord;

public record QuotePrepBriefDocumentPageView(
        PageMeta meta,
        OpeningProtectionBriefRecord brief,
        OpeningProtectionBriefNarrative narrative,
        String publicBriefPath,
        String pdfExportPath,
        String replyGuidance) {
}
