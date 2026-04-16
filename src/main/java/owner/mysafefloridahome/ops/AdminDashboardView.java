package owner.mysafefloridahome.ops;

import java.util.List;
import owner.mysafefloridahome.data.PromotionReview;
import owner.mysafefloridahome.data.RouteHealthRecord;
import owner.mysafefloridahome.data.RouteStatusRecord;
import owner.mysafefloridahome.data.SourceRecord;
import owner.mysafefloridahome.leads.LeadDashboardSummary;
import owner.mysafefloridahome.pages.PageMeta;

public record AdminDashboardView(
        PageMeta meta,
        LeadDashboardSummary leadSummary,
        List<SourceRecord> staleSources,
        List<RouteHealthRecord> staleRoutes,
        List<RouteHealthRecord> routeHealth,
        List<RouteStatusRecord> routeStatuses,
        PromotionReview promotionReview) {
}
