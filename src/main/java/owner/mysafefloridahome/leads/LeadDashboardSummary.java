package owner.mysafefloridahome.leads;

import java.util.Map;

public record LeadDashboardSummary(
        int leadCount,
        int partnerInquiryCount,
        int eventCount,
        Map<String, Long> leadsByImprovement,
        Map<String, Long> leadsByPartnerType,
        Map<String, Long> partnerInquiriesByRouteFocus,
        Map<String, Long> eventsByType,
        Map<String, Long> ctaClicksByRouteFamily) {
}
