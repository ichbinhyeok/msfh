package owner.mysafefloridahome;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String baseUrl;
    private String siteName;
    private final Storage storage = new Storage();
    private final Admin admin = new Admin();

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String resolvedBaseUrl(String requestBaseUrl) {
        String candidate = StringUtils.hasText(baseUrl) ? baseUrl : requestBaseUrl;
        if (!StringUtils.hasText(candidate)) {
            return "";
        }
        return candidate.endsWith("/") ? candidate.substring(0, candidate.length() - 1) : candidate;
    }

    public String absoluteUrl(String path, String requestBaseUrl) {
        String resolvedBaseUrl = resolvedBaseUrl(requestBaseUrl);
        return resolvedBaseUrl.isBlank() ? path : resolvedBaseUrl + path;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Storage getStorage() {
        return storage;
    }

    public Admin getAdmin() {
        return admin;
    }

    public static class Storage {
        private String leadsPath;
        private String eventsPath;
        private String partnerInquiriesPath;

        public String getLeadsPath() {
            return leadsPath;
        }

        public void setLeadsPath(String leadsPath) {
            this.leadsPath = leadsPath;
        }

        public String getEventsPath() {
            return eventsPath;
        }

        public void setEventsPath(String eventsPath) {
            this.eventsPath = eventsPath;
        }

        public String getPartnerInquiriesPath() {
            return partnerInquiriesPath;
        }

        public void setPartnerInquiriesPath(String partnerInquiriesPath) {
            this.partnerInquiriesPath = partnerInquiriesPath;
        }
    }

    public static class Admin {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
