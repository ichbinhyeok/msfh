package owner.mysafefloridahome.data;

import java.util.List;

public record ProgramPageContent(
        String slug,
        String routeId,
        String title,
        String description,
        String triggerState,
        String quickAnswer,
        String workflowMeaning,
        List<String> coverageNotes,
        String attachedHomeCaveat,
        List<String> whatNotToAssume,
        ActionChecklist actionChecklist,
        DecisionTable decisionTable,
        ActionLink primaryCta,
        ActionLink secondaryCta,
        List<ActionLink> relatedLinks,
        List<String> sourceIds) {
}
