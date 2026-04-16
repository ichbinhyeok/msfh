package owner.mysafefloridahome.data;

import java.util.List;

public record ActionChecklist(
        String eyebrow,
        String title,
        String intro,
        List<String> items) {
}
