package owner.mysafefloridahome.pages;

import java.util.List;
import owner.mysafefloridahome.data.RouteRecord;
import owner.mysafefloridahome.data.SourceRecord;

public record FamilyPageView(
        PageMeta meta,
        RouteRecord route,
        String eyebrow,
        String quickAnswer,
        String intro,
        List<String> whatNotToAssume,
        String primaryHeading,
        String primaryIntro,
        List<RouteDetailCard> primaryCards,
        String secondaryHeading,
        String secondaryIntro,
        List<RouteDetailCard> secondaryCards,
        List<SourceRecord> sourceStack) {
}
