package owner.mysafefloridahome.vendor;

import java.time.OffsetDateTime;

public record OpeningProtectionHandoffRecord(
        String handoffId,
        String internalToken,
        String publicToken,
        OffsetDateTime createdAt,
        String siteLabel,
        String countyZip,
        String homeType,
        String recommendationLine,
        String scopeOpenings,
        String scopeLane,
        String officeLabel,
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
