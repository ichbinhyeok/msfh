package owner.mysafefloridahome.pages;

import java.util.List;
import owner.mysafefloridahome.data.ActionLink;
import owner.mysafefloridahome.data.ContentRepository;
import owner.mysafefloridahome.data.SourceRecord;
import owner.mysafefloridahome.leads.ContractorTypeService;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class DecisionEngineService {

    private final ContentRepository contentRepository;
    private final ContractorTypeService contractorTypeService;

    public DecisionEngineService(ContentRepository contentRepository, ContractorTypeService contractorTypeService) {
        this.contentRepository = contentRepository;
        this.contractorTypeService = contractorTypeService;
    }

    public HomeDecisionResult decide(HomeDecisionInput input) {
        if (input == null || !input.hasUserInput()) {
            return null;
        }

        return switch (input.reportState()) {
            case "status-problem" -> statusProblemResult();
            case "received-no-recommendations" -> noRecommendationsResult();
            case "not-yet" -> reportNotReadyResult();
            default -> recommendationResult(input);
        };
    }

    private HomeDecisionResult recommendationResult(HomeDecisionInput input) {
        if ("attached".equals(input.homeType()) && isRoofRelated(input.recommendationType())) {
            return attachedHomeOverrideResult(input.priority());
        }

        String contractorType = contractorTypeService.resolveContractorType(
                scenarioFor(input.recommendationType()), input.recommendationType(), input.homeType());

        return switch (contractorType) {
            case "opening-protection-contractor" -> openingProtectionResult(input);
            case "roof-retrofit-specialist" -> roofRetrofitResult(input.recommendationType(), input.priority());
            case "roofing-contractor" -> roofingResult(input.recommendationType(), input.priority());
            default -> unsureRecommendationResult(input.priority(), input.homeType());
        };
    }

    private HomeDecisionResult openingProtectionResult(HomeDecisionInput input) {
        String priority = input.priority();
        ActionLink primary = "compare-quotes".equals(priority)
                ? new ActionLink("Get the opening-protection quote path", "/guides/impact-windows-vs-shutters/")
                : new ActionLink("Review opening protection first", "/improvements/opening-protection/");
        ActionLink secondary = "compare-quotes".equals(priority)
                ? new ActionLink("See what the program may pay for", "/program/what-msfh-will-pay-for/")
                : new ActionLink("Get the contractor quote checklist", "/guides/msfh-contractor-quote-checklist/");
        return new HomeDecisionResult(
                "Start with opening protection before you widen scope",
                "Your recommendation is pointing toward opening protection. Treat that as the first project filter, then compare quotes only after you know which openings and documents actually matter.",
                "Window or shutter contractor",
                "Do not let a window-heavy sales pitch turn a narrow recommendation into a whole-house replacement story.",
                evidence("recommended-improvements"),
                primary,
                secondary,
                quotePrepQuickStart(input, "Opening protection recommendation from the report"),
                List.of(
                        "Confirm which openings the report actually flags.",
                        "Keep the first quote tied to the eligible opening scope.",
                        "Separate optional upgrades from grant-aligned work."),
                List.of(
                        branch("Run now", primary,
                                "The recommendation is narrow enough to branch into opening protection before broader quote shopping."),
                        branch("Hold for later", secondary,
                                "Keep this nearby, but only after the opening scope is explicit."),
                        branch("Do not widen yet",
                                new ActionLink("Check when roof replacement is actually eligible",
                                        "/improvements/roof-replacement-under-swr/"),
                                "A roof-heavy path is the usual overreaction here unless the report clearly demands it.")));
    }

    private HomeDecisionResult roofRetrofitResult(String recommendationType, String priority) {
        ActionLink route = switch (recommendationType) {
            case "roof-deck-attachment" ->
                new ActionLink("Review roof deck attachment first", "/improvements/roof-deck-attachment/");
            default -> new ActionLink("Review roof-to-wall first", "/improvements/roof-to-wall/");
        };
        ActionLink quote = new ActionLink("Get the roof-related quote checklist", "/guides/msfh-contractor-quote-checklist/");
        return new HomeDecisionResult(
                "Keep this as a retrofit decision, not a generic reroof job",
                "Your recommendation fits the retrofit side of the workflow. The first move is to understand the exact attachment recommendation and only then compare specialists or roof-adjacent quotes.",
                "Roof retrofit specialist",
                "Do not assume a general roofer and a retrofit specialist solve the same recommendation.",
                evidence("authorized-improvements"),
                "compare-quotes".equals(priority) ? quote : route,
                "compare-quotes".equals(priority) ? route : quote,
                null,
                List.of(
                        "Read the exact recommendation language again.",
                        "Verify the contractor license matches the retrofit scope.",
                        "Keep unrelated roof upgrades out of the first quote."),
                List.of(
                        branch("Run now", "compare-quotes".equals(priority) ? quote : route,
                                "This route keeps the work framed as retrofit scope instead of letting it drift into a full reroof story."),
                        branch("Hold for later", "compare-quotes".equals(priority) ? route : quote,
                                "Use the companion route once the recommendation language and contractor fit are both clear."),
                        branch("Do not widen yet",
                                new ActionLink("Review roof replacement under SWR logic",
                                        "/improvements/roof-replacement-under-swr/"),
                                "Replacement logic is a different test and should not become the default just because the roof is involved.")));
    }

    private HomeDecisionResult roofingResult(String recommendationType, String priority) {
        ActionLink route = switch (recommendationType) {
            case "roof-replacement-under-swr" ->
                new ActionLink("Review roof replacement under SWR logic", "/improvements/roof-replacement-under-swr/");
            default -> new ActionLink("Review secondary water resistance first", "/improvements/secondary-water-resistance/");
        };
        ActionLink quote = "roof-replacement-under-swr".equals(recommendationType)
                ? new ActionLink("Get the roof replacement quote path", "/guides/roof-replacement-through-msfh/")
                : new ActionLink("Get the contractor quote checklist", "/program/contractor-quotes/");
        return new HomeDecisionResult(
                "Use the roof recommendation as a scope filter, not a blank check",
                "Your next step is to tie the roof-related recommendation back to what the program may actually fund. This is where homeowners often turn a narrow eligible path into a much broader roof job too early.",
                "Roofing contractor",
                "Do not budget around reimbursement until the eligible roof scope is explicit in the quote path.",
                evidence("roof-replacement"),
                "compare-quotes".equals(priority) ? quote : route,
                "compare-quotes".equals(priority) ? route : new ActionLink("See what the program may pay for", "/program/what-msfh-will-pay-for/"),
                null,
                List.of(
                        "Check whether the report truly ties replacement to eligible work.",
                        "Keep SWR logic visible in the first quote review.",
                        "Separate grant-aligned scope from the rest of the reroof job."),
                List.of(
                        branch("Run now", "compare-quotes".equals(priority) ? quote : route,
                                "Use the roof route that keeps reimbursement logic visible instead of treating the entire reroof as approved."),
                        branch("Hold for later",
                                "compare-quotes".equals(priority) ? route
                                        : new ActionLink("See what the program may pay for",
                                                "/program/what-msfh-will-pay-for/"),
                                "This is the next check once the first eligibility question is clear."),
                        branch("Do not widen yet",
                                new ActionLink("Choose the first project", "/program/choose-project/"),
                                "Do not reopen the whole project menu after the report already pushed you into a roof-specific decision.")));
    }

    private HomeDecisionResult attachedHomeOverrideResult(String priority) {
        ActionLink primary = "compare-quotes".equals(priority)
                ? new ActionLink("Get the opening-protection quote path", "/guides/impact-windows-vs-shutters/")
                : new ActionLink("Branch into opening protection first", "/improvements/opening-protection/");
        return new HomeDecisionResult(
                "Start with opening protection before a roof-heavy quote",
                "Because the home is attached or townhouse-like, the safest first move is to treat opening protection as the first scope check before you assume roof-related work belongs in the first grant-backed project.",
                "Window or shutter contractor",
                "Do not let attached-home scope drift into broad roof replacement assumptions before the rule check is clear.",
                evidence("select-projects"),
                primary,
                new ActionLink("See what the program may pay for", "/program/what-msfh-will-pay-for/"),
                quotePrepQuickStart(
                        new HomeDecisionInput("received-recommendation", "opening-protection", "attached", priority),
                        "Attached-home scope may need opening protection first before broader roof work is priced"),
                List.of(
                        "Verify whether attached-home rules narrow the project list.",
                        "Check opening protection before you compare roof-heavy bids.",
                        "Only widen scope after the program path is explicit."),
                List.of(
                        branch("Run now", primary,
                                "Attached-home cases need the narrowest eligible path first, and that usually means opening protection."),
                        branch("Hold for later",
                                new ActionLink("See what the program may pay for", "/program/what-msfh-will-pay-for/"),
                                "Use the reimbursement check after the attached-home scope is already narrowed."),
                        branch("Do not widen yet",
                                new ActionLink("Review roof-to-wall first", "/improvements/roof-to-wall/"),
                                "Roof-heavy interpretation is where attached-home users most often burn trust and money.")));
    }

    private HomeDecisionResult noRecommendationsResult() {
        return new HomeDecisionResult(
                "No recommended improvements is the decision right now",
                "If the report shows no recommended improvements, the first job is not contractor shopping. The first job is to confirm the report outcome and understand whether there is any corrective path before you spend money.",
                "No contractor type yet",
                "Do not self-select a project and hope it becomes reimbursable later.",
                evidence("no-recommendations"),
                new ActionLink("Review the no-recommendations path", "/program/no-recommended-improvements/"),
                new ActionLink("Return to the inspection report guide", "/program/inspection-report/"),
                null,
                List.of(
                        "Confirm the report result inside the portal.",
                        "Check whether anything in the record needs clarification.",
                        "Avoid generic quote shopping until the status changes."),
                List.of(
                        branch("Run now",
                                new ActionLink("Review the no-recommendations path",
                                        "/program/no-recommended-improvements/"),
                                "This is the real status. Treat it as a stop sign until the facts change."),
                        branch("Hold for later",
                                new ActionLink("Return to the inspection report guide",
                                        "/program/inspection-report/"),
                                "Go back here only to verify the report outcome, not to reopen contractor shopping."),
                        branch("Do not start here",
                                new ActionLink("Compare contractor quotes safely", "/program/contractor-quotes/"),
                                "Quote collection makes the situation feel productive, but it does not restore eligibility.")));
    }

    private HomeDecisionResult statusProblemResult() {
        return new HomeDecisionResult(
                "Resolve the status problem before you plan the project",
                "If you are stuck on Group 5, RFI, or another portal-state problem, the support issue is the decision right now. Quote planning comes after the file can move again.",
                "No contractor type yet",
                "Do not treat a portal status as permission to start reimbursable work.",
                evidence("support-hub"),
                new ActionLink("See what the portal status changes", "/program/portal-statuses/"),
                new ActionLink("Review the RFI path", "/program/rfi/"),
                null,
                List.of(
                        "Figure out what the current status actually blocks.",
                        "Resolve missing information before treating the project as secure.",
                        "Return to project choice only after the support state is clear."),
                List.of(
                        branch("Run now",
                                new ActionLink("See what the portal status changes", "/program/portal-statuses/"),
                                "This tells you what the state blocks before you spend time pricing work."),
                        branch("Hold for later",
                                new ActionLink("Review the RFI path", "/program/rfi/"),
                                "Use the specific support route once you know whether the blocker is a missing item."),
                        branch("Do not start here",
                                new ActionLink("Choose the first project", "/program/choose-project/"),
                                "Project choice is downstream from file movement. It is not the current bottleneck.")));
    }

    private HomeDecisionResult reportNotReadyResult() {
        return new HomeDecisionResult(
                "This tool gets stronger once the report arrives",
                "If you do not have the report yet, the right next move is to prepare for the recommendation stage rather than guess at a contractor path too early.",
                "Contractor type depends on the recommendation",
                "Do not lock into a project before the report tells you what the program actually observed.",
                evidence("report-results"),
                new ActionLink("Start with the inspection report guide", "/program/inspection-report/"),
                new ActionLink("See how project choice works after the report", "/program/choose-project/"),
                null,
                List.of(
                        "Wait for the recommended improvements section.",
                        "Use the report to narrow the first project.",
                        "Only then move into quotes and contractor type."),
                List.of(
                        branch("Run now",
                                new ActionLink("Start with the inspection report guide", "/program/inspection-report/"),
                                "This keeps the homeowner oriented around the report instead of premature contractor research."),
                        branch("Hold for later",
                                new ActionLink("See how project choice works after the report", "/program/choose-project/"),
                                "Project choice becomes useful once the report actually names the mitigation path."),
                        branch("Do not start here",
                                new ActionLink("Compare contractor quotes safely", "/program/contractor-quotes/"),
                                "A quote checklist is precise only after the recommendation exists.")));
    }

    private HomeDecisionResult unsureRecommendationResult(String priority, String homeType) {
        String summary = "attached".equals(homeType)
                ? "Because the home is attached, the safest first move is to read the report and opening-protection logic before you assume a roof-heavy first project."
                : "When the recommendation is still unclear, start by reading the report through a project-choice lens instead of rushing into contractor outreach.";
        ActionLink primary = new ActionLink("Read the inspection report first", "/program/inspection-report/");
        ActionLink secondary = new ActionLink("Choose the first project", "/program/choose-project/");
        return new HomeDecisionResult(
                "Clarify the recommendation before you compare contractors",
                summary,
                "Contractor type depends on the recommendation",
                "Do not let urgency turn an unclear recommendation into the wrong first quote.",
                evidence("recommended-improvements"),
                primary,
                secondary,
                null,
                List.of(
                        "Read the recommended improvements section again.",
                        "Name the first project before you shop the quote.",
                        "Only then choose the contractor type."),
                List.of(
                        branch("Run now", primary,
                                "The next route should reduce ambiguity, not reward it."),
                        branch("Hold for later", secondary,
                                "Project choice becomes trustworthy only after the recommendation reads clearly."),
                        branch("Do not start here",
                                new ActionLink("Review roof replacement under SWR logic",
                                        "/improvements/roof-replacement-under-swr/"),
                                "Deep roof logic is the wrong first branch when the recommendation itself is still fuzzy.")));
    }

    private boolean isRoofRelated(String recommendationType) {
        return "roof-to-wall".equals(recommendationType)
                || "roof-deck-attachment".equals(recommendationType)
                || "secondary-water-resistance".equals(recommendationType)
                || "roof-replacement-under-swr".equals(recommendationType);
    }

    private String scenarioFor(String recommendationType) {
        return switch (recommendationType) {
            case "opening-protection" -> "opening_protection_decision";
            case "roof-to-wall", "roof-deck-attachment", "secondary-water-resistance", "roof-replacement-under-swr" ->
                "roof_related_decision";
            default -> "report_received_need_next_step";
        };
    }

    private DecisionEvidence evidence(String sourceId) {
        SourceRecord source = contentRepository.sourceById(sourceId)
                .orElseThrow(() -> new IllegalStateException("Missing decision evidence source: " + sourceId));
        return new DecisionEvidence("Official signal", source.note(), source.title(), source.url());
    }

    private HomeRouteBranch branch(String label, ActionLink action, String note) {
        return new HomeRouteBranch(label, action, note);
    }

    private ActionLink quotePrepQuickStart(HomeDecisionInput input, String recommendationLine) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/tools/opening-protection/quote-prep-brief/build/")
                .queryParam("homeType", input.homeType())
                .queryParam("scopeLane", "mixed")
                .queryParam("recommendationLine", recommendationLine);
        if ("received-recommendation".equals(input.reportState())
                || "received-no-recommendations".equals(input.reportState())) {
            builder.queryParam("reportPageReceived", true);
        }
        if ("compare-quotes".equals(input.priority())) {
            builder.queryParam("compareQuotesRequested", true);
        }
        if ("attached".equals(input.homeType())) {
            builder.queryParam(
                    "attachedScopeNote",
                    "Because this home may be attached or townhouse-like, keep the first quote inside the narrower attached-home opening scope until the path is confirmed.");
        }
        return new ActionLink("Build the first quote request", builder.build().encode().toUriString());
    }
}
