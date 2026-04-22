package owner.mysafefloridahome.quoteprep;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import owner.mysafefloridahome.AppProperties;
import owner.mysafefloridahome.leads.LeadEventRequest;
import owner.mysafefloridahome.leads.LeadStorageService;
import org.springframework.stereotype.Service;

@Service
public class OpeningProtectionQuotePrepService {

    private static final String BRIEFS_HEADER =
            "brief_id,internal_token,public_token,created_at,site_label,county_zip,home_type,recommendation_line,scope_openings,scope_lane,contact_label,sender_name,reply_instructions,service_area_note,permit_handling_note,attached_scope_note,boundary_scope_note,report_page_received,photos_received,broad_package_requested,compare_quotes_requested,reimbursement_assumed,hoa_review_likely";
    private static final int LEGACY_BRIEF_COLUMN_COUNT = 19;
    private static final int EXPANDED_BRIEF_COLUMN_COUNT = 22;
    private static final int CANONICAL_BRIEF_COLUMN_COUNT = 23;

    private final AppProperties appProperties;
    private final LeadStorageService leadStorageService;
    private final Path briefsPath;
    private final Path eventsPath;

    public OpeningProtectionQuotePrepService(AppProperties appProperties, LeadStorageService leadStorageService) {
        this.appProperties = appProperties;
        this.leadStorageService = leadStorageService;
        this.briefsPath = Path.of(appProperties.getStorage().getQuotePrepBriefsPath());
        this.eventsPath = Path.of(appProperties.getStorage().getEventsPath());
    }

    public synchronized OpeningProtectionBriefRecord create(OpeningProtectionBriefRequest request) {
        OpeningProtectionBriefRecord record = new OpeningProtectionBriefRecord(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                OffsetDateTime.now(),
                normalize(request.getSiteLabel(), "this home"),
                normalize(request.getCountyZip(), ""),
                normalize(request.getHomeType(), "detached"),
                normalize(request.getRecommendationLine(), ""),
                normalize(request.getScopeOpenings(), ""),
                normalizeScopeLane(request.getScopeLane()),
                "",
                "",
                normalize(request.getReplyInstructions(), ""),
                normalize(request.getServiceAreaNote(), ""),
                normalize(request.getPermitHandlingNote(), ""),
                normalize(request.getAttachedScopeNote(), ""),
                normalize(request.getBoundaryScopeNote(), ""),
                request.isReportPageReceived(),
                request.isPhotosReceived(),
                request.isBroadPackageRequested(),
                request.isCompareQuotesRequested(),
                request.isReimbursementAssumed(),
                request.isHoaReviewLikely());
        ensureFile(briefsPath, BRIEFS_HEADER);
        appendRow(briefsPath, csvRow(
                sanitize(record.briefId()),
                sanitize(record.internalToken()),
                sanitize(record.publicToken()),
                sanitize(record.createdAt().toString()),
                sanitize(record.siteLabel()),
                sanitize(record.countyZip()),
                sanitize(record.homeType()),
                sanitize(record.recommendationLine()),
                sanitize(record.scopeOpenings()),
                sanitize(record.scopeLane()),
                sanitize(record.contactLabel()),
                sanitize(record.senderName()),
                sanitize(record.replyInstructions()),
                sanitize(record.serviceAreaNote()),
                sanitize(record.permitHandlingNote()),
                sanitize(record.attachedScopeNote()),
                sanitize(record.boundaryScopeNote()),
                sanitize(Boolean.toString(record.reportPageReceived())),
                sanitize(Boolean.toString(record.photosReceived())),
                sanitize(Boolean.toString(record.broadPackageRequested())),
                sanitize(Boolean.toString(record.compareQuotesRequested())),
                sanitize(Boolean.toString(record.reimbursementAssumed())),
                sanitize(Boolean.toString(record.hoaReviewLikely()))));
        logEvent(
                "quote_prep_brief_created",
                resultPath(record.internalToken()),
                "briefId=" + record.briefId() + ";surface=result;action=created");
        return record;
    }

