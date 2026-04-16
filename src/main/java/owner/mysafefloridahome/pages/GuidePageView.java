package owner.mysafefloridahome.pages;

import java.util.List;
import owner.mysafefloridahome.data.GuideContent;
import owner.mysafefloridahome.data.RouteRecord;
import owner.mysafefloridahome.data.SourceRecord;

public record GuidePageView(
        PageMeta meta,
        RouteRecord route,
        GuideContent page,
        List<RouteDetailCard> relatedRouteCards,
        List<SourceRecord> sourceStack,
        LeadFormContext leadForm,
        String leadStatus) {
}
