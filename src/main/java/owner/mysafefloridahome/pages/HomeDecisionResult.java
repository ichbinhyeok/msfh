package owner.mysafefloridahome.pages;

import java.util.List;
import owner.mysafefloridahome.data.ActionLink;

public record HomeDecisionResult(
        String title,
        String summary,
        String contractorTypeLabel,
        String mistakeToAvoid,
        DecisionEvidence evidence,
        ActionLink primaryAction,
        ActionLink secondaryAction,
        List<String> nextSteps,
        List<HomeRouteBranch> routeBranches) {
}