    public List<String> publicBriefBlockingItems(OpeningProtectionBriefRequest request) {
        List<String> blockingItems = new ArrayList<>();
        if (normalize(request.getRecommendationLine(), "").isBlank()) {
            blockingItems.add("the report recommendation");
        }
        if (!validHomeType(request.getHomeType())) {
            blockingItems.add("the home type");
        }
        if (normalizeScopeLane(request.getScopeLane()).isBlank()
                && normalize(request.getScopeOpenings(), "").isBlank()) {
            blockingItems.add("the first quote focus or exact openings");
        }
        return List.copyOf(blockingItems);
    }

    private List<String> publicBriefBlockingItems(OpeningProtectionBriefRecord record) {
        List<String> blockingItems = new ArrayList<>();
        if (record.recommendationLine().isBlank()) {
            blockingItems.add("the report recommendation");
        }
        if (!validHomeType(record.homeType())) {
            blockingItems.add("the home type");
        }
        if (record.scopeLane().isBlank() && record.scopeOpenings().isBlank()) {
            blockingItems.add("the first quote focus or exact openings");
        }
        return List.copyOf(blockingItems);
    }

    public Optional<OpeningProtectionBriefRecord> findByInternalToken(String internalToken) {
        String normalizedToken = normalize(internalToken, "");
        if (normalizedToken.isBlank()) {
            return Optional.empty();
        }
        return readRows(briefsPath).stream()
                .map(this::toRecord)
                .filter(record -> record.internalToken().equals(normalizedToken))
                .findFirst();
    }

    public Optional<OpeningProtectionBriefRecord> findByPublicToken(String publicToken) {
        String normalizedToken = normalize(publicToken, "");
        if (normalizedToken.isBlank()) {
            return Optional.empty();
        }
        return readRows(briefsPath).stream()
                .map(this::toRecord)
                .filter(record -> record.publicToken().equals(normalizedToken))
                .findFirst();
    }

