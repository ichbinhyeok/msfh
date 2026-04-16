package owner.mysafefloridahome.pages;

import java.util.List;
import owner.mysafefloridahome.data.RouteRecord;
import owner.mysafefloridahome.data.SourceRecord;
import owner.mysafefloridahome.data.TrustPageContent;

public record TrustPageView(
        PageMeta meta,
        RouteRecord route,
        TrustPageContent page,
        List<RouteDetailCard> relatedRouteCards,
        List<SourceRecord> sourceStack,
        String partnerStatus) {
}
