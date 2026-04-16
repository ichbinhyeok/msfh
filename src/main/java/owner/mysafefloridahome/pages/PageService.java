package owner.mysafefloridahome.pages;

import java.time.LocalDate;
import java.util.List;
import owner.mysafefloridahome.AppProperties;
import owner.mysafefloridahome.data.ActionLink;
import owner.mysafefloridahome.data.ContentRepository;
import owner.mysafefloridahome.data.GuideContent;
import owner.mysafefloridahome.data.ImprovementContent;
import owner.mysafefloridahome.data.ProgramPageContent;
import owner.mysafefloridahome.data.RouteRecord;
import owner.mysafefloridahome.data.SourceRecord;
import owner.mysafefloridahome.data.TrustPageContent;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PageService {

    private final ContentRepository contentRepository;
    private final AppProperties appProperties;
    private final DecisionEngineService decisionEngineService;

    public PageService(ContentRepository contentRepository, AppProperties appProperties,
            DecisionEngineService decisionEngineService) {
        this.contentRepository = contentRepository;
        this.appProperties = appProperties;
        this.decisionEngineService = decisionEngineService;
    }

    public HomePageView homePage(String requestBaseUrl) {
        return homePage(new HomeDecisionInput(null, null, null, null), requestBaseUrl);
    }

    public HomePageView homePage(HomeDecisionInput decisionInput, String requestBaseUrl) {
        List<RouteSummaryCard> scenarioCards = List.of(
                card("/program/inspection-report/"),
                card("/program/choose-project/"),
                card("/program/contractor-quotes/"),
                card("/improvements/opening-protection/"));
        List<RouteSummaryCard> improvementCards = List.of(
                card("/improvements/opening-protection/"),
                card("/improvements/roof-to-wall/"),
                card("/improvements/roof-deck-attachment/"),
                card("/improvements/secondary-water-resistance/"),
                card("/improvements/roof-replacement-under-swr/"));
        List<RouteSummaryCard> guideCards = List.of(
                card("/guides/msfh-inspection-report-what-next/"),
                card("/guides/impact-windows-vs-shutters/"),
                card("/guides/msfh-contractor-quote-checklist/"),
                card("/guides/roof-replacement-through-msfh/"));
        List<SourceRecord> sourceStack = contentRepository.sourcesFor(contentRepository.programSnapshot().sourceIds());
        PageMeta meta = new PageMeta(
                appProperties.getSiteName() + " | Florida post-inspection decision support",
                "Florida-only decision support for homeowners comparing recommended improvements, contractor types, and quote paths after an MSFH inspection report.",
                canonicalUrl("/", requestBaseUrl),
                "index,follow",
                List.of(new BreadcrumbItem("Home", "/")),
                siteBaseUrl(requestBaseUrl),
                latestReviewedOn(sourceStack));
        HomeDecisionInput normalizedInput = decisionInput == null
                ? new HomeDecisionInput(null, null, null, null)
                : decisionInput;
        HomeDecisionResult decisionResult = decisionEngineService.decide(normalizedInput);
        return new HomePageView(meta, contentRepository.programSnapshot(), scenarioCards, improvementCards, guideCards,
                decisionResult == null ? defaultVerdictCards() : detailCardsForHome(decisionResult.routeBranches()),
                sourceStack,
                normalizedInput,
                decisionResult);
    }

    public ProgramPageView programPage(String slug, String leadStatus, String requestBaseUrl) {
        ProgramPageContent page = contentRepository.programPage(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        RouteRecord route = routeById(page.routeId());
        List<SourceRecord> sourceStack = contentRepository.sourcesFor(page.sourceIds());
        return new ProgramPageView(metaFor(route, sourceStack, requestBaseUrl), route, page, detailCardsForLinks(page.relatedLinks()),
                sourceStack,
                new LeadFormContext(route.path(), route.family(), page.triggerState(),
                        route.dominantImprovementType() == null ? "" : route.dominantImprovementType(),
                        page.primaryCta().label()),
                leadStatus);
    }

    public ImprovementPageView improvementPage(String slug, String leadStatus, String requestBaseUrl) {
        ImprovementContent page = contentRepository.improvement(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        RouteRecord route = routeById(page.routeId());
        List<SourceRecord> sourceStack = contentRepository.sourcesFor(page.sourceIds());
        return new ImprovementPageView(metaFor(route, sourceStack, requestBaseUrl), route, page, detailCardsForLinks(page.relatedLinks()),
                sourceStack,
                new LeadFormContext(route.path(), route.family(), page.triggerState(), page.slug(),
                        page.primaryCta().label()),
                leadStatus);
    }

    public GuidePageView guidePage(String slug, String leadStatus, String requestBaseUrl) {
        GuideContent page = contentRepository.guide(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        RouteRecord route = routeById(page.routeId());
        List<SourceRecord> sourceStack = contentRepository.sourcesFor(page.sourceIds());
        return new GuidePageView(metaFor(route, sourceStack, requestBaseUrl), route, page, detailCardsForLinks(page.relatedLinks()),
                sourceStack,
                new LeadFormContext(route.path(), route.family(), page.triggerState(), route.dominantImprovementType(),
                        page.primaryCta().label()),
                leadStatus);
    }

    public TrustPageView trustPage(String slug, String partnerStatus, String requestBaseUrl) {
        TrustPageContent page = contentRepository.trustPage(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        RouteRecord route = routeById(page.routeId());
        List<SourceRecord> sourceStack = contentRepository.sourcesFor(page.sourceIds());
        return new TrustPageView(metaFor(route, sourceStack, requestBaseUrl), route, page, detailCardsForLinks(page.relatedLinks()),
                sourceStack, partnerStatus);
    }

    public FamilyPageView familyPage(String family, String requestBaseUrl) {
        return switch (family) {
            case "program" -> programHub(requestBaseUrl);
            case "guide" -> guideHub(requestBaseUrl);
            case "improvement" -> improvementHub(requestBaseUrl);
            default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        };
    }

    private RouteSummaryCard card(String path) {
        RouteRecord route = contentRepository.routeByPath(path)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Missing route seed: " + path));
        return new RouteSummaryCard(route.title(), route.summary(), route.path(), route.family());
    }

    private FamilyPageView programHub(String requestBaseUrl) {
        RouteRecord route = contentRepository.routeByPath("/program/")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<SourceRecord> sourceStack = sourcesForFamily("program");
        return new FamilyPageView(
                metaFor(route, sourceStack, requestBaseUrl),
                route,
                "Program hub",
                "Use the program routes to interpret the report, choose the first project, and pressure-test eligibility before you sign anything.",
                "Start here when your question is really about the report, the first project, or whether the work even belongs in the grant path.",
                List.of(
                        "Do not treat the report as approval.",
                        "Do not treat Group 5, RFI, or portal-status confusion like project advice.",
                        "Do not jump to quote shopping before the first project is clear."),
                "Core decision routes",
                "These are the main routes most homeowners should use first after the report lands.",
                detailCardsForPaths(List.of(
                        "/program/inspection-report/",
                        "/program/choose-project/",
                        "/program/what-msfh-will-pay-for/",
                        "/program/contractor-quotes/")),
                "Support-state routes",
                "Use these only when the portal or report state is the blocker. They are corrective routes, not the main wedge.",
                detailCardsForPaths(List.of(
                        "/program/no-recommended-improvements/",
                        "/program/group-5/",
                        "/program/rfi/",
                        "/program/portal-statuses/",
                        "/program/final-inspection-and-draw-request/")),
                sourceStack);
    }

    private FamilyPageView guideHub(String requestBaseUrl) {
        RouteRecord route = contentRepository.routeByPath("/guides/")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<SourceRecord> sourceStack = sourcesForFamily("guide");
        return new FamilyPageView(
                metaFor(route, sourceStack, requestBaseUrl),
                route,
                "Guide hub",
                "Use the guides when the route is known but the homeowner still needs a narrower checklist before acting.",
                "Use one guide when you know the question but still want a tighter checklist before you act.",
                List.of(
                        "Do not use a guide as a substitute for the main route that owns the decision.",
                        "Do not bounce across three similar guides when one page already answers the blocker.",
                        "Do not treat a checklist page like approval or reimbursement confirmation."),
                "Decision guides",
                "Each guide should answer one supporting question clearly, then send you back to the route that owns the real next step.",
                detailCardsForPaths(List.of(
                        "/guides/msfh-inspection-report-what-next/",
                        "/guides/impact-windows-vs-shutters/",
                        "/guides/msfh-contractor-quote-checklist/",
                        "/guides/roof-replacement-through-msfh/",
                        "/guides/opening-protection-quote-checklist/",
                        "/guides/roof-to-wall-quote-checklist/",
                        "/guides/swr-roof-quote-checklist/",
                        "/guides/attached-home-scope-under-msfh/",
                        "/guides/no-recommended-improvements-what-next/",
                        "/guides/msfh-rfi-response-checklist/",
                        "/guides/msfh-group-5-what-next/",
                        "/guides/msfh-portal-statuses-explained/",
                        "/guides/final-inspection-draw-request-checklist/")),
                "",
                "",
                List.of(),
                sourceStack);
    }

    private FamilyPageView improvementHub(String requestBaseUrl) {
        RouteRecord route = contentRepository.routeByPath("/improvements/")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<SourceRecord> sourceStack = sourcesForFamily("improvement");
        return new FamilyPageView(
                metaFor(route, sourceStack, requestBaseUrl),
                route,
                "Improvement hub",
                "Use the improvement routes to decide what a specific recommendation really means before a contractor widens the scope.",
                "Use these pages when the recommendation is clear but the scope still feels slippery, especially on roof-related work.",
                List.of(
                        "Do not let every roof recommendation turn into a generic reroof pitch.",
                        "Do not assume attached homes follow detached-home scope.",
                        "Do not use the wrong improvement page just because the contractor widened the job."),
                "Improvement routes",
                "Each route should help you decide what one recommendation really means before price shopping starts.",
                detailCardsForPaths(List.of(
                        "/improvements/opening-protection/",
                        "/improvements/roof-to-wall/",
                        "/improvements/roof-deck-attachment/",
                        "/improvements/secondary-water-resistance/",
                        "/improvements/roof-replacement-under-swr/")),
                "",
                "",
                List.of(),
                sourceStack);
    }

    private List<RouteDetailCard> defaultVerdictCards() {
        return List.of(
                detailCard("Start here", new ActionLink("Read the report first", "/program/inspection-report/"),
                        "The report is still the cleanest first source of truth before the wedge narrows."),
                detailCard("Then choose", new ActionLink("Choose the first project", "/program/choose-project/"),
                        "Project choice should come after the recommendation is read, not before."),
                detailCard("Only after that", new ActionLink("Compare contractor quotes safely", "/program/contractor-quotes/"),
                        "The quote stage is precise only when the project and scope are already disciplined."));
    }

    private List<RouteDetailCard> detailCardsForHome(List<HomeRouteBranch> branches) {
        return branches.stream()
                .map(branch -> detailCard(branch.label(), branch.action(), branch.note()))
                .toList();
    }

    private List<RouteDetailCard> detailCardsForLinks(List<ActionLink> links) {
        return links.stream()
                .map(link -> detailCard(link.label(), link, null))
                .toList();
    }

    private List<RouteDetailCard> detailCardsForPaths(List<String> paths) {
        return paths.stream()
                .map(path -> detailCard(familyLabelForPath(path), new ActionLink("Route", path), null))
                .toList();
    }

    private RouteDetailCard detailCard(String eyebrow, ActionLink action, String summaryOverride) {
        RouteRecord route = contentRepository.routeByPath(action.href()).orElse(null);
        if (route == null) {
            return new RouteDetailCard(eyebrow, action.label(),
                    summaryOverride == null ? "Open the decision tool for the next route." : summaryOverride,
                    action.href(),
                    "Decision tool");
        }
        return new RouteDetailCard(
                eyebrow,
                route.title(),
                summaryOverride == null ? route.summary() : summaryOverride,
                route.path(),
                familyLabel(route.family()));
    }

    private String familyLabel(String family) {
        return switch (family) {
            case "program" -> "Program route";
            case "guide" -> "Guide";
            case "improvement" -> "Improvement route";
            case "trust" -> "Trust page";
            default -> "Route";
        };
    }

    private String familyLabelForPath(String path) {
        RouteRecord route = contentRepository.routeByPath(path).orElse(null);
        if (route == null) {
            return "Route";
        }
        return familyLabel(route.family());
    }

    private List<SourceRecord> sourcesForFamily(String family) {
        List<String> sourceIds = switch (family) {
            case "program" -> contentRepository.programPages().stream()
                    .flatMap(page -> page.sourceIds().stream())
                    .distinct()
                    .toList();
            case "guide" -> contentRepository.guides().stream()
                    .flatMap(page -> page.sourceIds().stream())
                    .distinct()
                    .toList();
            case "improvement" -> contentRepository.improvements().stream()
                    .flatMap(page -> page.sourceIds().stream())
                    .distinct()
                    .toList();
            default -> List.of();
        };
        return contentRepository.sourcesFor(sourceIds);
    }

    private RouteRecord routeById(String routeId) {
        return contentRepository.routeById(routeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Missing route seed: " + routeId));
    }

    private PageMeta metaFor(RouteRecord route, List<SourceRecord> sources, String requestBaseUrl) {
        return new PageMeta(
                route.title() + " | " + appProperties.getSiteName(),
                route.summary(),
                canonicalUrl(route.path(), requestBaseUrl),
                "index".equals(route.indexStatus()) ? "index,follow" : "noindex,nofollow",
                breadcrumbsFor(route),
                siteBaseUrl(requestBaseUrl),
                latestReviewedOn(sources));
    }

    private List<BreadcrumbItem> breadcrumbsFor(RouteRecord route) {
        if ("/".equals(route.path())) {
            return List.of(new BreadcrumbItem("Home", "/"));
        }
        if (route.path().equals(familyHubPath(route.family()))) {
            return List.of(
                    new BreadcrumbItem("Home", "/"),
                    new BreadcrumbItem(route.title(), null));
        }
        BreadcrumbItem familyCrumb = switch (route.family()) {
            case "program" -> new BreadcrumbItem("Program", "/program/");
            case "improvement" -> new BreadcrumbItem("Improvements", "/improvements/");
            case "guide" -> new BreadcrumbItem("Guides", "/guides/");
            case "trust" -> new BreadcrumbItem("Trust", "/methodology/");
            case "admin" -> new BreadcrumbItem("Admin", "/admin/");
            default -> new BreadcrumbItem("Pages", "/");
        };
        return List.of(
                new BreadcrumbItem("Home", "/"),
                familyCrumb,
                new BreadcrumbItem(route.title(), route.path()));
    }

    private String familyHubPath(String family) {
        return switch (family) {
            case "program" -> "/program/";
            case "guide" -> "/guides/";
            case "improvement" -> "/improvements/";
            default -> "";
        };
    }

    private String canonicalUrl(String path, String requestBaseUrl) {
        return appProperties.absoluteUrl(path, requestBaseUrl);
    }

    private String siteBaseUrl(String requestBaseUrl) {
        return appProperties.resolvedBaseUrl(requestBaseUrl);
    }

    private String latestReviewedOn(List<SourceRecord> sources) {
        return sources.stream()
                .map(SourceRecord::verifiedOn)
                .max(LocalDate::compareTo)
                .map(LocalDate::toString)
                .orElse(null);
    }
}
