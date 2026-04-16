package owner.mysafefloridahome.pages;

import java.util.List;

public record PageMeta(
        String title,
        String description,
        String canonicalUrl,
        String robots,
        List<BreadcrumbItem> breadcrumbs,
        String siteBaseUrl,
        String reviewedOn) {
}
