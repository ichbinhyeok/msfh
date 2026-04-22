package owner.mysafefloridahome.pages;

import java.util.List;
import owner.mysafefloridahome.AppProperties;
import org.springframework.stereotype.Service;

@Service
public class VendorPacketService {
    private static final String OPENING_PROTECTION_TOOL_ENTRY_PATH = "/tools/opening-protection/quote-prep-brief/";
    private static final String OPENING_PROTECTION_PACKET_PATH = "/tools/opening-protection/quote-prep-brief/build/";

    private final AppProperties appProperties;

    public VendorPacketService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public VendorWorkflowPageView openingProtectionWorkflowEntry(String requestBaseUrl) {
        return new VendorWorkflowPageView(
                meta(
                        "Opening Protection Quote-Prep Brief",
                        "Shareable brief for narrowing an opening-protection quote request before price shopping or measurements.",
                        OPENING_PROTECTION_TOOL_ENTRY_PATH,
                        requestBaseUrl),
                "Shareable tool",
                "Get the first opening-protection request clear before you ask for price",
                "Use this when the My Safe Florida Home report already points toward opening protection and you want the first quote conversation to stay narrow around windows, shutters, doors, or a smaller mixed scope.",
                "Turn a fuzzy report-backed quote request into one short shareable brief a contractor can answer without widening the job too early.",
                "The free product is one homeowner-built quote-prep brief: current focus, named openings, attached-home caution, and what a contractor should clarify before pricing.",
                List.of(
                        "The exact opening-protection recommendation from the report is copied out instead of paraphrased loosely.",
                        "The current conversation is already narrowed to windows, shutters, doors, or a smaller mixed opening-protection quote.",
                        "The home type is known and any attached-home caution is still active.",
                        "The specific openings inside the first quote are named instead of implied as a whole-house package.",
                        "You can share photos of those openings now or say they may still be needed."),
                List.of(
                        "Build the shareable quote-prep brief and send it with the first contractor outreach.",
                        "Use the reply to narrow scope before phone calls, measurements, or broad price comparison starts.",
                        "Keep estimator notes, quote-boundary language, and office setup behind the free layer unless a contractor office is reusing this repeatedly."),
                List.of(
                        "Homeowner already has the report and wants the first contractor reply to stay narrow",
                        "Opening-protection scope keeps drifting into broad whole-house pricing",
                        "Attached-home or HOA caveats may change what belongs in the first quote",
                        "A contractor office wants one customer-facing brief instead of rewriting the same first-send explanation"),
                List.of(
                        "This is not a whole-house package request",
                        "This does not replace permits, approvals, reimbursement decisions, or grant closeout",
                        "This should not blur attached-home caution into detached-home assumptions",
                        "This should not turn into office persistence, branding, dashboards, or estimator software"),
                List.of(preQuoteBriefCard()),
                "");
    }

    public VendorPacketPageView openingProtectionPreQuote(
            String requestBaseUrl,
            String blockingMessage) {
        return new VendorPacketPageView(
                meta(
                        "Opening Protection Quote-Prep Brief Builder",
                        "Shareable quote-prep brief for narrowing an opening-protection request before pricing.",
                        OPENING_PROTECTION_PACKET_PATH,
                        requestBaseUrl),
                "Shareable brief builder",
                "Build the first quote request before you ask for price",
                "Use this before you contact contractors when the report already points toward opening protection but the first quote still needs a narrower scope.",
                OPENING_PROTECTION_TOOL_ENTRY_PATH,
                "This builder turns a report-backed quote request into a shareable brief a contractor can answer without widening the job too early.",
                "Use this brief when",
                "You already have the inspection report, want a quote, and the likely path sounds like windows, shutters, doors, garage-door protection, or a narrower opening-protection mix.",
                "Quote-prep anchors before contractor time gets spent",
                "The first contractor reply gets cleaner when these anchors are strong enough to keep the quote narrow and defensible.",
                List.of(
                        "The exact recommendation wording copied out of the report",
                        "The current quote focus: windows, shutters, doors, or a smaller opening-protection mix",
                        "The home type: detached or attached / townhouse-like",
                        "The specific openings inside the first quote, named clearly instead of implied as whole-house work",
                        "Whether the contractor should expect the report page or opening photos right away"),
                "Questions the brief should survive",
                "Use these to keep the first quote understandable before price comparison starts.",
                List.of(
                        "Which focus is this conversation in right now: windows, shutters, doors, or still too broad?",
                        "Which exact recommendation from the report is this quote solving?",
                        "Which openings are inside this quote, and which are outside it?",
                        "Which line items are mitigation-path items and which are optional upgrades?",
                        "Who handles permits, inspections, and closeout documents?",
                        "If the home is attached, what keeps the quote inside the narrower scope?"),
                "What the brief should refuse to imply",
                List.of(
                        "Do not assume every opening needs the same product",
                        "Do not assume unnamed openings belong in the first quote",
                        "Do not assume a contractor mentioning the program means the scope is correct",
                        "Do not assume attached-home cases follow detached-home scope",
                        "Do not assume broad whole-house pricing is the same thing as the report recommendation"),
                blockingMessage,
                List.of());
    }

    private PageMeta meta(String title, String description, String path, String requestBaseUrl) {
        return new PageMeta(
                title + " | " + appProperties.getSiteName(),
                description,
                appProperties.absoluteUrl(path, requestBaseUrl),
                "noindex,nofollow",
                List.of(
                        new BreadcrumbItem("Home", "/"),
                        new BreadcrumbItem("Opening-protection quote-prep brief", OPENING_PROTECTION_TOOL_ENTRY_PATH),
                        new BreadcrumbItem(title, path)),
                appProperties.resolvedBaseUrl(requestBaseUrl),
                "");
    }

    private RouteDetailCard preQuoteBriefCard() {
        return new RouteDetailCard(
                "Start here",
                "Quote-Prep Brief Builder",
                "Create the shareable brief you can send before a contractor widens the first quote conversation.",
                OPENING_PROTECTION_PACKET_PATH,
                "Build brief");
    }
}
