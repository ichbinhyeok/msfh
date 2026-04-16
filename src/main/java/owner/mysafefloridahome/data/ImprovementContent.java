package owner.mysafefloridahome.data;

import java.time.LocalDate;
import java.util.List;

public record ImprovementContent(
        String slug,
        String routeId,
        String displayLabel,
        String title,
        String description,
        String triggerState,
        String contractorType,
        String recommendationMeaning,
        List<String> rightFirstProjectSignals,
        List<String> qualifyingScope,
        String attachedHomeCaveat,
        List<String> confusionOrDenialRisks,
        ActionLink primaryCta,
        ActionLink secondaryCta,
        List<ActionLink> relatedLinks,
        List<String> sourceIds,
        LocalDate verifiedOn,
        LocalDate nextReviewOn) {
}
