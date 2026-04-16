package owner.mysafefloridahome.leads;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import owner.mysafefloridahome.AppProperties;
import org.springframework.stereotype.Service;

@Service
public class LeadStorageService {

    private static final String LEADS_HEADER =
            "captured_at,route_path,route_family,scenario,improvement_type,partner_type,county,zip,report_received,home_type,budget_range,timeline,email,phone,consent";
    private static final String PARTNER_INQUIRIES_HEADER =
            "captured_at,inquiry_type,route_focus,contact_name,company,email,phone,license_number,counties_served,message,consent";
    private static final String EVENTS_HEADER =
            "captured_at,event_type,route_path,route_family,scenario,improvement_type,detail";

    private final Path leadsPath;
    private final Path partnerInquiriesPath;
    private final Path eventsPath;

    public LeadStorageService(AppProperties appProperties) {
        this.leadsPath = Path.of(appProperties.getStorage().getLeadsPath());
        this.partnerInquiriesPath = Path.of(appProperties.getStorage().getPartnerInquiriesPath());
        this.eventsPath = Path.of(appProperties.getStorage().getEventsPath());
    }

    public synchronized void captureLead(LeadCaptureRequest request) {
        ensureFile(leadsPath, LEADS_HEADER);
        String row = String.join(",",
                sanitize(OffsetDateTime.now().toString()),
                sanitize(request.getRoutePath()),
                sanitize(request.getRouteFamily()),
                sanitize(request.getScenario()),
                sanitize(request.getImprovementType()),
                sanitize(request.getPartnerType()),
                sanitize(request.getCounty()),
                sanitize(request.getZip()),
                sanitize(request.getReportReceived()),
                sanitize(request.getHomeType()),
                sanitize(request.getBudgetRange()),
                sanitize(request.getTimeline()),
                sanitize(request.getEmail()),
                sanitize(request.getPhone()),
                sanitize(Boolean.toString(request.isConsent())));
        appendRow(leadsPath, row);
        logEvent(internalEvent("lead_submit_success", request.getRoutePath(), request.getRouteFamily(),
                request.getScenario(), request.getImprovementType(), "form_submit"));
    }

    public synchronized void capturePartnerInquiry(PartnerInquiryRequest request) {
        ensureFile(partnerInquiriesPath, PARTNER_INQUIRIES_HEADER);
        String row = String.join(",",
                sanitize(OffsetDateTime.now().toString()),
                sanitize(request.getInquiryType()),
                sanitize(request.getRouteFocus()),
                sanitize(request.getContactName()),
                sanitize(request.getCompany()),
                sanitize(request.getEmail()),
                sanitize(request.getPhone()),
                sanitize(request.getLicenseNumber()),
                sanitize(request.getCountiesServed()),
                sanitize(request.getMessage()),
                sanitize(Boolean.toString(request.isConsent())));
        appendRow(partnerInquiriesPath, row);
        logEvent(internalEvent("partner_inquiry_submit_success", "/contact/", "trust",
                request.getInquiryType(), request.getRouteFocus(), request.getCompany()));
    }

    public synchronized void logEvent(LeadEventRequest request) {
        ensureFile(eventsPath, EVENTS_HEADER);
        String row = String.join(",",
                sanitize(OffsetDateTime.now().toString()),
                sanitize(request.getEventType()),
                sanitize(request.getRoutePath()),
                sanitize(request.getRouteFamily()),
                sanitize(request.getScenario()),
                sanitize(request.getImprovementType()),
                sanitize(request.getDetail()));
        appendRow(eventsPath, row);
    }

