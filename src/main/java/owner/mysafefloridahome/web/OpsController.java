package owner.mysafefloridahome.web;

import jakarta.servlet.http.HttpServletRequest;
import owner.mysafefloridahome.ops.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OpsController {

    private final AdminService adminService;

    public OpsController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin/")
    public String admin(HttpServletRequest request, Model model) {
        String scheme = request.getScheme();
        int port = request.getServerPort();
        boolean defaultPort = ("http".equalsIgnoreCase(scheme) && port == 80)
                || ("https".equalsIgnoreCase(scheme) && port == 443);
        String baseUrl = defaultPort
                ? scheme + "://" + request.getServerName()
                : scheme + "://" + request.getServerName() + ":" + port;
        model.addAttribute("page", adminService.dashboard(baseUrl));
        return "admin";
    }
}
