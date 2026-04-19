package owner.mysafefloridahome.pages;

import java.util.List;

public record VendorPresetPageView(
        PageMeta meta,
        String eyebrow,
        String title,
        String description,
        String vendorEntryPath,
        String savePresetPath,
        String deletePresetPath,
        String packetBuilderPath,
        String estimatorPath,
        String quoteBoundaryPath,
        String statusMessage,
        VendorPresetFormView form,
        List<SavedOfficePresetCardView> savedPresets,
        String recentHandoffsTitle,
        String recentHandoffsDescription,
        List<VendorHandoffActivityCardView> recentHandoffs) {
}
