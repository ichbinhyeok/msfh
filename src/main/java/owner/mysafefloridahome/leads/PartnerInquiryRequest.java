package owner.mysafefloridahome.leads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PartnerInquiryRequest {

    @NotBlank
    private String originPath;

    @NotBlank
    private String inquiryType;

    @NotBlank
    private String routeFocus;

    @NotBlank
    private String contactName;

    @NotBlank
    private String company;

    @Email
    @NotBlank
    private String email;

    @Size(max = 40)
    private String phone;

    @NotBlank
    private String licenseNumber;

    @NotBlank
    private String countiesServed;

    @NotBlank
    @Size(max = 600)
    private String message;

    @Size(max = 0)
    private String website;

    private boolean consent;

    public String getOriginPath() {
        return originPath;
    }

    public void setOriginPath(String originPath) {
        this.originPath = originPath;
    }

    public String getInquiryType() {
        return inquiryType;
    }

    public void setInquiryType(String inquiryType) {
        this.inquiryType = inquiryType;
    }

    public String getRouteFocus() {
        return routeFocus;
    }

    public void setRouteFocus(String routeFocus) {
        this.routeFocus = routeFocus;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getCountiesServed() {
        return countiesServed;
    }

    public void setCountiesServed(String countiesServed) {
        this.countiesServed = countiesServed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public boolean isConsent() {
        return consent;
    }

    public void setConsent(boolean consent) {
        this.consent = consent;
    }
}
