package owner.mysafefloridahome.data;

import java.time.LocalDate;
import java.util.List;

public record ProgramSnapshot(
        String versionNotes,
        List<String> workflowNotes,
        List<String> keyGrantRules,
        List<String> prioritizationNotes,
        List<String> contractorResponsibilityNotes,
        List<String> attachedHomeScopeNotes,
        List<String> sourceIds,
        LocalDate verifiedOn,
        LocalDate nextReviewOn) {
}
