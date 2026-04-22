package owner.mysafefloridahome.quoteprep;

import java.util.List;

public record OpeningProtectionBriefNarrative(
        String headline,
        String customerSummary,
        String requestLine,
        String openingsLine,
        String scopeLaneLabel,
        String scopeLaneSummary,
        String sendState,
        String sendStateLabel,
        String sendStateSummary,
        String nextAction,
        String nextReason,
        List<String> missingItems,
        List<String> homeownerReplyChecklist,
        String homeownerReplyExample,
        List<String> watchouts,
        List<OpeningProtectionBriefScenarioModule> scenarioModules,
        List<String> customerSteps,
        List<String> followUpSteps) {
}
