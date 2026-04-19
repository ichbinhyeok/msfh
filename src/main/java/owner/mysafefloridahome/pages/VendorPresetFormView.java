package owner.mysafefloridahome.pages;

public record VendorPresetFormView(
        String presetId,
        String presetName,
        String officeLabel,
        String senderName,
        String replyInstructions,
        String serviceAreaNote,
        String permitHandlingNote,
        String attachedScopeNote,
        String boundaryScopeNote) {
}
