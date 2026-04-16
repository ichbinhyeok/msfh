package owner.mysafefloridahome.data;

import java.util.List;

public record PromotionRouteNote(String routeId, String decision, String reason, List<String> blockers) {
}
