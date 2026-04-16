package owner.mysafefloridahome.data;

import java.util.List;

public record TrustPageContent(
        String slug,
        String routeId,
        String title,
        String description,
        String quickAnswer,
        String intro,
        List<String> whatNotToAssume,
        ActionLink primaryCta,
        ActionLink secondaryCta,
        List<String> sourceIds,
        List<String> bodySections,
        List<ActionLink> relatedLinks) {
}