    public OpeningProtectionBriefNarrative narrative(OpeningProtectionBriefRecord record) {
        List<String> missing = new ArrayList<>();
        List<String> watchouts = new ArrayList<>();
        List<OpeningProtectionBriefScenarioModule> scenarioModules = new ArrayList<>();
        boolean scopeStillBroad = scopeLaneNeedsNarrowing(record.scopeLane());
        List<String> blockingItems = publicBriefBlockingItems(record);

        if (!record.reportPageReceived()) {
            missing.add("the report page that shows the opening-protection recommendation");
        }
        if (!record.photosReceived()) {
            missing.add("clear photos of the openings for this first quote");
        }
        if (record.countyZip().isBlank()) {
            missing.add("the county or ZIP code for the property");
        }
        if (record.recommendationLine().isBlank()) {
            missing.add("the recommendation wording from the report");
        }
        if (record.scopeOpenings().isBlank()) {
            missing.add("the exact windows or doors that belong in this first quote");
        }
        if (record.scopeLane().isBlank()) {
            missing.add("the first quote focus: windows, shutters, doors, or a smaller opening-protection mix");
        }

        watchouts.add("This brief does not approve work, reimbursement, or every opening in the home.");
        watchouts.add("Any window or door not named for this first quote stays outside scope.");
        if (record.reimbursementAssumed()) {
            watchouts.add("Do not treat this brief as proof that approval or reimbursement is already confirmed.");
        }
        if (scopeStillBroad) {
            watchouts.add("The conversation is still too broad for one clear first quote.");
        }
        if (record.broadPackageRequested()) {
            watchouts.add("Do not treat this as a broad whole-house package.");
        }

        if ("attached".equals(record.homeType())) {
            scenarioModules.add(new OpeningProtectionBriefScenarioModule(
                    "Attached-home caution",
                    "Keep this first quote inside the attached-home scope",
                    record.attachedScopeNote().isBlank()
                            ? "Because this home may be attached or townhouse-like, compare only quotes that stay with the same named openings and any attached-home limits."
                            : sentence(record.attachedScopeNote())));
        }
        if (record.compareQuotesRequested()) {
            scenarioModules.add(new OpeningProtectionBriefScenarioModule(
                    "Quote comparison",
                    "Compare only the same openings and the same exclusions",
                    "If you collect other bids now, compare only quotes that cover the same named openings, the same path, and the same exclusions."));
        }
        if (scopeStillBroad) {
            scenarioModules.add(new OpeningProtectionBriefScenarioModule(
                    "Narrow the first quote",
                    "Narrow this before the first quote moves forward",
                    "Right now this still sounds broader than one clear windows, shutters, doors, or small mixed openings quote."));
        }
        if (record.reimbursementAssumed()) {
            scenarioModules.add(new OpeningProtectionBriefScenarioModule(
                    "Program caution",
                    "Approval or reimbursement is still not confirmed",
                    "This brief does not confirm grant approval, reimbursement timing, or that any contractor price will be reimbursed."));
        }
        if (record.hoaReviewLikely()) {
            scenarioModules.add(new OpeningProtectionBriefScenarioModule(
                    "Community review",
                    "HOA or condo review may still sit outside this brief",
                    "This first quote can still be useful before any HOA or condo review is finished."));
        }

        String recommendationSummary = record.recommendationLine().isBlank()
                ? "This brief keeps the first quote tied to the MSFH report instead of drifting into a broader whole-house package."
                : "This brief keeps the first quote tied to this MSFH recommendation: " + sentence(record.recommendationLine());
        String scopeLaneLabel = scopeLaneLabel(record.scopeLane());
        String scopeLaneSummary = scopeLaneSummary(record);
        List<String> requestItems = new ArrayList<>(missing);
        if (scopeStillBroad) {
            requestItems.add("the narrowest focus you want quoted first");
        }
        String requestLine = requestItems.isEmpty()
                ? "This brief should be enough to start one narrow first-quote conversation."
                : "If you need more before pricing or scheduling, keep it limited to " + joinWithCommas(requestItems) + ".";
        String openingsLine = record.scopeOpenings().isBlank()
                ? "The first quote should only cover the openings supported by the report and the photos already shared."
                : "The first quote should only cover " + sentence(record.scopeOpenings());
        String boundaryLine = sentence(defaultExcludedOpenings(record));
        String permitLine = sentence(defaultPermitHandling(record));
        String homeownerReplyFocusItem = scopeStillBroad
                ? "Tell me the narrowest focus you can quote first, and whether this request is still too broad."
                : "Confirm the first quote can stay "
                        + scopeLaneReplyPhrase(record.scopeLane())
                        + " and limited to "
                        + openingsReplyPhrase(record)
                        + ".";
        List<String> homeownerReplyChecklist = List.of(
                homeownerReportReplyItem(record),
                homeownerPhotosReplyItem(record),
                homeownerReplyFocusItem);
        String homeownerReplyExample = "Example reply: "
                + replyExample(record);

        String customerSummary = scopeStillBroad
                ? "This brief is meant to narrow the first contractor quote conversation before anyone prices or schedules."
                : "This brief gives a contractor one narrow first-quote frame before the conversation widens.";
        String sendState = "send_now";
        String sendStateLabel = "Ready to share";
        String sendStateSummary = "The brief is specific enough to share now so the first reply comes back narrower and easier to classify before time gets spent.";
        String nextAction = "Share quote-prep brief now";
        String nextReason = "This brief now explains what the first quote includes, what stays outside it, and what the contractor should clarify next.";
        if (!blockingItems.isEmpty()) {
            sendState = "blocked";
            sendStateLabel = "Do not share yet";
            sendStateSummary = "The quote-prep anchors are still too thin. Fill the report-backed basics before creating the shareable brief.";
            nextAction = "Fill the scope anchors";
            nextReason = "This first quote still needs the core report-backed basics that explain what belongs in scope.";
        } else if (scopeStillBroad || record.broadPackageRequested() || !missing.isEmpty()) {
            sendState = "clarification";
            sendStateLabel = "Share as clarification brief";
            sendStateSummary = "Use this brief to narrow the first reply before measurements, price comparison, or scope drift starts.";
        }
        if (scopeStillBroad || record.broadPackageRequested()) {
            nextAction = "Narrow the scope before comparing quotes";
            nextReason = "The current conversation is still broader than a clean opening-protection quote focus, so narrow it before contractor time gets spent.";
        } else if (!missing.isEmpty()) {
            nextAction = "Share it and keep follow-up narrow";
            nextReason = "The first reply should stay limited to the report-backed details that still matter before pricing.";
        }

        List<String> customerSteps = List.of(
                "First quote focus: " + scopeLaneSummary,
                "What this first quote can include: " + openingsLine,
                "If more is needed before pricing or scheduling: " + requestLine,
                "What stays outside this first quote for now: " + boundaryLine,
                "Permit and inspection note: " + permitLine,
                "Why this brief exists: " + recommendationSummary,
                "How to reply: " + replyInstruction(record));
        List<String> followUpSteps = List.of(
                "Keep the shareable brief focused on one first quote request.",
                "Use the contractor reply to confirm scope before you compare prices or schedule measurements.",
                "If the reply widens the job, go back to the brief and restate the named openings in writing.");
        return new OpeningProtectionBriefNarrative(
                "Opening protection quote-prep brief for " + record.siteLabel(),
                customerSummary,
                requestLine,
                openingsLine,
                scopeLaneLabel,
                scopeLaneSummary,
                sendState,
                sendStateLabel,
                sendStateSummary,
                nextAction,
                nextReason,
                List.copyOf(missing),
                List.copyOf(homeownerReplyChecklist),
                homeownerReplyExample,
                List.copyOf(watchouts),
                List.copyOf(scenarioModules),
                customerSteps,
                followUpSteps);
    }

