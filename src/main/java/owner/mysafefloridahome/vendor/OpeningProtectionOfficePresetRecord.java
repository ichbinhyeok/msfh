package owner.mysafefloridahome.vendor;

import java.time.OffsetDateTime;

public record OpeningProtectionOfficePresetRecord(
        String presetId,
        OffsetDateTime createdAt,
        String presetName,
        String officeLabel,
        String senderName,
        String replyInstructions,
        String serviceAreaNote,
        String permitHandlingNote,
        String attachedScopeNote,
        String boundaryScopeNote) {
}
