package owner.mysafefloridahome;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import owner.mysafefloridahome.data.ContentRepository;
import owner.mysafefloridahome.data.RouteHealthRecord;
import owner.mysafefloridahome.data.RouteRecord;
import owner.mysafefloridahome.data.RouteStatusRecord;
import owner.mysafefloridahome.data.SourceRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest(properties = {
        "app.admin.username=admin",
        "app.admin.password=test-admin-password"
})
@AutoConfigureMockMvc
class SiteControllerTests {

    private static final String ADMIN_AUTH = "Basic "
            + Base64.getEncoder().encodeToString("admin:test-admin-password".getBytes(java.nio.charset.StandardCharsets.UTF_8));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContentRepository contentRepository;

    @Test
    void homePageRendersCoreDecisionCopy() throws Exception {
        String html = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(html).contains("Route your next step");
        assertThat(html).contains("Report decision guide");
        assertThat(html).contains("Interpret the recommendation");
        assertThat(html).contains("/program/inspection-report/");
        assertThat(html).contains("Show my next step");
        assertThat(html).contains("Where are you right now?");
        assertThat(html).contains("Use the report, the home type, and the recommendation together");
        assertThat(html).doesNotContain("Start with opening protection before a roof-heavy quote");
        assertThat(html).contains("Official source stack");
        assertThat(html).contains("Virtual editorial team");
    }