    public OpeningProtectionBriefMetrics metricsFor(String briefId) {
        List<String[]> eventRows = readRows(eventsPath);
        return metricsFor(briefId, eventRows);
    }

    public String resultPath(String internalToken) {
        return "/tools/opening-protection/quote-prep-brief/result/" + internalToken + "/";
    }

    public String publicBriefPath(String publicToken) {
        return "/tools/opening-protection/quote-prep-brief/share/" + publicToken + "/";
    }

    public String publicBriefPdfExportPath(String publicToken) {
        return "/tools/opening-protection/quote-prep-brief/share/" + publicToken + "/export/pdf/";
    }

    public String workflowEntryPath() {
        return "/tools/opening-protection/quote-prep-brief/";
    }

    public String improvementGuidePath() {
        return "/improvements/opening-protection/";
    }

    public String quoteChecklistPath() {
        return "/guides/msfh-contractor-quote-checklist/";
    }

    public String briefBuilderPath() {
        return "/tools/opening-protection/quote-prep-brief/build/";
    }

    public String absoluteUrl(String path, String requestBaseUrl) {
        return appProperties.absoluteUrl(path, requestBaseUrl);
    }

    public String outboundSubject(OpeningProtectionBriefRecord record) {
        return "Opening-protection quote request for " + record.siteLabel();
    }

    public String outboundMessage(OpeningProtectionBriefRecord record, String shareUrl) {
        List<String> lines = new ArrayList<>();
        String openingLine = "I already have the MSFH inspection report for " + record.siteLabel()
                + " and I am trying to keep the first opening-protection quote narrow.";
        lines.add("Hi,");
        lines.add(openingLine);
        lines.add("I put the current scope, named openings, and limits in this short brief.");
        lines.add(replyInstruction(record));
        lines.add("Brief link: " + shareUrl);
        return String.join(System.lineSeparator(), lines);
    }

    public String replyGuidance(OpeningProtectionBriefRecord record) {
        return replyInstruction(record);
    }

    private void logEvent(String eventType, String routePath, String detail) {
        LeadEventRequest event = new LeadEventRequest();
        event.setEventType(eventType);
        event.setRoutePath(routePath);
        event.setRouteFamily("quote-prep-brief");
        event.setScenario("opening-protection");
        event.setImprovementType("opening-protection");
        event.setDetail(detail);
        leadStorageService.logEvent(event);
    }

    private long countEvents(List<String[]> rows, String briefId, String eventType) {
        return rows.stream()
                .filter(row -> eventType.equals(column(row, 1)))
                .filter(row -> "quote-prep-brief".equals(column(row, 3)))
                .filter(row -> column(row, 6).contains("briefId=" + briefId))
                .count();
    }

