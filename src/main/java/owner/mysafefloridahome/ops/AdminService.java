package owner.mysafefloridahome.ops;

import java.util.List;
import owner.mysafefloridahome.AppProperties;
import owner.mysafefloridahome.data.ContentRepository;
import owner.mysafefloridahome.leads.LeadStorageService;
import owner.mysafefloridahome.pages.BreadcrumbItem;
import owner.mysafefloridahome.pages.PageMeta;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final ContentRepository contentRepository;
    private final LeadStorageService leadStorageService;
    private final AppProperties appProperties;

    public AdminService(ContentRepository contentRepository, LeadStorageService leadStorageService,
            AppProperties appProperties) {
        this.contentRepository = contentRepository;
        this.leadStorageService = leadStorageService;
        this.appProperties = appProperties;
    }

    public AdminDashboardView dashboard() {
        return dashboard("");
    }

    public AdminDashboardView dashboard(String requestBaseUrl) {
        PageMeta meta = new PageMeta(
                "Admin | " + appProperties.getSiteName(),
                "Operations review for route status, source freshness, and lead activity.",
                appProperties.absoluteUrl("/admin/", requestBaseUrl),
                "noindex,nofollow",
                List.of(new BreadcrumbItem("Home", "/"), new BreadcrumbItem("Admin", "/admin/")),
                appProperties.resolvedBaseUrl(requestBaseUrl),
                null);
        return new AdminDashboardView(meta, leadStorageService.dashboardSummary(),
                contentRepository.sourceFreshnessWatchlist(), contentRepository.staleRouteHealth(),
                contentRepository.routeHealth(), contentRepository.routeStatuses(),
                contentRepository.promotionReview());
    }
}
