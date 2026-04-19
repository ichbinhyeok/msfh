package owner.mysafefloridahome.vendor;

import java.util.List;

public record OpeningProtectionHandoffNarrative(
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
        List<OpeningProtectionScenarioModule> scenarioModules,
        List<String> customerSteps,
        List<String> officeSteps) {
}