    private OpeningProtectionBriefMetrics metricsFor(String briefId, List<String[]> eventRows) {
        long createdCount = countEvents(eventRows, briefId, "quote_prep_brief_created");
        long resultOpenCount = countEvents(eventRows, briefId, "quote_prep_brief_result_open");
        long publicBriefOpenCount = countEvents(eventRows, briefId, "quote_prep_brief_open");
        long recordOpenCount = countEvents(eventRows, briefId, "quote_prep_brief_record_open");
        long briefCopyCount = countEvents(eventRows, briefId, "quote_prep_brief_copy");
        long sendNoteCopyCount = countEvents(eventRows, briefId, "quote_prep_message_copy");
        long replyNarrowedCount = countEvents(eventRows, briefId, "quote_prep_reply_narrowed");
        long replyReportPageCount = countEvents(eventRows, briefId, "quote_prep_reply_report_page");
        long replyPhotoCount = countEvents(eventRows, briefId, "quote_prep_reply_photos");
        long recordCopyCount = countEvents(eventRows, briefId, "quote_prep_brief_record_copy");
        long recoveryClickCount = countEvents(eventRows, briefId, "quote_prep_brief_recovery_click");
        return new OpeningProtectionBriefMetrics(
                createdCount,
                resultOpenCount,
                publicBriefOpenCount,
                recordOpenCount,
                briefCopyCount,
                sendNoteCopyCount,
                replyNarrowedCount,
                replyReportPageCount,
                replyPhotoCount,
                recordCopyCount,
                recoveryClickCount);
    }

