package owner.mysafefloridahome.pages;

import java.util.List;
import owner.mysafefloridahome.data.ImprovementContent;
import owner.mysafefloridahome.data.RouteRecord;
import owner.mysafefloridahome.data.SourceRecord;

public record ImprovementPageView(
        PageMeta meta,
        RouteRecord route,
        ImprovementContent page,
        List<RouteDetailCard> relatedRouteCards,
        List<SourceRecord> sourceStack,
        LeadFormContext leadForm,
        String leadStatus) {
}
