package owner.mysafefloridahome.data;

import java.time.LocalDate;

public record SourceRecord(
        String id,
        String title,
        String url,
        String note,
        LocalDate verifiedOn,
        LocalDate nextReviewOn) {
}
