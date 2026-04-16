package owner.mysafefloridahome.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Repository;

@Repository
public class ContentRepository {

    private final Map<String, SourceRecord> sourceRecords;
    private final ProgramSnapshot programSnapshot;
    private final Map<String, ProgramPageContent> programPages;
    private final Map<String, ImprovementContent> improvements;
    private final Map<String, GuideContent> guides;
    private final Map<String, TrustPageContent> trustPages;
    private final List<RouteRecord> routes;
    private final Map<String, RouteRecord> routeById;
    private final Map<String, RouteRecord> routeByPath;
    private final List<RouteStatusRecord> routeStatuses;
    private final Map<String, RouteStatusRecord> routeStatusByRouteId;
    private final PromotionReview promotionReview;

    public ContentRepository(ObjectMapper objectMapper) throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        this.sourceRecords = loadSourceRecords(objectMapper);
        this.programSnapshot = readJson(objectMapper, "data/normalized/program/current.json", ProgramSnapshot.class);
        this.programPages = readJson(objectMapper, "data/normalized/program/pages.json", ProgramPageSet.class).pages()
                .stream()
                .collect(Collectors.toMap(ProgramPageContent::slug, page -> page, (left, right) -> right,
                        LinkedHashMap::new));
        this.improvements = loadImprovementRecords(objectMapper, resolver);
        this.guides = readJson(objectMapper, "data/normalized/guides/pages.json", GuidePageSet.class).pages()
                .stream()
                .collect(Collectors.toMap(GuideContent::slug, page -> page, (left, right) -> right,
                        LinkedHashMap::new));
        this.trustPages = readJson(objectMapper, "data/normalized/trust/pages.json", TrustPageSet.class).pages()
                .stream()
                .collect(Collectors.toMap(TrustPageContent::slug, page -> page, (left, right) -> right,
                        LinkedHashMap::new));
        this.routes = readJson(objectMapper, "data/derived/routes.json", new TypeReference<List<RouteRecord>>() {
        }).stream().sorted(Comparator.comparing(RouteRecord::path)).toList();
        this.routeById = this.routes.stream()
                .collect(Collectors.toMap(RouteRecord::routeId, route -> route, (left, right) -> right,
                        LinkedHashMap::new));
        this.routeByPath = this.routes.stream()
                .collect(Collectors.toMap(RouteRecord::path, route -> route, (left, right) -> right,
                        LinkedHashMap::new));
        this.routeStatuses = loadRouteStatuses();
        this.routeStatusByRouteId = this.routeStatuses.stream()
                .collect(Collectors.toMap(RouteStatusRecord::routeId, status -> status, (left, right) -> right,
                        LinkedHashMap::new));
        this.promotionReview = readJson(objectMapper, "data/ops/promotion-review.json", PromotionReview.class);
    }

    public ProgramSnapshot programSnapshot() {
        return programSnapshot;
    }

    public Optional<ProgramPageContent> programPage(String slug) {
        return Optional.ofNullable(programPages.get(slug));
    }

    public Optional<ImprovementContent> improvement(String slug) {
        return Optional.ofNullable(improvements.get(slug));
    }

    public Optional<GuideContent> guide(String slug) {
        return Optional.ofNullable(guides.get(slug));
    }

    public Optional<TrustPageContent> trustPage(String slug) {
        return Optional.ofNullable(trustPages.get(slug));
    }

    public List<RouteRecord> routes() {
        return routes;
    }

    public List<RouteRecord> indexableRoutes() {
        return routes.stream().filter(route -> "index".equals(route.indexStatus())).toList();
    }

    public Optional<RouteRecord> routeById(String routeId) {
        return Optional.ofNullable(routeById.get(routeId));
    }

    public Optional<RouteRecord> routeByPath(String path) {
        return Optional.ofNullable(routeByPath.get(path));
    }

    public List<RouteStatusRecord> routeStatuses() {
        return routeStatuses;
    }

    public List<RouteHealthRecord> routeHealth() {
        return buildRouteHealth();
    }

    public List<RouteHealthRecord> staleRouteHealth() {
        return buildRouteHealth().stream()
                .filter(route -> "stale".equals(route.effectiveSourceFreshnessStatus()))
                .toList();
    }

    public PromotionReview promotionReview() {
        return promotionReview;
    }

    public List<SourceRecord> sourcesFor(List<String> sourceIds) {
        return sourceIds.stream()
                .map(sourceRecords::get)
                .filter(Objects::nonNull)
                .toList();
    }

    public Optional<SourceRecord> sourceById(String sourceId) {
        return Optional.ofNullable(sourceRecords.get(sourceId));
    }

    public List<SourceRecord> sourceFreshnessWatchlist() {
        LocalDate today = LocalDate.now();
        return sourceRecords.values().stream()
                .filter(source -> source.nextReviewOn().isBefore(today))
                .sorted(Comparator.comparing(SourceRecord::nextReviewOn))
                .toList();
    }

    public Collection<ProgramPageContent> programPages() {
        return programPages.values();
    }

    public Collection<ImprovementContent> improvements() {
        return improvements.values();
    }

    public Collection<GuideContent> guides() {
        return guides.values();
    }

    public Collection<TrustPageContent> trustPages() {
        return trustPages.values();
    }

    public boolean routeHasFreshSources(String routeId) {
        return routeSourceIds(routeId).stream()
                .map(sourceRecords::get)
                .filter(Objects::nonNull)
                .noneMatch(source -> source.nextReviewOn().isBefore(LocalDate.now()));
    }

    private Map<String, SourceRecord> loadSourceRecords(ObjectMapper objectMapper) throws IOException {
        List<SourceRecord> sources = readJson(objectMapper, "data/normalized/sources/official.json",
                new TypeReference<List<SourceRecord>>() {
                });
        return sources.stream()
                .collect(Collectors.toMap(SourceRecord::id, source -> source, (left, right) -> right,
                        LinkedHashMap::new));
    }

    private Map<String, ImprovementContent> loadImprovementRecords(ObjectMapper objectMapper,
            PathMatchingResourcePatternResolver resolver) throws IOException {
        Map<String, ImprovementContent> records = new LinkedHashMap<>();
        for (Resource resource : resolver.getResources("classpath:data/normalized/improvements/*.json")) {
            ImprovementContent content = readJson(objectMapper, resource, ImprovementContent.class);
            records.put(content.slug(), content);
        }
        return records;
    }

    private List<RouteStatusRecord> loadRouteStatuses() throws IOException {
        List<RouteStatusRecord> statuses = new ArrayList<>();
        Resource resource = new PathMatchingResourcePatternResolver().getResource("classpath:data/ops/route-status.csv");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            if (line == null) {
                return List.of();
            }
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] columns = line.split(",", -1);
                statuses.add(new RouteStatusRecord(
                        columns[0],
                        columns[1],
                        columns[2],
                        columns[3],
                        columns[4],
                        columns[5],
                        columns[6],
                        Integer.parseInt(columns[7]),
                        Integer.parseInt(columns[8]),
                        Double.parseDouble(columns[9]),
                        Integer.parseInt(columns[10]),
                        Integer.parseInt(columns[11]),
                        Integer.parseInt(columns[12]),
                        columns[13],
                        columns[14],
                        columns[15],
                        LocalDate.parse(columns[16]),
                        LocalDate.parse(columns[17])));
            }
        }
        return statuses;
    }

    private List<RouteHealthRecord> buildRouteHealth() {
        return routes.stream()
                .map(route -> {
                    RouteStatusRecord routeStatus = routeStatusByRouteId.get(route.routeId());
                    boolean stale = !routeHasFreshSources(route.routeId());
                    String freshness = stale ? "stale"
                            : routeStatus != null ? routeStatus.sourceFreshnessStatus() : route.sourceFreshnessStatus();
                    String recommendation = routeStatus != null ? routeStatus.promotionRecommendation() : "hold";
                    String reason = routeStatus != null ? routeStatus.recommendationReason() : "route seed without ops review";
                    if (stale) {
                        recommendation = "hold";
                        reason = "stale official source support blocks promotion until review is refreshed";
                    }
                    return new RouteHealthRecord(route, freshness, recommendation, reason);
                })
                .toList();
    }

    private List<String> routeSourceIds(String routeId) {
        if ("home".equals(routeId)) {
            return programSnapshot.sourceIds();
        }
        return programPages.values().stream()
                .filter(page -> routeId.equals(page.routeId()))
                .map(ProgramPageContent::sourceIds)
                .findFirst()
                .or(() -> improvements.values().stream()
                        .filter(page -> routeId.equals(page.routeId()))
                        .map(ImprovementContent::sourceIds)
                        .findFirst())
                .or(() -> guides.values().stream()
                        .filter(page -> routeId.equals(page.routeId()))
                        .map(GuideContent::sourceIds)
                        .findFirst())
                .or(() -> trustPages.values().stream()
                        .filter(page -> routeId.equals(page.routeId()))
                        .map(TrustPageContent::sourceIds)
                        .findFirst())
                .orElse(List.of());
    }

    private static <T> T readJson(ObjectMapper objectMapper, String classpathLocation, Class<T> type) throws IOException {
        Resource resource = new PathMatchingResourcePatternResolver().getResource("classpath:" + classpathLocation);
        return readJson(objectMapper, resource, type);
    }

    private static <T> T readJson(ObjectMapper objectMapper, String classpathLocation, TypeReference<T> type)
            throws IOException {
        Resource resource = new PathMatchingResourcePatternResolver().getResource("classpath:" + classpathLocation);
        return readJson(objectMapper, resource, type);
    }

    private static <T> T readJson(ObjectMapper objectMapper, Resource resource, Class<T> type) throws IOException {
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, type);
        }
    }

    private static <T> T readJson(ObjectMapper objectMapper, Resource resource, TypeReference<T> type)
            throws IOException {
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, type);
        }
    }
}
