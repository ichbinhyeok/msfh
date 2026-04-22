package owner.mysafefloridahome.quoteprep;

import java.time.OffsetDateTime;

public record OpeningProtectionBriefRecord(
        String briefId,
        String internalToken,
        String publicToken,
        OffsetDateTime createdAt,
        String siteLabel,
        String countyZip,
        String homeType,
        String recommendationLine,
        String scopeOpenings,
        String scopeLane,
        String contactLabel,
        String senderName,
        String replyInstructions,
        String serviceAreaNote,
        String permitHandlingNote,
        String attachedScopeNote,
        String boundaryScopeNote,
        boolean reportPageReceived,
        boolean photosReceived,
        boolean broadPackageRequested,
        boolean compareQuotesRequested,
        boolean reimbursementAssumed,
        boolean hoaReviewLikely) {
}
