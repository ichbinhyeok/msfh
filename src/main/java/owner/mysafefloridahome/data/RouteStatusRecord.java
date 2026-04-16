package owner.mysafefloridahome.data;

import java.time.LocalDate;

public record RouteStatusRecord(
        String routeId,
        String routePath,
        String routeFamily,
        String scope,
        String phase,
        String indexStatus,
        String sourceFreshnessStatus,
        int last28DayImpressions,
        int last28DayClicks,
        double last28DayCtr,
        int last28DayCtaClicks,
        int last28DayLeadOpens,
        int last28DayLeadSubmissions,
        String dominantImprovementType,
        String promotionRecommendation,
        String recommendationReason,
        LocalDate reviewedOn,
        LocalDate nextReviewOn) {
}
