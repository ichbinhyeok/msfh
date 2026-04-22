package owner.mysafefloridahome.leads;

import org.springframework.stereotype.Service;

@Service
public class ContractorTypeService {

    public String resolveContractorType(String scenario, String improvementType, String homeType) {
        String normalizedScenario = normalize(scenario);
        String normalizedImprovementType = normalize(improvementType);
        String normalizedHomeType = normalize(homeType);

        if ("attached".equals(normalizedHomeType) || "attached_home_scope_constraint".equals(normalizedScenario)) {
            return "opening-protection-contractor";
        }
        if ("opening-protection".equals(normalizedImprovementType)
                || "opening_protection_decision".equals(normalizedScenario)) {
            return "opening-protection-contractor";
        }
        if ("roof-to-wall".equals(normalizedImprovementType)
                || "roof-deck-attachment".equals(normalizedImprovementType)) {
            return "roof-retrofit-specialist";
        }
        if ("secondary-water-resistance".equals(normalizedImprovementType)
                || "roof-replacement-under-swr".equals(normalizedImprovementType)
                || "roof_related_decision".equals(normalizedScenario)) {
            return "roofing-contractor";
        }
        return "needs-manual-review";
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }
}
