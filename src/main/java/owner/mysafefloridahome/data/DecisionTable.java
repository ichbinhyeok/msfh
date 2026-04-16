package owner.mysafefloridahome.data;

import java.util.List;

public record DecisionTable(
        String eyebrow,
        String title,
        String intro,
        List<String> columns,
        List<DecisionTableRow> rows) {
}
