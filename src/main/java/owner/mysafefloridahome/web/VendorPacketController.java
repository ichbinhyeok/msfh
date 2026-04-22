package owner.mysafefloridahome.web;

import jakarta.servlet.http.HttpServletRequest;
import owner.mysafefloridahome.pages.VendorPacketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VendorPacketController {

    private final VendorPacketService vendorPacketService;

    public VendorPacketController(VendorPacketService vendorPacketService) {
        this.vendorPacketService = vendorPacketService;
    }

    @GetMapping("/tools/opening-protection/quote-prep-brief/")
    public String openingProtectionWorkflowEntry(HttpServletRequest request, Model model) {
        model.addAttribute("page", vendorPacketService.openingProtectionWorkflowEntry(baseUrl(request)));
        return "vendorWorkflow";
    }

    @GetMapping("/tools/opening-protection/quote-prep-brief/build/")
    public String openingProtectionPreQuote(
            @RequestParam(required = false) String blockingError,
            HttpServletRequest request,
            Model model) {
        model.addAttribute("page", vendorPacketService.openingProtectionPreQuote(
                baseUrl(request),
                blockingMessage(blockingError)));
        return "vendorPacket";
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
