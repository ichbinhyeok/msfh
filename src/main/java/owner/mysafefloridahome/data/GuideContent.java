package owner.mysafefloridahome.data;

import java.util.List;

public record GuideContent(
        String slug,
        String routeId,
        String title,
        String description,
        String triggerState,
        String quickAnswer,
        List<String> whatNotToAssume,
        List<String> keyTakeaways,
        List<String> nextSteps,
        ActionLink primaryCta,
        ActionLink secondaryCta,
        List<ActionLink> relatedLinks,
        List<String> sourceIds) {
}
