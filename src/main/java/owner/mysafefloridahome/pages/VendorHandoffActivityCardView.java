package owner.mysafefloridahome.pages;

public record VendorHandoffActivityCardView(
        String siteLabel,
        String officeLabel,
        String createdOn,
        String nextAction,
        String statusLine,
        long publicOpens,
        long linkCopies,
        long officeOpens,
        String resultPath,
        String publicBriefPath,
        String officeRecordPath) {
}
