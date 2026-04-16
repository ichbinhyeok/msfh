package owner.mysafefloridahome.web;

import jakarta.servlet.http.HttpServletRequest;
import owner.mysafefloridahome.pages.PageService;
import owner.mysafefloridahome.pages.HomeDecisionInput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SiteController {

    private final PageService pageService;

    public SiteController(PageService pageService) {
        this.pageService = pageService;
    }

    @GetMapping("/")
    public String home(
            @RequestParam(name = "reportState", required = false) String reportState,
            @RequestParam(name = "recommendationType", required = false) String recommendationType,
            @RequestParam(name = "homeType", required = false) String homeType,
            @RequestParam(name = "priority", required = false) String priority,
            HttpServletRequest request,
            Model model) {
        model.addAttribute("page", pageService.homePage(
                new HomeDecisionInput(reportState, recommendationType, homeType, priority),
                baseUrl(request)));
        return "home";
    }

    @GetMapping("/program/")
    public String programHub(HttpServletRequest request, Model model) {
        model.addAttribute("page", pageService.familyPage("program", baseUrl(request)));
        return "family";
    }

    @GetMapping("/program/{slug}/")
    public String programPage(@PathVariable String slug,
            @RequestParam(name = "lead", required = false) String leadStatus,
            HttpServletRequest request,
            Model model) {
        model.addAttribute("page", pageService.programPage(slug, leadStatus, baseUrl(request)));
        return "program";
    }

    @GetMapping("/improvements/")
    public String improvementHub(HttpServletRequest request, Model model) {
        model.addAttribute("page", pageService.familyPage("improvement", baseUrl(request)));
        return "family";
    }

    @GetMapping("/improvements/{slug}/")
    public String improvementPage(@PathVariable String slug,
            @RequestParam(name = "lead", required = false) String leadStatus,
            HttpServletRequest request,
            Model model) {
        model.addAttribute("page", pageService.improvementPage(slug, leadStatus, baseUrl(request)));
        return "improvement";
    }

    @GetMapping("/guides/")
    public String guideHub(HttpServletRequest request, Model model) {
        model.addAttribute("page", pageService.familyPage("guide", baseUrl(request)));
        return "family";
    }

    @GetMapping("/guides/{slug}/")
    public String guidePage(@PathVariable String slug,
            @RequestParam(name = "lead", required = false) String leadStatus,
            HttpServletRequest request,
            Model model) {
        model.addAttribute("page", pageService.guidePage(slug, leadStatus, baseUrl(request)));
        return "guide";
    }

    @GetMapping("/about/")
    public String about(HttpServletRequest request, Model model) {
        model.addAttribute("page", pageService.trustPage("about", null, baseUrl(request)));
        return "trust";
    }

    @GetMapping("/methodology/")
    public String methodology(HttpServletRequest request, Model model) {
        model.addAttribute("page", pageService.trustPage("methodology", null, baseUrl(request)));
        return "trust";
    }

    @GetMapping("/contact/")
    public String contact(@RequestParam(name = "partner", required = false) String partnerStatus,
            HttpServletRequest request, Model model) {
        model.addAttribute("page", pageService.trustPage("contact", partnerStatus, baseUrl(request)));
        return "trust";
    }

    @GetMapping("/not-government-affiliated/")
    public String nonAffiliate(HttpServletRequest request, Model model) {
        model.addAttribute("page", pageService.trustPage("not-government-affiliated", null, baseUrl(request)));
        return "trust";
    }

    @GetMapping("/privacy/")
    public String privacy(HttpServletRequest request, Model model) {
        model.addAttribute("page", pageService.trustPage("privacy", null, baseUrl(request)));
        return "trust";
    }

    @GetMapping("/terms/")
    public String terms(HttpServletRequest request, Model model) {
        model.addAttribute("page", pageService.trustPage("terms", null, baseUrl(request)));
        return "trust";
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
