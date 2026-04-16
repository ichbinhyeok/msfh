package owner.mysafefloridahome;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "app.storage.leads-path=target/test-data/leads.csv",
        "app.storage.events-path=target/test-data/events.csv",
        "app.storage.partner-inquiries-path=target/test-data/partner-inquiries.csv",
        "app.admin.username=admin",
        "app.admin.password=test-admin-password"
})
class LeadControllerTests {

    private static final String ADMIN_AUTH = "Basic "
            + Base64.getEncoder().encodeToString("admin:test-admin-password".getBytes(StandardCharsets.UTF_8));

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void cleanStorage() throws Exception {
        Files.deleteIfExists(Path.of("target/test-data/leads.csv"));
        Files.deleteIfExists(Path.of("target/test-data/events.csv"));
        Files.deleteIfExists(Path.of("target/test-data/partner-inquiries.csv"));
    }

    @Test
    void leadCaptureWritesStorageAndRedirects() throws Exception {
        mockMvc.perform(post("/api/leads/capture")
                        .param("originPath", "/program/inspection-report/")
                        .param("routePath", "/program/inspection-report/")
                        .param("routeFamily", "program")
                        .param("scenario", "report_received_need_next_step")
                        .param("improvementType", "")
                        .param("county", "Miami-Dade")
                        .param("zip", "33101")
                        .param("reportReceived", "yes")
                        .param("homeType", "detached")
                        .param("budgetRange", "5k-10k")
                        .param("timeline", "within-30-days")
                        .param("email", "person@example.com")
                        .param("phone", "")
                        .param("consent", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/program/inspection-report/?lead=success#lead-form"));

        String leads = Files.readString(Path.of("target/test-data/leads.csv"), StandardCharsets.UTF_8);
        String events = Files.readString(Path.of("target/test-data/events.csv"), StandardCharsets.UTF_8);

        assertThat(leads).contains("person@example.com");
        assertThat(leads).contains("needs-manual-review");
        assertThat(events).contains("lead_submit_success");
    }

    @Test
    void improvementTypeChangesPartnerRouting() throws Exception {
        mockMvc.perform(post("/api/leads/capture")
                        .param("originPath", "/improvements/roof-to-wall/")
                        .param("routePath", "/improvements/roof-to-wall/")
                        .param("routeFamily", "improvement")
                        .param("scenario", "roof_related_decision")
                        .param("improvementType", "roof-to-wall")
                        .param("county", "Palm Beach")
                        .param("zip", "33480")
                        .param("reportReceived", "yes")
                        .param("homeType", "detached")
                        .param("budgetRange", "10k-20k")
                        .param("timeline", "31-90-days")
                        .param("email", "roof@example.com")
                        .param("phone", "")
                        .param("consent", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/improvements/roof-to-wall/?lead=success#lead-form"));

        String leads = Files.readString(Path.of("target/test-data/leads.csv"), StandardCharsets.UTF_8);
        assertThat(leads).contains("roof-retrofit-specialist");
    }

    @Test
    void leadCaptureRejectsMissingConsent() throws Exception {
        mockMvc.perform(post("/api/leads/capture")
                        .param("originPath", "/program/inspection-report/")
                        .param("routePath", "/program/inspection-report/")
                        .param("routeFamily", "program")
                        .param("scenario", "report_received_need_next_step")
                        .param("county", "Miami-Dade")
                        .param("zip", "33101")
                        .param("reportReceived", "yes")
                        .param("homeType", "detached")
                        .param("budgetRange", "5k-10k")
                        .param("timeline", "within-30-days")
                        .param("email", "person@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/program/inspection-report/?lead=error#lead-form"));

        String html = mockMvc.perform(get("/program/inspection-report/").param("lead", "error"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(html).contains("The form is missing a required field. Review the inputs and submit again.");
    }

    @Test
    void maliciousOriginPathFallsBackToRoot() throws Exception {
        mockMvc.perform(post("/api/leads/capture")
                        .param("originPath", "//evil.test")
                        .param("routePath", "/program/inspection-report/")
                        .param("routeFamily", "program")
                        .param("scenario", "report_received_need_next_step")
                        .param("county", "Miami-Dade")
                        .param("zip", "33101")
                        .param("reportReceived", "yes")
                        .param("homeType", "detached")
                        .param("budgetRange", "5k-10k")
                        .param("timeline", "within-30-days")
                        .param("email", "person@example.com")
                        .param("consent", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/?lead=success#lead-form"));
    }

    @Test
    void leadEventStoresScenarioRouteContextAndImprovementType() throws Exception {
        mockMvc.perform(post("/api/leads/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "eventType": "route_cta_click",
                                  "routePath": "/improvements/roof-to-wall/",
                                  "routeFamily": "improvement",
                                  "scenario": "roof_related_decision",
                                  "improvementType": "roof-to-wall",
                                  "detail": "hero"
                                }
                                """))
                .andExpect(status().isNoContent());

        String events = Files.readString(Path.of("target/test-data/events.csv"), StandardCharsets.UTF_8);
        assertThat(events).contains("route_cta_click");
        assertThat(events).contains("/improvements/roof-to-wall/");
        assertThat(events).contains("improvement");
        assertThat(events).contains("roof_related_decision");
        assertThat(events).contains("roof-to-wall");
    }

    @Test
    void partnerInquiryWritesStorageAndRedirects() throws Exception {
        mockMvc.perform(post("/api/contact/capture")
                        .param("originPath", "/contact/")
                        .param("inquiryType", "partner_pilot")
                        .param("routeFocus", "roof-to-wall")
                        .param("contactName", "Jordan Lee")
                        .param("company", "Coastal Retrofit Group")
                        .param("email", "partner@example.com")
                        .param("phone", "")
                        .param("licenseNumber", "CCC1333333")
                        .param("countiesServed", "Miami-Dade, Broward")
                        .param("message", "We handle retrofit-only roof-to-wall projects and stay inside report scope.")
                        .param("consent", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact/?partner=success#partner-inquiry"));

        String inquiries = Files.readString(Path.of("target/test-data/partner-inquiries.csv"), StandardCharsets.UTF_8);
        String events = Files.readString(Path.of("target/test-data/events.csv"), StandardCharsets.UTF_8);

        assertThat(inquiries).contains("Coastal Retrofit Group");
        assertThat(inquiries).contains("roof-to-wall");
        assertThat(events).contains("partner_inquiry_submit_success");
    }

    @Test
    void adminSummaryReflectsStoredLeadAndClickSignals() throws Exception {
        mockMvc.perform(post("/api/leads/capture")
                        .param("originPath", "/improvements/roof-to-wall/")
                        .param("routePath", "/improvements/roof-to-wall/")
                        .param("routeFamily", "improvement")
                        .param("scenario", "roof_related_decision")
                        .param("improvementType", "roof-to-wall")
                        .param("county", "Palm Beach")
                        .param("zip", "33480")
                        .param("reportReceived", "yes")
                        .param("homeType", "detached")
                        .param("budgetRange", "10k-20k")
                        .param("timeline", "31-90-days")
                        .param("email", "roof@example.com")
                        .param("phone", "")
                        .param("consent", "true"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(post("/api/leads/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "eventType": "route_cta_click",
                                  "routePath": "/improvements/roof-to-wall/",
                                  "routeFamily": "improvement",
                                  "scenario": "roof_related_decision",
                                  "improvementType": "roof-to-wall",
                                  "detail": "hero"
                                }
                                """))
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/api/contact/capture")
                        .param("originPath", "/contact/")
                        .param("inquiryType", "partner_pilot")
                        .param("routeFocus", "roof-to-wall")
                        .param("contactName", "Jordan Lee")
                        .param("company", "Coastal Retrofit Group")
                        .param("email", "partner@example.com")
                        .param("phone", "")
                        .param("licenseNumber", "CCC1333333")
                        .param("countiesServed", "Miami-Dade, Broward")
                        .param("message", "We handle retrofit-only roof-to-wall projects and stay inside report scope.")
                        .param("consent", "true"))
                .andExpect(status().is3xxRedirection());

        String html = mockMvc.perform(get("/admin/")
                        .header("Authorization", ADMIN_AUTH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(html).contains("roof-retrofit-specialist");
        assertThat(html).contains("Stored pilot inquiries");
        assertThat(html).contains("What contractors want to sponsor");
        assertThat(html).contains("roof-to-wall");
        assertThat(html).contains("route_cta_click");
        assertThat(html).contains("improvement");
    }
}