    @Test
    void homeDecisionToolBranchesAttachedHomesIntoOpeningProtection() throws Exception {
        String html = mockMvc.perform(get("/")
                        .param("reportState", "received-recommendation")
                        .param("recommendationType", "roof-to-wall")
                        .param("homeType", "attached")
                        .param("priority", "choose-project"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(html).contains("Start with opening protection before a roof-heavy quote");
        assertThat(html).contains("Window or shutter contractor");
        assertThat(html).contains("/improvements/opening-protection/");
        assertThat(html).contains("Source verification");
        assertThat(html).contains("Attached homes treated as townhouses can be limited to opening-protection-only funding.");
    }

    @Test
    void homeDecisionToolHandlesNoRecommendationsAsSupportState() throws Exception {
        String html = mockMvc.perform(get("/")
                        .param("reportState", "received-no-recommendations")
                        .param("recommendationType", "not-sure")
                        .param("homeType", "detached")
                        .param("priority", "choose-project"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(html).contains("No recommended improvements is the decision right now");
        assertThat(html).contains("No contractor type yet");
        assertThat(html).contains("/program/no-recommended-improvements/");
        assertThat(html).contains("Without recommended improvements the grant path cannot proceed.");
    }

    @Test
    void programRouteRendersCanonicalAndIndexMeta() throws Exception {
        String html = mockMvc.perform(get("/program/inspection-report/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(html).contains("How to read an MSFH inspection report before you choose a project");
        assertThat(html).contains("meta name=\"robots\" content=\"index,follow\"");
        assertThat(html).contains("link rel=\"canonical\" href=\"http://localhost/program/inspection-report/\"");
        assertThat(html).contains("\"item\": \"http:\\/\\/localhost\\/program\\/\"");
        assertThat(html).contains("\"@type\": \"Organization\"");
    }

    @Test
    void heldSupportRouteRendersNoindex() throws Exception {
        String html = mockMvc.perform(get("/program/group-5/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(html).contains("meta name=\"robots\" content=\"noindex,nofollow\"");
    }

    @Test
    void robotsAndSitemapExposeOnlyPublicRoutes() throws Exception {
        mockMvc.perform(get("/robots.txt"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Disallow: /api/")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Sitemap: http://localhost/sitemap.xml")));

        String sitemap = mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(sitemap).contains("http://localhost/program/inspection-report/");
        assertThat(sitemap).doesNotContain("http://localhost/program/group-5/");
    }

    @Test
    void guideAndTrustPagesExposeDecisionBlocks() throws Exception {
        String guide = mockMvc.perform(get("/guides/msfh-contractor-quote-checklist/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(guide).contains("What not to assume");
        assertThat(guide).contains("Official source stack");
        assertThat(guide).contains("Get the signing checklist");

        String trust = mockMvc.perform(get("/methodology/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(trust).contains("Quick answer");
        assertThat(trust).contains("Official source stack");
        assertThat(trust).contains("See what the program may pay for");

        String contact = mockMvc.perform(get("/contact/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(contact).contains("Request a route-specific pilot review");
        assertThat(contact).contains("$390 for 30 days");
        assertThat(contact).contains("Licensed Florida contractors only.");
        assertThat(contact).contains("Request pilot review");
    }

    @Test
    void familyHubPagesExposeAcceptanceBlocks() throws Exception {
        String programHub = mockMvc.perform(get("/program/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(programHub).contains("Core decision routes");
        assertThat(programHub).contains("What not to assume");
        assertThat(programHub).contains("Official source stack");

        String improvementHub = mockMvc.perform(get("/improvements/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(improvementHub).contains("Improvement routes");
        assertThat(improvementHub).contains("Official source stack");
    }

    @Test
    void uncertainRecommendationNeverJumpsStraightToQuotePath() throws Exception {
        String html = mockMvc.perform(get("/")
                        .param("reportState", "received-recommendation")
                        .param("recommendationType", "not-sure")
                        .param("homeType", "detached")
                        .param("priority", "compare-quotes"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(html).contains("Clarify the recommendation before you compare contractors");
        assertThat(html).contains("Read the inspection report first");
        assertThat(html).doesNotContain("Get the contractor quote checklist");
    }

    @Test
    void everyPublicRouteRendersAcceptanceBlocks() throws Exception {
        List<RouteRecord> publicRoutes = contentRepository.routes().stream()
                .filter(route -> !"admin".equals(route.family()))
                .toList();

        for (RouteRecord route : publicRoutes) {
            String html = mockMvc.perform(get(route.path()))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThat(html).as(route.path()).contains("Quick answer");
            assertThat(html).as(route.path()).contains("What not to assume");
            assertThat(html).as(route.path()).contains("Official source stack");
            assertThat(html).as(route.path()).contains("class=\"primary-button\"");
        }
    }

    @Test
    void everyGuideRouteIncludesProgramEntryPath() throws Exception {
        List<RouteRecord> guides = contentRepository.routes().stream()
                .filter(route -> "guide".equals(route.family()))
                .toList();

        for (RouteRecord route : guides) {
            String html = mockMvc.perform(get(route.path()))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThat(html).as(route.path()).contains("/program/");
        }
    }

    @Test
    void routeMetadataMatchesIndexStatusAcrossPublicRoutes() throws Exception {
        List<RouteRecord> publicRoutes = contentRepository.routes().stream()
                .filter(route -> !"admin".equals(route.family()))
                .toList();

        for (RouteRecord route : publicRoutes) {
            String html = mockMvc.perform(get(route.path()))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThat(html).as(route.path())
                    .contains("link rel=\"canonical\" href=\"http://localhost" + route.path() + "\"");
            if ("index".equals(route.indexStatus())) {
                assertThat(html).as(route.path()).contains("meta name=\"robots\" content=\"index,follow\"");
            } else {
                assertThat(html).as(route.path()).contains("meta name=\"robots\" content=\"noindex,nofollow\"");
            }
        }
    }

    @Test
    void publicRoutesDoNotClaimGrantCertaintyOrOfficialAffiliation() throws Exception {
        List<RouteRecord> publicRoutes = contentRepository.routes().stream()
                .filter(route -> !"admin".equals(route.family()))
                .toList();
        List<String> bannedPhrases = List.of(
                "guaranteed approval",
                "reimbursement is guaranteed",
                "approval is locked in",
                "approval is secure",
                "official government site",
                "official program site",
                "we approve grants",
                "we can approve");

        for (RouteRecord route : publicRoutes) {
            String html = mockMvc.perform(get(route.path()))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString()
                    .toLowerCase();

            for (String phrase : bannedPhrases) {
                assertThat(html).as(route.path() + " banned phrase: " + phrase).doesNotContain(phrase);
            }
        }
    }

    @Test
    void coreProgramAndImprovementRoutesStayEmailFirst() throws Exception {
        List<RouteRecord> leadRoutes = contentRepository.routes().stream()
                .filter(route -> "phase_1_public".equals(route.phase()))
                .filter(route -> "program".equals(route.family()) || "improvement".equals(route.family()))
                .filter(route -> !"/program/".equals(route.path()) && !"/improvements/".equals(route.path()))
                .toList();

        for (RouteRecord route : leadRoutes) {
            String html = mockMvc.perform(get(route.path()))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            int emailIndex = html.indexOf("name=\"email\"");
            int phoneIndex = html.indexOf("name=\"phone\"");

            assertThat(html).as(route.path()).contains("id=\"lead-form\"");
            assertThat(html).as(route.path()).contains("data-lead-form=\"true\"");
            assertThat(emailIndex).as(route.path() + " email field index").isGreaterThan(-1);
            assertThat(phoneIndex).as(route.path() + " phone field index").isGreaterThan(emailIndex);
            assertThat(html).as(route.path()).doesNotContain("name=\"phone\" required");
        }
    }

    @Test
    void adminPageShowsLeadAndRouteReviewSummaries() throws Exception {
        String html = mockMvc.perform(get("/admin/")
                        .header("Authorization", ADMIN_AUTH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(html).contains("Commercial routing output");
        assertThat(html).contains("Which route family is earning action");
        assertThat(html).contains("Current route board");
    }

    @Test
    void adminPageRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/admin/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void staleSourceSupportFailsClosedForHeldRoutes() {
        @SuppressWarnings("unchecked")
        Map<String, SourceRecord> sourceRecords =
                (Map<String, SourceRecord>) ReflectionTestUtils.getField(contentRepository, "sourceRecords");
        SourceRecord original = sourceRecords.get("support-hub");
        sourceRecords.put("support-hub", new SourceRecord(
                original.id(),
                original.title(),
                original.url(),
                original.note(),
                original.verifiedOn(),
                LocalDate.now().minusDays(1)));

        try {
            @SuppressWarnings("unchecked")
            List<RouteHealthRecord> routeHealth =
                    (List<RouteHealthRecord>) ReflectionTestUtils.invokeMethod(contentRepository, "buildRouteHealth");

            RouteHealthRecord heldRoute = routeHealth.stream()
                    .filter(row -> "/program/group-5/".equals(row.route().path()))
                    .findFirst()
                    .orElseThrow();

            assertThat(heldRoute.effectiveSourceFreshnessStatus()).isEqualTo("stale");
            assertThat(heldRoute.effectivePromotionRecommendation()).isEqualTo("hold");
            assertThat(heldRoute.effectiveRecommendationReason())
                    .contains("stale official source support blocks promotion");
        } finally {
            sourceRecords.put("support-hub", original);
        }
    }

    @Test
    void staleSourceFailsClosedForIndexRoutesAndAppearsInAdminReview() throws Exception {
        @SuppressWarnings("unchecked")
        Map<String, SourceRecord> sourceRecords =
                (Map<String, SourceRecord>) ReflectionTestUtils.getField(contentRepository, "sourceRecords");
        @SuppressWarnings("unchecked")
        Map<String, RouteStatusRecord> routeStatusByRouteId =
                (Map<String, RouteStatusRecord>) ReflectionTestUtils.getField(contentRepository, "routeStatusByRouteId");

        SourceRecord originalSource = sourceRecords.get("report-results");
        RouteStatusRecord originalStatus = routeStatusByRouteId.get("program-inspection-report");

        sourceRecords.put("report-results", new SourceRecord(
                originalSource.id(),
                originalSource.title(),
                originalSource.url(),
                originalSource.note(),
                originalSource.verifiedOn(),
                LocalDate.now().minusDays(1)));
        routeStatusByRouteId.put("program-inspection-report", new RouteStatusRecord(
                originalStatus.routeId(),
                originalStatus.routePath(),
                originalStatus.routeFamily(),
                originalStatus.scope(),
                originalStatus.phase(),
                originalStatus.indexStatus(),
                originalStatus.sourceFreshnessStatus(),
                originalStatus.last28DayImpressions(),
                originalStatus.last28DayClicks(),
                originalStatus.last28DayCtr(),
                originalStatus.last28DayCtaClicks(),
                originalStatus.last28DayLeadOpens(),
                originalStatus.last28DayLeadSubmissions(),
                originalStatus.dominantImprovementType(),
                "recommend_promote",
                "synthetic test fixture",
                originalStatus.reviewedOn(),
                originalStatus.nextReviewOn()));

        try {
            RouteHealthRecord route = contentRepository.routeHealth().stream()
                    .filter(row -> "/program/inspection-report/".equals(row.route().path()))
                    .findFirst()
                    .orElseThrow();

            assertThat(route.effectiveSourceFreshnessStatus()).isEqualTo("stale");
            assertThat(route.effectivePromotionRecommendation()).isEqualTo("hold");
            assertThat(route.effectiveRecommendationReason())
                    .contains("stale official source support blocks promotion");

            String html = mockMvc.perform(get("/admin/")
                            .header("Authorization", ADMIN_AUTH))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThat(html).contains("/program/inspection-report/");
            assertThat(html).contains("stale official source support blocks promotion until review is refreshed");
        } finally {
            sourceRecords.put("report-results", originalSource);
            routeStatusByRouteId.put("program-inspection-report", originalStatus);
        }
    }
}