    public LeadDashboardSummary dashboardSummary() {
        List<String[]> leadRows = readRows(leadsPath);
        List<String[]> partnerInquiryRows = readRows(partnerInquiriesPath);
        List<String[]> eventRows = readRows(eventsPath);
        Map<String, Long> leadsByImprovement = leadRows.stream()
                .collect(Collectors.groupingBy(row -> blankToUnknown(column(row, 4)), LinkedHashMap::new,
                        Collectors.counting()));
        Map<String, Long> leadsByPartnerType = leadRows.stream()
                .collect(Collectors.groupingBy(row -> blankToUnknown(column(row, 5)), LinkedHashMap::new,
                        Collectors.counting()));
        Map<String, Long> partnerInquiriesByRouteFocus = partnerInquiryRows.stream()
                .collect(Collectors.groupingBy(row -> blankToUnknown(column(row, 2)), LinkedHashMap::new,
                        Collectors.counting()));
        Map<String, Long> eventsByType = eventRows.stream()
                .collect(Collectors.groupingBy(row -> blankToUnknown(column(row, 1)), LinkedHashMap::new,
                        Collectors.counting()));
        Map<String, Long> ctaClicksByRouteFamily = eventRows.stream()
                .filter(row -> "route_cta_click".equals(column(row, 1)) || "lead_form_open".equals(column(row, 1))
                        || "partner_outbound_click".equals(column(row, 1)))
                .collect(Collectors.groupingBy(row -> blankToUnknown(column(row, 3)), LinkedHashMap::new,
                        Collectors.counting()));
        return new LeadDashboardSummary(leadRows.size(), partnerInquiryRows.size(), eventRows.size(),
                sortDescending(leadsByImprovement), sortDescending(leadsByPartnerType),
                sortDescending(partnerInquiriesByRouteFocus),
                sortDescending(eventsByType), sortDescending(ctaClicksByRouteFamily));
    }

    private LeadEventRequest internalEvent(String eventType, String routePath, String routeFamily, String scenario,
            String improvementType, String detail) {
        LeadEventRequest event = new LeadEventRequest();
        event.setEventType(eventType);
        event.setRoutePath(routePath);
        event.setRouteFamily(routeFamily);
        event.setScenario(scenario);
        event.setImprovementType(improvementType);
        event.setDetail(detail);
        return event;
    }

    private void ensureFile(Path path, String header) {
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            if (!Files.exists(path)) {
                Files.writeString(path, header + System.lineSeparator(), StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE_NEW);
                return;
            }
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            if (lines.isEmpty()) {
                Files.writeString(path, header + System.lineSeparator(), StandardCharsets.UTF_8,
                        StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
                return;
            }
            if (!header.equals(lines.getFirst())) {
                Path legacyPath = legacyBackupPath(path);
                Files.move(path, legacyPath, StandardCopyOption.REPLACE_EXISTING);
                Files.writeString(path, header + System.lineSeparator(), StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to prepare storage file " + path, exception);
        }
    }

    private Path legacyBackupPath(Path path) {
        String fileName = path.getFileName().toString();
        int extensionIndex = fileName.lastIndexOf('.');
        String baseName = extensionIndex >= 0 ? fileName.substring(0, extensionIndex) : fileName;
        String extension = extensionIndex >= 0 ? fileName.substring(extensionIndex) : "";
        String legacyName = baseName + ".legacy-" + System.currentTimeMillis() + extension;
        return path.resolveSibling(legacyName);
    }

    private void appendRow(Path path, String row) {
        try {
            Files.writeString(path, row + System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to append storage file " + path, exception);
        }
    }

    private List<String[]> readRows(Path path) {
        if (!Files.exists(path)) {
            return List.of();
        }
        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8).stream()
                    .skip(1)
                    .filter(line -> !line.isBlank())
                    .map(line -> line.split(",", -1))
                    .toList();
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read storage file " + path, exception);
        }
    }

    private Map<String, Long> sortDescending(Map<String, Long> source) {
        return source.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (left, right) -> left,
                        LinkedHashMap::new));
    }

    private String column(String[] row, int index) {
        return index < row.length ? row[index] : "";
    }

    private String blankToUnknown(String value) {
        return value == null || value.isBlank() ? "unknown" : value;
    }

    private String sanitize(String value) {
        if (value == null) {
            return "";
        }
        return value.replace(",", " ").replace("\r", " ").replace("\n", " ").trim();
    }
}
