package owner.mysafefloridahome.web;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import owner.mysafefloridahome.pages.VendorHandoffActivityCardView;
import owner.mysafefloridahome.pages.VendorPacketService;
import owner.mysafefloridahome.pages.SavedOfficePresetCardView;
import owner.mysafefloridahome.pages.VendorPresetFormView;
import owner.mysafefloridahome.vendor.OpeningProtectionHandoffActivity;
import owner.mysafefloridahome.vendor.OpeningProtectionHandoffService;
import owner.mysafefloridahome.vendor.OpeningProtectionOfficePresetRecord;
import owner.mysafefloridahome.vendor.OpeningProtectionOfficePresetRequest;
import owner.mysafefloridahome.vendor.OpeningProtectionOfficePresetSaveResult;
import owner.mysafefloridahome.vendor.OpeningProtectionOfficePresetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class VendorPacketController {

    private final VendorPacketService vendorPacketService;
    private final OpeningProtectionOfficePresetService officePresetService;
    private final OpeningProtectionHandoffService handoffService;

    public VendorPacketController(
            VendorPacketService vendorPacketService,
            OpeningProtectionOfficePresetService officePresetService,
            OpeningProtectionHandoffService handoffService) {
        this.vendorPacketService = vendorPacketService;
        this.officePresetService = officePresetService;
        this.handoffService = handoffService;
    }

    @GetMapping({"/tools/opening-protection/quote-prep-brief/", "/vendors/opening-protection/quote-ready-packets/"})
    public String openingProtectionWorkflowEntry(HttpServletRequest request, Model model) {
        model.addAttribute("page", vendorPacketService.openingProtectionWorkflowEntry(baseUrl(request)));
        return "vendorWorkflow";
    }

    @GetMapping({"/tools/opening-protection/quote-prep-brief/build/", "/vendor-packets/opening-protection/quote-ready-packet/"})
    public String openingProtectionPreQuote(
            @RequestParam(required = false) String blockingError,
            HttpServletRequest request,
            Model model) {
        model.addAttribute("page", vendorPacketService.openingProtectionPreQuote(
                baseUrl(request),
                blockingMessage(blockingError)));
        return "vendorPacket";
    }

    @GetMapping("/vendor-packets/opening-protection/estimator-handoff/")
    public String openingProtectionEstimatorHandoff(HttpServletRequest request, Model model) {
        model.addAttribute("page", vendorPacketService.openingProtectionEstimatorHandoff(baseUrl(request)));
        return "vendorPacket";
    }

    @GetMapping("/vendor-packets/opening-protection/quote-boundary/")
    public String openingProtectionQuoteBoundary(HttpServletRequest request, Model model) {
        model.addAttribute("page", vendorPacketService.openingProtectionQuoteBoundary(baseUrl(request)));
        return "vendorPacket";
    }

    @GetMapping("/vendor-packets/opening-protection/office-preset/")
    public String openingProtectionOfficePreset(
            @RequestParam(required = false) String preset,
            @RequestParam(required = false) String saved,
            @RequestParam(required = false) String updated,
            @RequestParam(required = false) String deleted,
            HttpServletRequest request,
            Model model) {
        VendorPresetFormView form = officePresetService.findById(preset)
                .map(this::toForm)
                .orElse(defaultForm());
        List<SavedOfficePresetCardView> savedPresets = officePresetService.listRecent(8).stream()
                .map(savedPreset -> toCard(savedPreset, form.presetId()))
                .toList();
        List<VendorHandoffActivityCardView> recentHandoffs = handoffService.listRecentActivity(form.officeLabel(), 6)
                .stream()
                .map(this::toActivityCard)
                .toList();
        String statusMessage = statusMessage(saved, updated, deleted);
        String recentHandoffsTitle = form.presetId().isBlank()
                ? "Recent public brief sends"
                : "Recent public brief sends for " + form.officeLabel();
        String recentHandoffsDescription = form.presetId().isBlank()
                ? "Use setup only after the same office keeps sending the public brief. On this screen, the clearest confirmed homeowner signal is whether the public brief actually opened."
                : "A preset matters only after repeat sends. Reopen the latest first-send results tied to this wording, and treat public-brief opens as the confirmed homeowner signal before widening the workflow.";
        model.addAttribute("page", vendorPacketService.openingProtectionOfficePreset(
                baseUrl(request),
                statusMessage,
                form,
                savedPresets,
                recentHandoffsTitle,
                recentHandoffsDescription,
                recentHandoffs));
        return "vendorPreset";
    }

    @PostMapping("/vendor-presets/opening-protection/save")
    public String saveOpeningProtectionOfficePreset(OpeningProtectionOfficePresetRequest request) {
        OpeningProtectionOfficePresetSaveResult saveResult = officePresetService.save(request);
        OpeningProtectionOfficePresetRecord preset = saveResult.record();
        return "redirect:" + UriComponentsBuilder.fromPath("/vendor-packets/opening-protection/office-preset/")
                .queryParam("preset", preset.presetId())
                .queryParam(saveResult.updated() ? "updated" : "saved", 1)
                .build()
                .encode()
                .toUriString();
    }

    @PostMapping("/vendor-presets/opening-protection/delete")
    public String deleteOpeningProtectionOfficePreset(@RequestParam String presetId) {
        officePresetService.delete(presetId);
        return "redirect:" + UriComponentsBuilder.fromPath("/vendor-packets/opening-protection/office-preset/")
                .queryParam("deleted", 1)
                .build()
                .encode()
                .toUriString();
    }

    private VendorPresetFormView defaultForm() {
        return new VendorPresetFormView(
                "",
                "",
                "",
                "",
                "",
                "",
                "Permit and inspection handling still need to be confirmed before signing.",
                "",
                "");
    }

    private VendorPresetFormView toForm(OpeningProtectionOfficePresetRecord preset) {
        return new VendorPresetFormView(
                preset.presetId(),
                preset.presetName(),
                preset.officeLabel(),
                preset.senderName(),
                preset.replyInstructions(),
                preset.serviceAreaNote(),
                preset.permitHandlingNote(),
                preset.attachedScopeNote(),
                preset.boundaryScopeNote());
    }

    private SavedOfficePresetCardView toCard(OpeningProtectionOfficePresetRecord preset, String loadedPresetId) {
        return new SavedOfficePresetCardView(
                preset.presetId(),
                preset.presetName(),
                preset.officeLabel(),
                preset.senderName(),
                preset.createdAt().toLocalDate().toString(),
                preset.presetId().equals(loadedPresetId),
                officePresetService.officePresetPagePath(preset.presetId()),
                officePresetService.packetBuilderPath(preset),
                officePresetService.estimatorPath(preset),
                officePresetService.quoteBoundaryPath(preset));
    }

    private VendorHandoffActivityCardView toActivityCard(OpeningProtectionHandoffActivity activity) {
        return new VendorHandoffActivityCardView(
                activity.siteLabel(),
                activity.officeLabel(),
                activity.createdAt().toLocalDate().toString(),
                activity.nextAction(),
                activity.statusLine(),
                activity.metrics().publicBriefOpenCount(),
                activity.metrics().briefCopyCount(),
                activity.metrics().officeRecordOpenCount(),
                handoffService.resultPath(activity.internalToken()),
                handoffService.publicBriefPath(activity.publicToken()),
                handoffService.officeRecordPath(activity.internalToken()));
    }

    private String statusMessage(String saved, String updated, String deleted) {
        if ("1".equals(updated)) {
            return "Office preset updated. Use it only if the same public brief wording is repeating.";
        }
        if ("1".equals(deleted)) {
            return "Office preset deleted. Keep setup only where the public brief is actually repeating.";
        }
        if ("1".equals(saved)) {
            return "Office preset saved. Reuse it only after the public brief starts repeating.";
        }
        return "";
    }

    private String blockingMessage(String blockingError) {
        if (!"missing_required_scope".equals(blockingError)) {
            return "";
        }
        return "Cannot create the shareable brief yet. Confirm the report recommendation, home type, and first quote focus or exact openings first.";
    }

    private String baseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        int port = request.getServerPort();
        boolean defaultPort = ("http".equalsIgnoreCase(scheme) && port == 80)
                || ("https".equalsIgnoreCase(scheme) && port == 443);
        return defaultPort
                ? scheme + "://" + request.getServerName()
                : scheme + "://" + request.getServerName() + ":" + port;
    }
}
