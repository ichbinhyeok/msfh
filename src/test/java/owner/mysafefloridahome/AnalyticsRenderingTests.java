package owner.mysafefloridahome;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AnalyticsRenderingTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void homePageUsesCustomGaBootstrap() throws Exception {
        String html = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(html).contains("https://www.googletagmanager.com/gtag/js?id=G-1C3Q631V0G");
        assertThat(html).contains("gtag('config', 'G-1C3Q631V0G', { send_page_view: false });");
        assertThat(html).contains("<script defer src=\"/analytics.js\"></script>");
    }
}
