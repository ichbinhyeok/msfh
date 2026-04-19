package owner.mysafefloridahome.pages;

import java.util.List;

public record VendorPacketPageView(
        PageMeta meta,
        String eyebrow,
        String title,
        String description,
        String vendorEntryPath,
        String quickAnswer,
        String roleTitle,
        String roleSummary,
        String checklistTitle,
        String checklistIntro,
        List<String> checklistItems,
        String secondaryTitle,
        String secondaryIntro,
        List<String> secondaryItems,
        String cautionTitle,
        List<String> cautionItems,
        String blockingMessage,
        List<RouteDetailCard> siblingCards) {
}
