package owner.mysafefloridahome.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import owner.mysafefloridahome.leads.ContractorTypeService;
import owner.mysafefloridahome.leads.LeadCaptureRequest;
import owner.mysafefloridahome.leads.LeadEventRequest;
import owner.mysafefloridahome.leads.LeadStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class LeadController {

    private final LeadStorageService leadStorageService;
    private final ContractorTypeService contractorTypeService;
    private final RequestAbuseProtectionService requestAbuseProtectionService;

    public LeadController(LeadStorageService leadStorageService, ContractorTypeService contractorTypeService,
            RequestAbuseProtectionService requestAbuseProtectionService) {
        this.leadStorageService = leadStorageService;
        this.contractorTypeService = contractorTypeService;
        this.requestAbuseProtectionService = requestAbuseProtectionService;
    }

    @PostMapping("/api/leads/capture")
    public String captureLead(@Valid LeadCaptureRequest request, BindingResult bindingResult,
            HttpServletRequest httpServletRequest) {
        if (!requestAbuseProtectionService.allowLeadForm(httpServletRequest)) {
            return "redirect:" + safeOrigin(request.getOriginPath()) + "?lead=error#lead-form";
        }
        if (!request.isConsent()) {
            bindingResult.rejectValue("consent", "required", "Consent is required");
        }

        if (bindingResult.hasErrors()) {
            return "redirect:" + safeOrigin(request.getOriginPath()) + "?lead=error#lead-form";
        }

        request.setContractorType(contractorTypeService.resolveContractorType(
                request.getScenario(), request.getImprovementType(), request.getHomeType()));
        leadStorageService.captureLead(request);
        return "redirect:" + safeOrigin(request.getOriginPath()) + "?lead=success#lead-form";
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(value = "/api/leads/event", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void captureEvent(@Valid @RequestBody LeadEventRequest request, HttpServletRequest httpServletRequest) {
        if (!requestAbuseProtectionService.allowEventCapture(httpServletRequest)) {
            return;
        }
        leadStorageService.logEvent(request);
    }

    private String safeOrigin(String originPath) {
        if (originPath == null || originPath.isBlank() || !originPath.startsWith("/")
                || originPath.startsWith("//") || originPath.contains("://")) {
            return "/";
        }
        return originPath;
    }
}
