package owner.mysafefloridahome.pages;

public record HomeDecisionInput(
        String reportState,
        String recommendationType,
        String homeType,
        String priority,
        boolean userInput) {

    public HomeDecisionInput(String reportState, String recommendationType, String homeType, String priority) {
        this(
                reportState,
                recommendationType,
                homeType,
                priority,
                hasValue(reportState) || hasValue(recommendationType) || hasValue(homeType) || hasValue(priority));
    }

    public HomeDecisionInput {
        reportState = normalize(reportState, "received-recommendation");
        recommendationType = normalize(recommendationType, "not-sure");
        homeType = normalize(homeType, "detached");
        priority = normalize(priority, "choose-project");
    }

    public boolean hasUserInput() {
        return userInput;
    }

    private static String normalize(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.trim().toLowerCase();
    }

    private static boolean hasValue(String value) {
        return value != null && !value.isBlank();
    }
}
