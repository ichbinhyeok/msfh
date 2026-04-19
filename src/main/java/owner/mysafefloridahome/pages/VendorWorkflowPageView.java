package owner.mysafefloridahome.pages;

import java.util.List;

public record VendorWorkflowPageView(
        PageMeta meta,
        String eyebrow,
        String title,
        String description,
        String quickAnswer,
        String laneSummary,
        List<String> quoteReadinessCriteria,
        List<String> workflowSteps,
        List<String> fitPoints,
        List<String> guardrails,
        List<RouteDetailCard> artifactCards,
        String officePresetPath) {
}
