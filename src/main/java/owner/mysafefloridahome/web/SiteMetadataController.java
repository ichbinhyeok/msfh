package owner.mysafefloridahome.web;

import jakarta.servlet.http.HttpServletRequest;
import owner.mysafefloridahome.AppProperties;
import owner.mysafefloridahome.data.ContentRepository;
import owner.mysafefloridahome.data.RouteRecord;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SiteMetadataController {

    private final ContentRepository contentRepository;
    private final AppProperties appProperties;

    public SiteMetadataController(ContentRepository contentRepository, AppProperties appProperties) {
        this.contentRepository = contentRepository;
        this.appProperties = appProperties;
    }

    @ResponseBody
    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public String robots(HttpServletRequest request) {
        String sitemapUrl = appProperties.absoluteUrl("/sitemap.xml", baseUrl(request));
        return """
                User-agent: *
                Allow: /
                Disallow: /api/

                Sitemap: %s
                """.formatted(sitemapUrl);
    }

    @ResponseBody
    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String sitemap(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        String requestBaseUrl = baseUrl(request);
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        for (RouteRecord route : contentRepository.indexableRoutes()) {
            builder.append("<url><loc>")
                    .append(appProperties.absoluteUrl(route.path(), requestBaseUrl))
                    .append("</loc></url>");
        }
        builder.append("</urlset>");
        return builder.toString();
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
