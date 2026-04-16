package owner.mysafefloridahome.data;

import java.time.LocalDate;
import java.util.List;

public record PromotionReview(
        LocalDate reviewDate,
        String dataWindow,
        String agentSummary,
        List<PromotionRouteNote> promotedCandidateRoutes,
        List<PromotionRouteNote> heldRoutesStillNotReady,
        List<PromotionRouteNote> routesToDemoteOrMerge,
        List<String> blockers) {
}
