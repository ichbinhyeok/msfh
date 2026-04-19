package owner.mysafefloridahome.pages;

public record SavedOfficePresetCardView(
        String presetId,
        String presetName,
        String officeLabel,
        String senderName,
        String createdOn,
        boolean loaded,
        String loadPath,
        String packetBuilderPath,
        String estimatorPath,
        String quoteBoundaryPath) {
}
