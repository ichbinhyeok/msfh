package owner.mysafefloridahome.vendor;

import java.time.OffsetDateTime;

public record OpeningProtectionHandoffActivity(
        String handoffId,
        String internalToken,
        String publicToken,
        OffsetDateTime createdAt,
        String siteLabel,
        String officeLabel,
        String nextAction,
        String statusLine,
        OpeningProtectionHandoffMetrics metrics) {
}
