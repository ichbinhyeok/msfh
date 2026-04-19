package owner.mysafefloridahome;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest(properties = {
        "app.admin.username=admin",
        "app.admin.password=test-admin-password",
        "app.storage.leads-path=target/test-storage/leads.csv",
        "app.storage.events-path=target/test-storage/lead_events.csv",
        "app.storage.partner-inquiries-path=target/test-storage/partner_inquiries.csv",
        "app.storage.vendor-handoffs-path=target/test-storage/opening_protection_handoffs.csv",
        "app.storage.vendor-presets-path=target/test-storage/opening_protection_office_presets.csv"
})
@AutoConfigureMockMvc
class SiteControllerTests {

    private static final String ADMIN_AUTH = "Basic "
            + Base64.getEncoder().encodeToString("admin:test-admin-password".getBytes(java.nio.charset.StandardCharsets.UTF_8));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private AppProperties appProperties;

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
        assertThat(html).contains("Most homeowners start here");
        assertThat(html).contains("Secondary paths");
        assertThat(html).contains("Use the report, the home type, and the recommendation together");
        assertThat(html).contains("Open the Quote-Prep Brief");
        assertThat(html).contains("/tools/opening-protection/quote-prep-brief/");
        assertThat(html).contains("https://www.googletagmanager.com/gtag/js?id=G-1C3Q631V0G");
        assertThat(html).contains("gtag('config', 'G-1C3Q631V0G', { send_page_view: false });");
        assertThat(html).doesNotContain("Start with opening protection before a roof-heavy quote");
        assertThat(html).contains("Official source stack");
        assertThat(html).contains("Why this page is careful");
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
    void contractorQuotesRouteLinksIntoOpeningProtectionQuotePrep() throws Exception {
        String html = mockMvc.perform(get("/program/contractor-quotes/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(html).contains("When the quote is really about openings, lock the first request before you compare price");
        assertThat(html).contains("Open the Quote-Prep Brief");
        assertThat(html).contains("/tools/opening-protection/quote-prep-brief/");
        assertThat(html).contains("Read the Opening-Protection Route");
        assertThat(html).contains("/improvements/opening-protection/");
    }

    @Test
    void heldSupportRoutesRenderNoindex() throws Exception {
        String group5 = mockMvc.perform(get("/program/group-5/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(group5).contains("meta name=\"robots\" content=\"noindex,nofollow\"");

        String noRecommendations = mockMvc.perform(get("/program/no-recommended-improvements/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(noRecommendations).contains("meta name=\"robots\" content=\"noindex,nofollow\"");
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
        assertThat(sitemap).doesNotContain("http://localhost/program/no-recommended-improvements/");
        assertThat(sitemap).doesNotContain("http://localhost/program/group-5/");
    }

    @Test
    void configuredBaseUrlOverridesRequestHostForCanonicalMetadata() throws Exception {
        String originalBaseUrl = appProperties.getBaseUrl();
        appProperties.setBaseUrl("https://scopeverdict.com");
        try {
            String html = mockMvc.perform(get("/program/inspection-report/"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThat(html).contains("link rel=\"canonical\" href=\"https://scopeverdict.com/program/inspection-report/\"");
            assertThat(html).contains("\"item\": \"https:\\/\\/scopeverdict.com\\/program\\/\"");
            assertThat(html).contains("\"url\": \"https:\\/\\/scopeverdict.com\\/\"");

            mockMvc.perform(get("/robots.txt"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(org.hamcrest.Matchers.containsString("Sitemap: https://scopeverdict.com/sitemap.xml")));

            String sitemap = mockMvc.perform(get("/sitemap.xml"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            assertThat(sitemap).contains("https://scopeverdict.com/program/inspection-report/");
            assertThat(sitemap).doesNotContain("http://localhost/program/inspection-report/");
        } finally {
            appProperties.setBaseUrl(originalBaseUrl);
        }
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
        assertThat(guide).contains("Questions that should have clean answers on a real quote");

        String trust = mockMvc.perform(get("/methodology/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(trust).contains("Quick answer");
        assertThat(trust).contains("Official source stack");
        assertThat(trust).contains("See what the program may pay for");
        assertThat(trust).contains("This site checks current official MSFH pages first");

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

        String guideHub = mockMvc.perform(get("/guides/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(guideHub).contains("Core decision guides");
        assertThat(guideHub).contains("Support and closeout guides");
        assertThat(guideHub).contains("/guides/msfh-contractor-quote-checklist/");
        assertThat(guideHub).contains("/guides/msfh-rfi-response-checklist/");
    }

    @Test
    void vendorPacketRoutesRenderNoindexPrintableAssets() throws Exception {
        String workflow = mockMvc.perform(get("/tools/opening-protection/quote-prep-brief/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(workflow).contains("Build the opening-protection quote-prep brief before you ask contractors for price");
        assertThat(workflow).contains("A clean first quote request has five anchors");
        assertThat(workflow).contains("meta name=\"robots\" content=\"noindex,nofollow\"");
        assertThat(workflow).contains("/tools/opening-protection/quote-prep-brief/build/");
        assertThat(workflow).doesNotContain("/vendor-packets/opening-protection/estimator-handoff/");
        assertThat(workflow).doesNotContain("/vendor-packets/opening-protection/quote-boundary/");
        assertThat(workflow).doesNotContain("/vendor-packets/opening-protection/office-preset/");
        assertThat(workflow).doesNotContain("Recent first sends on this browser only");

        String preQuote = mockMvc.perform(get("/tools/opening-protection/quote-prep-brief/build/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(preQuote).contains("Build the quote-prep brief before you ask for price");
        assertThat(preQuote).contains("meta name=\"robots\" content=\"noindex,nofollow\"");
        assertThat(preQuote).contains("Build your shareable brief");
        assertThat(preQuote).contains("Create Quote-Prep Brief");
        assertThat(preQuote).contains("Specific openings inside the first quote");
        assertThat(preQuote).contains("Current quote focus");
        assertThat(preQuote).contains("Add this only if a contractor already asked for it");
        assertThat(preQuote).contains("Contractor label (optional)");
        assertThat(preQuote).contains("Contact name (optional)");
        assertThat(preQuote).contains("Shareable brief preview");
        assertThat(preQuote).contains("A useful reply should stay inside 3 things");
        assertThat(preQuote).contains("Confirm the first quote can stay inside this focus and these openings");
        assertThat(preQuote).contains("I want to compare contractors against the same narrow scope");
        assertThat(preQuote).contains("I am worried someone will assume approval or reimbursement is already settled");
        assertThat(preQuote).contains("HOA or condo review may still matter before scope is final");
        assertThat(preQuote).doesNotContain("Who this should come from");
        assertThat(preQuote).doesNotContain("Office name on the brief");
        assertThat(preQuote).doesNotContain("Sender name on the brief");
        assertThat(preQuote).doesNotContain("Public brief URL");
        assertThat(preQuote).doesNotContain("Copy Brief Link");
        assertThat(preQuote).doesNotContain("Copy Send Note");
        assertThat(preQuote).doesNotContain("Open Public Brief");
        assertThat(preQuote).doesNotContain("Open PDF Export");
        assertThat(preQuote).doesNotContain("Saved office presets");
        assertThat(preQuote).doesNotContain("Recent real first sends");
        assertThat(preQuote).doesNotContain("Manage Presets");
        assertThat(preQuote).doesNotContain("Wording carried forward");
        assertThat(preQuote).doesNotContain("Sendable pre-quote scope brief");
        assertThat(preQuote).doesNotContain("Copy Pre-Quote Scope Brief");
        assertThat(preQuote).doesNotContain("value=\"Main property\"");
        assertThat(preQuote).doesNotContain("name=\"reportPageReceived\" checked");
        assertThat(preQuote).doesNotContain("name=\"photosReceived\" checked");
        assertThat(preQuote).doesNotContain("/vendor-packets/opening-protection/estimator-handoff/");
        assertThat(preQuote).doesNotContain("/vendor-packets/opening-protection/quote-boundary/");
        assertThat(preQuote).doesNotContain("After the homeowner replies, use the next document that fits");
        assertThat(preQuote.indexOf("Shareable brief preview"))
                .isLessThan(preQuote.indexOf("A useful reply should stay inside 3 things"));

        String estimator = mockMvc.perform(get("/vendor-packets/opening-protection/estimator-handoff/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(estimator).contains("Estimator handoff after the first brief");
        assertThat(estimator).contains("Internal review");
        assertThat(estimator).contains("Estimator handoff worksheet");
        assertThat(estimator).contains("Relevant openings are explicitly listed");
        assertThat(estimator).contains("Copy Summary");

        String boundary = mockMvc.perform(get("/vendor-packets/opening-protection/quote-boundary/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(boundary).contains("Front-page quote boundary note");
        assertThat(boundary).contains("Quote boundary note");
        assertThat(boundary).contains("Copy Boundary Note");

        String preset = mockMvc.perform(get("/vendor-packets/opening-protection/office-preset/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(preset).contains("Optional office setup after repeat sends");
        assertThat(preset).contains("Save Office Preset");
        assertThat(preset).contains("Copy Brief Setup URL");
        assertThat(preset).doesNotContain("Copy Boundary Preset URL");
        assertThat(preset).doesNotContain("Open Boundary");
        assertThat(preset).doesNotContain("Office Record");
    }

    @Test
    void openingProtectionHandoffRejectsThinFirstSend() throws Exception {
        String location = mockMvc.perform(post("/tools/opening-protection/quote-prep-brief/create")
                        .param("siteLabel", "Thin Scope home")
                        .param("homeType", "detached")
                        .param("officeLabel", "Thin Scope Desk"))
                .andExpect(status().is3xxRedirection())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        assertThat(location).startsWith("/tools/opening-protection/quote-prep-brief/build/?");
        assertThat(location).contains("blockingError=missing_required_scope");
        assertThat(location).contains("siteLabel=Thin%20Scope%20home");
        assertThat(location).contains("homeType=detached");

        String html = mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(html).contains("Cannot create the shareable brief yet");
        assertThat(html).contains("Confirm the report recommendation, home type, and first quote focus or exact openings first.");
    }

    @Test
    void openingProtectionHandoffAllowsBlankSenderIdentityWhenScopeIsReady() throws Exception {
        String location = mockMvc.perform(post("/tools/opening-protection/quote-prep-brief/create")
                        .param("siteLabel", "No sender home")
                        .param("countyZip", "Miami-Dade 33176")
                        .param("homeType", "detached")
                        .param("scopeLane", "windows")
                        .param("recommendationLine", "Opening protection recommendation from the report")
                        .param("scopeOpenings", "front windows only")
                        .param("reportPageReceived", "true")
                        .param("photosReceived", "true"))
                .andExpect(status().is3xxRedirection())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        assertThat(location).startsWith("/tools/opening-protection/quote-prep-brief/result/");

        String html = mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(html).contains("Homeowner-prepared brief");
        assertThat(html).contains("Open Shareable Brief");
        assertThat(html).doesNotContain("blockingError=missing_required_scope");
    }

    @Test
    void officePresetCanBeSavedAndReloaded() throws Exception {
        String location = mockMvc.perform(post("/vendor-presets/opening-protection/save")
                        .param("presetName", "Palm Coast default")
                        .param("officeLabel", "Palm Coast Openings Desk")
                        .param("senderName", "Amanda from intake")
                        .param("replyInstructions", "Reply with the report page and opening photos in one email before scheduling.")
                        .param("serviceAreaNote", "Serving Flagler only.")
                        .param("permitHandlingNote", "Permit filing is quoted separately unless stated in writing.")
                        .param("attachedScopeNote", "Attached openings are reviewed separately and may require HOA approval.")
                        .param("boundaryScopeNote", "Only listed openings are included in this quote-ready packet."))
                .andExpect(status().is3xxRedirection())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        assertThat(location).startsWith("/vendor-packets/opening-protection/office-preset/?preset=");
        assertThat(location).contains("saved=1");

        String presetHtml = mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(presetHtml).contains("Office preset saved. Reuse it only after the public brief starts repeating.");
        assertThat(presetHtml).contains("Palm Coast default");
        assertThat(presetHtml).contains("Update Office Preset");
        assertThat(presetHtml).contains("Editing Now");
        assertThat(presetHtml).contains("Open Quote-Prep Brief");
        assertThat(presetHtml).doesNotContain("Open Boundary");
    }

    @Test
    void preQuotePageKeepsFreeBuilderFocusedOnFirstSend() throws Exception {
        mockMvc.perform(post("/vendor-presets/opening-protection/save")
                        .param("presetName", "Inline preset")
                        .param("officeLabel", "Inline Openings Desk")
                        .param("senderName", "Morgan from intake")
                        .param("replyInstructions", "Reply with the report page and opening photos in one message.")
                        .param("boundaryScopeNote", "Only listed openings belong in the first quote."))
                .andExpect(status().is3xxRedirection());

        String html = mockMvc.perform(get("/tools/opening-protection/quote-prep-brief/build/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(html).contains("Shareable brief preview");
        assertThat(html).contains("A useful reply should stay inside 3 things");
        assertThat(html).doesNotContain("Load office wording without leaving this builder");
        assertThat(html).doesNotContain("Use on This Brief");
        assertThat(html).doesNotContain("Manage Presets");
        assertThat(html).doesNotContain("Inline preset");
        assertThat(html).doesNotContain("Reuse wording from a real first send");
        assertThat(html).doesNotContain("Use Last Send Wording");
    }

    @Test
    void officePresetCanBeUpdatedAndDeleted() throws Exception {
        String saveLocation = mockMvc.perform(post("/vendor-presets/opening-protection/save")
                        .param("presetName", "Treasure Coast default")
                        .param("officeLabel", "Treasure Coast Intake Desk")
                        .param("senderName", "Chris from intake")
                        .param("replyInstructions", "Reply with the report page first.")
                        .param("serviceAreaNote", "Serving Martin County only.")
                        .param("permitHandlingNote", "Permit handling must be restated before signing.")
                        .param("attachedScopeNote", "Attached homes stay narrower.")
                        .param("boundaryScopeNote", "Only listed openings stay in scope."))
                .andExpect(status().is3xxRedirection())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        String presetId = extractQueryParam(saveLocation, "preset");

        String updateLocation = mockMvc.perform(post("/vendor-presets/opening-protection/save")
                        .param("presetId", presetId)
                        .param("presetName", "Treasure Coast revised")
                        .param("officeLabel", "Treasure Coast Scope Desk")
                        .param("senderName", "Chris from intake")
                        .param("replyInstructions", "Reply with the report page and the opening photos together.")
                        .param("serviceAreaNote", "Serving Martin and St. Lucie only.")
                        .param("permitHandlingNote", "Permit handling is confirmed separately before signing.")
                        .param("attachedScopeNote", "Attached homes stay narrower.")
                        .param("boundaryScopeNote", "Only listed openings stay in scope."))
                .andExpect(status().is3xxRedirection())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        assertThat(updateLocation).contains("updated=1");
        assertThat(updateLocation).contains("preset=" + presetId);

        String updatedHtml = mockMvc.perform(get(updateLocation))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(updatedHtml).contains("Office preset updated. Use it only if the same public brief wording is repeating.");
        assertThat(updatedHtml).contains("Treasure Coast revised");
        assertThat(updatedHtml).contains("Treasure Coast Scope Desk");
        assertThat(updatedHtml).doesNotContain("Treasure Coast Intake Desk");

        String deleteLocation = mockMvc.perform(post("/vendor-presets/opening-protection/delete")
                        .param("presetId", presetId))
                .andExpect(status().is3xxRedirection())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        assertThat(deleteLocation).contains("deleted=1");

        String deletedHtml = mockMvc.perform(get(deleteLocation))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(deletedHtml).contains("Office preset deleted. Keep setup only where the public brief is actually repeating.");
        assertThat(deletedHtml).doesNotContain("Treasure Coast revised");
    }

    @Test
    void officePresetPageKeepsPresetReuseSeparateFromRecentPacketActivity() throws Exception {
        String presetLocation = mockMvc.perform(post("/vendor-presets/opening-protection/save")
                        .param("presetName", "Broward default")
                        .param("officeLabel", "Broward Openings Desk"))
                .andExpect(status().is3xxRedirection())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        mockMvc.perform(post("/vendor-handoffs/opening-protection")
                        .param("siteLabel", "Broward packet home")
                        .param("countyZip", "Broward 33301")
                        .param("homeType", "detached")
                        .param("scopeLane", "windows")
                        .param("recommendationLine", "Opening protection recommendation")
                        .param("scopeOpenings", "front windows")
                        .param("officeLabel", "Broward Openings Desk")
                        .param("senderName", "Bri from intake")
                        .param("reportPageReceived", "true")
                        .param("photosReceived", "true"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(post("/vendor-handoffs/opening-protection")
                        .param("siteLabel", "Unrelated office home")
                        .param("countyZip", "Palm Beach 33480")
                        .param("homeType", "detached")
                        .param("scopeLane", "doors")
                        .param("recommendationLine", "Opening protection recommendation")
                        .param("scopeOpenings", "rear slider")
                        .param("officeLabel", "Palm Beach Openings Desk")
                        .param("senderName", "Pat from intake")
                        .param("reportPageReceived", "true")
                        .param("photosReceived", "true"))
                .andExpect(status().is3xxRedirection());

        String html = mockMvc.perform(get(presetLocation))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(html).contains("Load reusable office wording back into the quote-prep brief");
        assertThat(html).contains("Broward default");
        assertThat(html).contains("Open Quote-Prep Brief");
        assertThat(html).contains("Do not let setup become the visible product");
        assertThat(html).contains("Recent public brief sends");
        assertThat(html).contains("Broward packet home");
        assertThat(html).doesNotContain("Unrelated office home");
        assertThat(html).contains("Result Console");
        assertThat(html).doesNotContain("Office Record");
    }

    @Test
    void vendorPacketsStayOutOfPublicSitemap() throws Exception {
        String sitemap = mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(sitemap).doesNotContain("/tools/opening-protection/quote-prep-brief/");
        assertThat(sitemap).doesNotContain("/tools/opening-protection/quote-prep-brief/build/");
        assertThat(sitemap).doesNotContain("/vendor-packets/opening-protection/pre-quote/");
        assertThat(sitemap).doesNotContain("/vendor-packets/opening-protection/quote-ready-packet/");
        assertThat(sitemap).doesNotContain("/vendor-packets/opening-protection/estimator-handoff/");
        assertThat(sitemap).doesNotContain("/vendor-packets/opening-protection/quote-boundary/");
        assertThat(sitemap).doesNotContain("/vendors/opening-protection/customer-briefs/");
        assertThat(sitemap).doesNotContain("/vendors/opening-protection/quote-ready-packets/");
    }

    @Test
    void openingProtectionHandoffFlowCreatesResultBriefAndOfficeRecord() throws Exception {
        String location = mockMvc.perform(post("/tools/opening-protection/quote-prep-brief/create")
                        .param("siteLabel", "123 Palm Avenue home")
                        .param("countyZip", "Miami-Dade 33176")
                        .param("homeType", "attached")
                        .param("scopeLane", "mixed")
                        .param("recommendationLine", "Opening protection recommendation from the report")
                        .param("scopeOpenings", "front windows and rear slider")
                        .param("officeLabel", "Palm Coast Openings Desk")
                        .param("senderName", "Amanda from intake")
                        .param("replyInstructions", "Reply with the report page and opening photos in one email before scheduling.")
                        .param("serviceAreaNote", "Serving Miami-Dade and Broward only.")
                        .param("permitHandlingNote", "Permit and inspection handling still need to be confirmed before signing.")
                        .param("attachedScopeNote", "Attached-home jobs stay inside the narrower attached-home path until the office restates scope.")
                        .param("boundaryScopeNote", "Anything not named in the packet stays outside the first quote.")
                        .param("compareQuotesRequested", "true")
                        .param("reimbursementAssumed", "true")
                        .param("hoaReviewLikely", "true")
                        .param("reportPageReceived", "true")
                        .param("photosReceived", "true"))
                .andExpect(status().is3xxRedirection())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        assertThat(location).startsWith("/tools/opening-protection/quote-prep-brief/result/");

        String resultHtml = mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(resultHtml).contains("Opening protection quote-prep brief for 123 Palm Avenue home");
        assertThat(resultHtml).contains("Shareable brief link");
        assertThat(resultHtml).contains("Copy Message");
        assertThat(resultHtml).contains("Copy Brief Link");
        assertThat(resultHtml).contains("data-track-event=\"vendor_handoff_brief_copy\"");
        assertThat(resultHtml).contains("data-track-event=\"vendor_handoff_send_note_copy\"");
        assertThat(resultHtml).contains("Open PDF Export");
        assertThat(resultHtml).contains("Prepared by");
        assertThat(resultHtml).contains("What a useful contractor reply should answer");
        assertThat(resultHtml).contains("Open Shareable Brief");
        assertThat(resultHtml).contains("Share signals");
        assertThat(resultHtml).contains("Watch the first public response before you add anything else");
        assertThat(resultHtml).contains("Message copies");
        assertThat(resultHtml).contains("Public brief opens");
        assertThat(resultHtml).contains("Return quality signals");
        assertThat(resultHtml).contains("Log what came back after sharing");
        assertThat(resultHtml).contains("Mark Narrower Response");
        assertThat(resultHtml).contains("Mark Report Page Signal");
        assertThat(resultHtml).contains("Mark Opening Photo Signal");
        assertThat(resultHtml).contains("data-track-event=\"vendor_handoff_reply_narrowed\"");
        assertThat(resultHtml).contains("data-track-event=\"vendor_handoff_reply_report_page\"");
        assertThat(resultHtml).contains("data-track-event=\"vendor_handoff_reply_photos\"");
        assertThat(resultHtml).contains("Keep the next move narrow");
        assertThat(resultHtml).contains("Keep the free layer to the brief, the link, and the reply target.");
        assertThat(resultHtml).doesNotContain("Ready-to-send bundle");
        assertThat(resultHtml).doesNotContain("Copy Subject");
        assertThat(resultHtml).doesNotContain("Copy Full Send Bundle");
        assertThat(resultHtml).contains("/export/pdf/");
        assertThat(resultHtml).doesNotContain("Recent first sends on this browser only");
        assertThat(resultHtml).doesNotContain("data-local-handoff-record");
        assertThat(resultHtml).doesNotContain("/vendor-packets/opening-protection/estimator-handoff/?siteLabel=123%20Palm%20Avenue%20home");
        assertThat(resultHtml).doesNotContain("/vendor-packets/opening-protection/quote-boundary/?productPath=opening%20protection%20work");
        assertThat(resultHtml).doesNotContain("/vendor-packets/opening-protection/office-preset/");
        assertThat(resultHtml).doesNotContain("Save Office Preset");
        assertThat(resultHtml).doesNotContain("Manage Presets");
        assertThat(resultHtml).contains("meta name=\"robots\" content=\"noindex,nofollow\"");
        assertThat(resultHtml.indexOf("Open Shareable Brief"))
                .isLessThan(resultHtml.indexOf("Keep the next move narrow"));
        assertThat(resultHtml.indexOf("Shareable brief link"))
                .isLessThan(resultHtml.indexOf("Keep the next move narrow"));

        String briefPath = extractFirst(resultHtml, "/tools/opening-protection/quote-prep-brief/share/");
        String internalToken = location.substring("/tools/opening-protection/quote-prep-brief/result/".length(), location.length() - 1);
        String recordPath = "/tools/opening-protection/quote-prep-brief/internal/" + internalToken + "/";

        String briefHtml = mockMvc.perform(get(briefPath))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(briefHtml).contains("Prepared by");
        assertThat(briefHtml).contains("Palm Coast Openings Desk");
        assertThat(briefHtml).contains("Reply with the report page and opening photos in one email before scheduling.");
        assertThat(briefHtml).contains("What stays outside this first quote for now");
        assertThat(briefHtml).contains("What to slow down in this quote request");
        assertThat(briefHtml).contains("Keep this first quote inside the attached-home scope");
        assertThat(briefHtml).contains("Compare only the same openings and the same exclusions");
        assertThat(briefHtml).doesNotContain("What we still need from you");
        assertThat(briefHtml).contains("What this brief does not ask you to assume yet");
        assertThat(briefHtml).contains("Permit and inspection handling still need to be confirmed before signing.");
        assertThat(briefHtml).contains("First quote focus");
        assertThat(briefHtml).contains("A small mixed openings quote");
        assertThat(briefHtml).contains("This brief helps narrow the first quote. It does not approve work, confirm reimbursement, or replace official program instructions.");
        assertThat(briefHtml).contains("Please answer these 3 points");
        assertThat(briefHtml).contains("Example reply:");
        assertThat(briefHtml).contains("Case ");
        assertThat(briefHtml).doesNotContain("Want a second check?");
        assertThat(briefHtml).doesNotContain("Use the next link that fits this exact situation");
        assertThat(briefHtml).doesNotContain("Contractor quote checklist");
        assertThat(briefHtml).doesNotContain("Vendor workflow");
        assertThat(briefHtml).doesNotContain("Office record");
        assertThat(briefHtml).doesNotContain("Stored intake");
        assertThat(briefHtml).doesNotContain("Keep the next step internal");
        assertThat(briefHtml).doesNotContain("Public brief URL");
        assertThat(briefHtml).doesNotContain("Copy Brief Link");
        assertThat(briefHtml).doesNotContain("Copy Send Note");
        assertThat(briefHtml).doesNotContain("Open Tool");
        assertThat(briefHtml).doesNotContain("Methodology");
        assertThat(briefHtml).doesNotContain("Not government affiliated");
        assertThat(briefHtml).doesNotContain("/vendor-packets/opening-protection/estimator-handoff/");
        assertThat(briefHtml).doesNotContain("/vendor-packets/opening-protection/quote-boundary/");
        assertThat(briefHtml).doesNotContain("/vendor-packets/opening-protection/office-preset/");
        assertThat(briefHtml).contains("Open PDF Export");

        int pdfPathIndex = resultHtml.indexOf("/export/pdf/");
        assertThat(pdfPathIndex).isGreaterThan(-1);
        int pdfPathStart = resultHtml.lastIndexOf("/tools/opening-protection/quote-prep-brief/share/", pdfPathIndex);
        int pdfPathEnd = resultHtml.indexOf("\"", pdfPathIndex);
        assertThat(pdfPathStart).isGreaterThan(-1);
        assertThat(pdfPathEnd).isGreaterThan(pdfPathIndex);
        String pdfPath = resultHtml.substring(pdfPathStart, pdfPathEnd);

        String pdfHtml = mockMvc.perform(get(pdfPath))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(pdfHtml).contains("Print or Save PDF");
        assertThat(pdfHtml).contains("Back to Web Brief");
        assertThat(pdfHtml).contains("Opening protection quote-prep brief");
        assertThat(pdfHtml).contains("Please answer these 3 points");
        assertThat(pdfHtml).doesNotContain("Office record");
        assertThat(pdfHtml).doesNotContain("Stored intake");
        assertThat(pdfHtml).doesNotContain("Keep the next step internal");
        assertThat(pdfHtml).doesNotContain("Public brief URL");
        assertThat(pdfHtml).doesNotContain("Copy Brief Link");
        assertThat(pdfHtml).doesNotContain("Copy Send Note");
        assertThat(pdfHtml).doesNotContain("Open Tool");
        assertThat(pdfHtml).doesNotContain("Methodology");
        assertThat(pdfHtml).doesNotContain("Not government affiliated");
        assertThat(pdfHtml).doesNotContain("/vendor-packets/opening-protection/estimator-handoff/");
        assertThat(pdfHtml).doesNotContain("/vendor-packets/opening-protection/quote-boundary/");
        assertThat(pdfHtml).doesNotContain("/vendor-packets/opening-protection/office-preset/");

        String recordHtml = mockMvc.perform(get(recordPath))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(recordHtml).contains("Office record");
        assertThat(recordHtml).contains("Stored intake");
        assertThat(recordHtml).contains("123 Palm Avenue home");
        assertThat(recordHtml).contains("front windows and rear slider");
        assertThat(recordHtml).contains("Current focus: A small mixed openings quote");
        assertThat(recordHtml).contains("Palm Coast Openings Desk");
        assertThat(recordHtml).contains("Amanda from intake");
        assertThat(recordHtml).contains("Serving Miami-Dade and Broward only.");
        assertThat(recordHtml).contains("Open Prefilled Estimator Handoff");
        assertThat(recordHtml).contains("Open Prefilled Quote Boundary");
    }

    @Test
    void broadOrIncompleteButSendableCaseUsesClarificationState() throws Exception {
        String location = mockMvc.perform(post("/tools/opening-protection/quote-prep-brief/create")
                        .param("siteLabel", "789 Clarify Scope home")
                        .param("countyZip", "Palm Beach 33480")
                        .param("homeType", "detached")
                        .param("scopeLane", "broad")
                        .param("recommendationLine", "Opening protection recommendation from the report")
                        .param("scopeOpenings", "front windows only")
                        .param("officeLabel", "Clarify Scope Desk")
                        .param("senderName", "Casey from intake")
                        .param("reportPageReceived", "true"))
                .andExpect(status().is3xxRedirection())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        String resultHtml = mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(resultHtml).contains("Share as clarification brief");
        assertThat(resultHtml).contains("Use this to narrow the first reply first");
        assertThat(resultHtml).contains("Use this for the clarification pass");
        assertThat(resultHtml).contains("What a useful contractor reply should answer");
        assertThat(resultHtml).contains("the narrowest focus you can quote first");
        assertThat(resultHtml).contains("Confirm the report page already shared matches this first quote, or say if you need the correct page.");
        assertThat(resultHtml).contains("Say whether clear photos of the openings are still needed before pricing.");
        assertThat(resultHtml).contains("Tell me the narrowest focus you can quote first, and whether this request is still too broad.");
        assertThat(resultHtml).doesNotContain("data-local-handoff-record");
    }

    @Test
    void replyQualitySignalsAppearAfterLoggingEvents() throws Exception {
        String location = mockMvc.perform(post("/tools/opening-protection/quote-prep-brief/create")
                        .param("siteLabel", "Reply signal home")
                        .param("countyZip", "Miami-Dade 33176")
                        .param("homeType", "detached")
                        .param("scopeLane", "windows")
                        .param("recommendationLine", "Opening protection recommendation from the report")
                        .param("scopeOpenings", "front windows only")
                        .param("officeLabel", "Reply Signal Desk")
                        .param("senderName", "Dana from intake")
                        .param("reportPageReceived", "true")
                        .param("photosReceived", "true"))
                .andExpect(status().is3xxRedirection())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        String resultHtml = mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String handoffId = extractBetween(resultHtml, "handoffId=", ";surface=result");

        logVendorEvent("vendor_handoff_reply_narrowed", location, handoffId, "reply-narrowed");
        logVendorEvent("vendor_handoff_reply_report_page", location, handoffId, "report-page");
        logVendorEvent("vendor_handoff_reply_photos", location, handoffId, "opening-photos");

        String refreshedHtml = mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(refreshedHtml).contains("data-signal-counter=\"reply-narrowed\">1<");
        assertThat(refreshedHtml).contains("data-signal-counter=\"reply-report-page\">1<");
        assertThat(refreshedHtml).contains("data-signal-counter=\"reply-photos\">1<");
        assertThat(refreshedHtml).contains("A return-quality signal is now logged.");
    }

    @Test
    void cleanHandoffResultPrioritizesEstimatorBeforeBoundary() throws Exception {
        String location = mockMvc.perform(post("/tools/opening-protection/quote-prep-brief/create")
                        .param("siteLabel", "456 Clean Scope home")
                        .param("countyZip", "Broward 33020")
                        .param("homeType", "detached")
                        .param("scopeLane", "doors")
                        .param("recommendationLine", "Opening protection recommendation from the report")
                        .param("scopeOpenings", "front entry door")
                        .param("officeLabel", "Clean Scope Desk")
                        .param("senderName", "Jamie from intake")
                        .param("reportPageReceived", "true")
                        .param("photosReceived", "true"))
                .andExpect(status().is3xxRedirection())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        String resultHtml = mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(resultHtml).contains("Keep the next move narrow");
        assertThat(resultHtml).doesNotContain("Estimator handoff");
        assertThat(resultHtml).doesNotContain("Quote scope boundary sheet");
        assertThat(resultHtml).doesNotContain("Open Office Record");
        assertThat(resultHtml).doesNotContain("Contractor quote checklist");
    }

    @Test
    void openingProtectionImprovementPageLinksToQuotePrepBrief() throws Exception {
        String html = mockMvc.perform(get("/improvements/opening-protection/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(html).contains("Build the opening-protection quote-prep brief");
        assertThat(html).contains("/tools/opening-protection/quote-prep-brief/");
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
    void keyRoutesRenderConcreteChecklistsAndStatusTables() throws Exception {
        String reportRoute = mockMvc.perform(get("/program/inspection-report/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(reportRoute).contains("What to check in the report before you call a contractor");

        String statusGuide = mockMvc.perform(get("/guides/msfh-portal-statuses-explained/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(statusGuide).contains("Translate the label into an actual next move");
        assertThat(statusGuide).contains("Portal wording");

        String closeoutGuide = mockMvc.perform(get("/guides/final-inspection-draw-request-checklist/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(closeoutGuide).contains("What the final-inspection file should include before you submit the draw request");
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

    private String extractFirst(String html, String prefix) {
        int start = html.indexOf(prefix);
        assertThat(start).isNotNegative();
        int end = html.indexOf("\"", start);
        assertThat(end).isGreaterThan(start);
        return html.substring(start, end);
    }

    private String extractQueryParam(String path, String key) {
        String prefix = key + "=";
        int start = path.indexOf(prefix);
        assertThat(start).isNotNegative();
        int valueStart = start + prefix.length();
        int end = path.indexOf("&", valueStart);
        if (end < 0) {
            end = path.length();
        }
        return path.substring(valueStart, end);
    }

    private String extractBetween(String text, String prefix, String suffix) {
        int start = text.indexOf(prefix);
        assertThat(start).isNotNegative();
        int valueStart = start + prefix.length();
        int end = text.indexOf(suffix, valueStart);
        assertThat(end).isGreaterThan(valueStart);
        return text.substring(valueStart, end);
    }

    private void logVendorEvent(String eventType, String routePath, String handoffId, String signal) throws Exception {
        String payload = """
                {
                  "eventType": "%s",
                  "routePath": "%s",
                  "routeFamily": "vendor-handoff",
                  "scenario": "opening-protection",
                  "improvementType": "opening-protection",
                  "detail": "handoffId=%s;surface=result;signal=%s"
                }
                """.formatted(eventType, routePath, handoffId, signal);

        mockMvc.perform(post("/api/leads/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isNoContent());
    }
}
