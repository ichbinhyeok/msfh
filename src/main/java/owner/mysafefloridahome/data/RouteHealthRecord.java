package owner.mysafefloridahome.data;

public record RouteHealthRecord(
        RouteRecord route,
        String effectiveSourceFreshnessStatus,
        String effectivePromotionRecommendation,
        String effectiveRecommendationReason) {
}
