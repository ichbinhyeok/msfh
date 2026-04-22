package owner.mysafefloridahome.leads;

import java.util.Map;

public record LeadDashboardSummary(
        int leadCount,
        int eventCount,
        Map<String, Long> leadsByImprovement,
        Map<String, Long> leadsByContractorType,
        Map<String, Long> eventsByType,
        Map<String, Long> ctaClicksByRouteFamily) {
}
