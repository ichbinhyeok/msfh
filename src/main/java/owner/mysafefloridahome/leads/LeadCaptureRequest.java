package owner.mysafefloridahome.leads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LeadCaptureRequest {

    @NotBlank
    private String originPath;

    @NotBlank
    private String routePath;

    @NotBlank
    private String routeFamily;

    @NotBlank
    private String scenario;

    private String improvementType;

    @NotBlank
    private String county;

    @Pattern(regexp = "^[0-9A-Za-z -]{3,10}$")
    private String zip;

    @NotBlank
    private String reportReceived;

    @NotBlank
    private String homeType;

    private String partnerType;

    @NotBlank
    private String budgetRange;

    @NotBlank
    private String timeline;

    @Email
    @NotBlank
    private String email;

    @Size(max = 40)
    private String phone;

    private boolean consent;

    public String getOriginPath() {
        return originPath;
    }

    public void setOriginPath(String originPath) {
        this.originPath = originPath;
    }

    public String getRoutePath() {
        return routePath;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    public String getRouteFamily() {
        return routeFamily;
    }

    public void setRouteFamily(String routeFamily) {
        this.routeFamily = routeFamily;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getImprovementType() {
        return improvementType;
    }

    public void setImprovementType(String improvementType) {
        this.improvementType = improvementType;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getReportReceived() {
        return reportReceived;
    }

    public void setReportReceived(String reportReceived) {
        this.reportReceived = reportReceived;
    }

    public String getHomeType() {
        return homeType;
    }

    public void setHomeType(String homeType) {
        this.homeType = homeType;
    }

    public String getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(String partnerType) {
        this.partnerType = partnerType;
    }

    public String getBudgetRange() {
        return budgetRange;
    }

    public void setBudgetRange(String budgetRange) {
        this.budgetRange = budgetRange;
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isConsent() {
        return consent;
    }

    public void setConsent(boolean consent) {
        this.consent = consent;
    }
}
