package owner.mysafefloridahome.data;

public record RouteRecord(
        String routeId,
        String path,
        String family,
        String title,
        String phase,
        String indexStatus,
        String sourceFreshnessStatus,
        String dominantImprovementType,
        String triggerState,
        String summary) {
}