    private OpeningProtectionBriefRecord toRecord(String[] row) {
        return new OpeningProtectionBriefRecord(
                column(row, 0),
                column(row, 1),
                column(row, 2),
                OffsetDateTime.parse(column(row, 3)),
                column(row, 4),
                column(row, 5),
                column(row, 6),
                column(row, 7),
                column(row, 8),
                column(row, 9),
                column(row, 10),
                column(row, 11),
                column(row, 12),
                column(row, 13),
                column(row, 14),
                column(row, 15),
                column(row, 16),
                Boolean.parseBoolean(column(row, 17)),
                Boolean.parseBoolean(column(row, 18)),
                Boolean.parseBoolean(column(row, 19)),
                Boolean.parseBoolean(column(row, 20)),
                Boolean.parseBoolean(column(row, 21)),
                Boolean.parseBoolean(column(row, 22)));
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
                List<String> migratedLines = new ArrayList<>();
                migratedLines.add(header);
                normalizeStoredRows(lines).stream()
                        .map(this::csvRow)
                        .forEach(migratedLines::add);
                Files.write(path, migratedLines, StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to prepare storage file " + path, exception);
        }
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
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            return normalizeStoredRows(lines);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read storage file " + path, exception);
        }
    }

    private String column(String[] row, int index) {
        return index < row.length ? row[index] : "";
    }

    private String normalize(String value, String fallback) {
        if (value == null) {
            return fallback;
        }
        String normalized = value.trim();
        return normalized.isBlank() ? fallback : normalized;
    }

    private List<String[]> normalizeStoredRows(List<String> lines) {
        if (lines.isEmpty()) {
            return List.of();
        }
        int headerColumnCount = lines.getFirst().split(",", -1).length;
        return lines.stream()
                .skip(1)
                .filter(line -> !line.isBlank())
                .map(this::parseCsvLine)
                .map(row -> normalizeStoredRow(row, headerColumnCount))
                .toList();
    }

    private String[] normalizeStoredRow(String[] row, int headerColumnCount) {
        return switch (headerColumnCount) {
            case LEGACY_BRIEF_COLUMN_COUNT -> normalizeLegacyBriefRow(row);
            case EXPANDED_BRIEF_COLUMN_COUNT -> normalizeExpandedBriefRow(row);
            default -> padRow(row, CANONICAL_BRIEF_COLUMN_COUNT);
        };
    }

    private String[] normalizeLegacyBriefRow(String[] row) {
        String[] normalized = padRow(row, LEGACY_BRIEF_COLUMN_COUNT);
        return new String[] {
                normalized[0],
                normalized[1],
                normalized[2],
                normalized[3],
                normalized[4],
                normalized[5],
                normalized[6],
                normalized[7],
                normalized[8],
                "",
                normalized[9],
                normalized[10],
                normalized[11],
                normalized[12],
                normalized[13],
                normalized[14],
                normalized[15],
                normalized[16],
                normalized[17],
                normalized[18],
                "",
                "",
                ""
        };
    }

    private String[] normalizeExpandedBriefRow(String[] row) {
        String[] normalized = padRow(row, EXPANDED_BRIEF_COLUMN_COUNT);
        return new String[] {
                normalized[0],
                normalized[1],
                normalized[2],
                normalized[3],
                normalized[4],
                normalized[5],
                normalized[6],
                normalized[7],
                normalized[8],
                "",
                normalized[9],
                normalized[10],
                normalized[11],
                normalized[12],
                normalized[13],
                normalized[14],
                normalized[15],
                normalized[16],
                normalized[17],
                normalized[18],
                normalized[19],
                normalized[20],
                normalized[21]
        };
    }

    private String[] padRow(String[] row, int columnCount) {
        String[] normalized = new String[columnCount];
        for (int index = 0; index < columnCount; index++) {
            normalized[index] = index < row.length ? row[index] : "";
        }
        return normalized;
    }

    private String joinWithCommas(List<String> items) {
        if (items.size() == 1) {
            return items.getFirst();
        }
        if (items.size() == 2) {
            return items.get(0) + " and " + items.get(1);
        }
        return String.join(", ", items.subList(0, items.size() - 1))
                + ", and " + items.getLast();
    }

    private String normalizeScopeLane(String value) {
        String normalized = normalize(value, "").toLowerCase();
        return switch (normalized) {
            case "windows", "shutters", "doors", "mixed", "broad" -> normalized;
            default -> "";
        };
    }

    private boolean validHomeType(String value) {
        String normalized = normalize(value, "");
        return "detached".equals(normalized) || "attached".equals(normalized);
    }

    private boolean scopeLaneNeedsNarrowing(String scopeLane) {
        return scopeLane.isBlank() || "broad".equals(scopeLane);
    }

    private String scopeLaneLabel(String scopeLane) {
        return switch (scopeLane) {
            case "windows" -> "Windows only";
            case "shutters" -> "Shutters only";
            case "doors" -> "Doors or garage-door opening protection only";
            case "mixed" -> "A small mixed openings quote";
            case "broad" -> "Still too broad or unclear";
            default -> "Still needs a narrower focus";
        };
    }

    private String scopeLaneSummary(OpeningProtectionBriefRecord record) {
        return switch (record.scopeLane()) {
            case "windows" -> "This first quote should stay focused on windows only unless the scope is restated more broadly in writing.";
            case "shutters" -> "This first quote should stay focused on shutters only unless the scope is restated more broadly in writing.";
            case "doors" -> "This first quote should stay focused on doors or garage-door opening protection only unless the scope is restated more broadly in writing.";
            case "mixed" -> "A small mixed openings quote is in bounds, but it should stay narrower than a broad whole-house package.";
            case "broad" -> "The request is still at a broad or unclear package level, so narrow this to windows, shutters, doors, or a small mixed openings quote before pricing.";
            default -> "This brief still needs to name whether the first quote is for windows, shutters, doors, or a small mixed openings quote.";
        };
    }

    private String scopeLaneReplyPhrase(String scopeLane) {
        return switch (scopeLane) {
            case "windows" -> "windows only";
            case "shutters" -> "shutters only";
            case "doors" -> "doors or garage-door opening protection only";
            case "mixed" -> "a small mixed openings quote";
            default -> "the narrowest first quote focus";
        };
    }

    private String openingsReplyPhrase(OpeningProtectionBriefRecord record) {
        return record.scopeOpenings().isBlank()
                ? "the exact openings you want quoted first"
                : record.scopeOpenings();
    }

    private String replyExample(OpeningProtectionBriefRecord record) {
        if (scopeLaneNeedsNarrowing(record.scopeLane())) {
            return "I can review this, but please send the report page, clear opening photos, and the narrowest first quote focus for "
                    + openingsReplyPhrase(record)
                    + ". I am not quoting a broad whole-house package yet.";
        }
        return "I can keep this "
                + scopeLaneReplyPhrase(record.scopeLane())
                + " for "
                + openingsReplyPhrase(record)
                + ". Please tell me if the report page or clearer opening photos are still needed before pricing.";
    }

    private String defaultExcludedOpenings(OpeningProtectionBriefRecord record) {
        if (!record.boundaryScopeNote().isBlank()) {
            return record.boundaryScopeNote();
        }
        if (record.scopeOpenings().isBlank()) {
            return "Any window or door not named for this first quote, plus broader whole-house work, unrelated roof work, and non-opening items.";
        }
        return "Any window or door not named for this first quote, plus broader whole-house work, unrelated roof work, and non-opening items outside "
                + record.scopeOpenings() + ".";
    }

    private String defaultPermitHandling(OpeningProtectionBriefRecord record) {
        if (!record.permitHandlingNote().isBlank()) {
            return record.permitHandlingNote();
        }
        return "Permit and inspection handling still need to be confirmed before signing.";
    }

    private String replyInstruction(OpeningProtectionBriefRecord record) {
        if (!record.replyInstructions().isBlank()) {
            return sentence(record.replyInstructions());
        }
        List<String> requestItems = new ArrayList<>();
        requestItems.add(record.reportPageReceived()
                ? "whether the report page already shared is enough for this first quote"
                : "whether you need the report page that shows the opening-protection recommendation");
        requestItems.add(record.photosReceived()
                ? "whether the photos already shared are enough for pricing"
                : "whether you need clear photos of the openings before pricing");
        if (scopeLaneNeedsNarrowing(record.scopeLane())) {
            requestItems.add("what the narrowest first quote focus should be");
            return "Please reply on 3 points: " + joinWithCommas(requestItems) + ".";
        }
        if (record.countyZip().isBlank()
                || record.recommendationLine().isBlank()
                || record.scopeOpenings().isBlank()) {
            requestItems.add("whether the first quote can stay "
                    + scopeLaneReplyPhrase(record.scopeLane())
                    + " for "
                    + openingsReplyPhrase(record));
            return "Please reply on 3 points: " + joinWithCommas(requestItems) + ".";
        }
        return "Please reply on 3 points: "
                + joinWithCommas(List.of(
                        record.reportPageReceived()
                                ? "whether the report page already shared is enough for this first quote"
                                : "whether you need the report page that shows the opening-protection recommendation",
                        record.photosReceived()
                                ? "whether the photos already shared are enough for pricing"
                                : "whether you need clear photos of the openings before pricing",
                        "whether the first quote can stay "
                + scopeLaneReplyPhrase(record.scopeLane())
                + " for "
                + openingsReplyPhrase(record)))
                + ".";
    }

    private String homeownerReportReplyItem(OpeningProtectionBriefRecord record) {
        return record.reportPageReceived()
                ? "Confirm the report page already shared matches this first quote, or say if you need the correct page."
                : "Say whether you need the report page that shows the opening-protection recommendation.";
    }

    private String homeownerPhotosReplyItem(OpeningProtectionBriefRecord record) {
        return record.photosReceived()
                ? "Confirm the listed openings match the photos already shared, or say if clearer photos are still needed."
                : "Say whether clear photos of the openings are still needed before pricing.";
    }

    private String sentence(String value) {
        if (value == null) {
            return "";
        }
        String normalized = value.trim();
        if (normalized.isEmpty()) {
            return normalized;
        }
        char lastCharacter = normalized.charAt(normalized.length() - 1);
        if (lastCharacter == '.' || lastCharacter == '!' || lastCharacter == '?') {
            return normalized;
        }
        return normalized + ".";
    }

    private String csvRow(String... values) {
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < values.length; index++) {
            if (index > 0) {
                builder.append(',');
            }
            builder.append(csvValue(values[index]));
        }
        return builder.toString();
    }

    private String csvValue(String value) {
        String normalized = value == null ? "" : value;
        return "\"" + normalized.replace("\"", "\"\"") + "\"";
    }

    private String[] parseCsvLine(String line) {
        List<String> columns = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int index = 0; index < line.length(); index++) {
            char character = line.charAt(index);
            if (character == '"') {
                if (inQuotes && index + 1 < line.length() && line.charAt(index + 1) == '"') {
                    current.append('"');
                    index++;
                    continue;
                }
                inQuotes = !inQuotes;
                continue;
            }
            if (character == ',' && !inQuotes) {
                columns.add(current.toString());
                current.setLength(0);
                continue;
            }
            current.append(character);
        }

        columns.add(current.toString());
        return columns.toArray(String[]::new);
    }

    private String sanitize(String value) {
        if (value == null) {
            return "";
        }
        String sanitized = value.replace("\r", " ").replace("\n", " ").trim();
        if (sanitized.isEmpty()) {
            return sanitized;
        }
        char firstCharacter = sanitized.charAt(0);
        if (firstCharacter == '=' || firstCharacter == '+' || firstCharacter == '-'
                || firstCharacter == '@') {
            return "'" + sanitized;
        }
        return sanitized;
    }

}
