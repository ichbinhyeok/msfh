package owner.mysafefloridahome.pages;

import java.util.List;
import owner.mysafefloridahome.data.ProgramPageContent;
import owner.mysafefloridahome.data.RouteRecord;
import owner.mysafefloridahome.data.SourceRecord;

public record ProgramPageView(
        PageMeta meta,
        RouteRecord route,
        ProgramPageContent page,
        List<RouteDetailCard> relatedRouteCards,
        List<SourceRecord> sourceStack,
        LeadFormContext leadForm,
        String leadStatus) {
}
