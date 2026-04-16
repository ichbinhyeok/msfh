package owner.mysafefloridahome.pages;

import java.util.List;
import owner.mysafefloridahome.data.ProgramSnapshot;
import owner.mysafefloridahome.data.SourceRecord;

public record HomePageView(
        PageMeta meta,
        ProgramSnapshot programSnapshot,
        List<RouteSummaryCard> scenarioCards,
        List<RouteSummaryCard> improvementCards,
        List<RouteSummaryCard> guideCards,
        List<RouteDetailCard> verdictRouteCards,
        List<SourceRecord> sourceStack,
        HomeDecisionInput decisionInput,
        HomeDecisionResult decisionResult) {
}
