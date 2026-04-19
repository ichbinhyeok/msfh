(function () {
  var pendingPrefix = "ga4_pending_";

  function payloadFromElement(element, eventType) {
    return {
      eventType: eventType,
      routePath: element.dataset.routePath || "/",
      routeFamily: element.dataset.routeFamily || "",
      scenario: element.dataset.scenario || "",
      improvementType: element.dataset.improvementType || "",
      detail: element.dataset.detail || ""
    };
  }

  function sendInternalEvent(payload) {
    fetch("/api/leads/event", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        eventType: payload.eventType,
        routePath: payload.routePath || "/",
        routeFamily: payload.routeFamily || "unknown",
        scenario: payload.scenario || "",
        improvementType: payload.improvementType || "",
        detail: payload.detail || ""
      }),
      keepalive: true
    }).catch(function () {
      return null;
    });
  }

  function hasGtag() {
    return typeof window.gtag === "function";
  }

  function normalizePath(path) {
    if (!path) {
      return "/";
    }

    var normalized = path;
    if (/^[a-z]+:\/\//i.test(normalized)) {
      try {
        normalized = new URL(normalized).pathname;
      } catch (error) {
        return "/";
      }
    }

    normalized = normalized.split("?")[0].split("#")[0];
    if (!normalized) {
      return "/";
    }
    if (normalized.charAt(0) !== "/") {
      normalized = "/" + normalized;
    }

    [
      [/^\/tools\/opening-protection\/quote-prep-brief\/result\/[^/]+\/$/, "/tools/opening-protection/quote-prep-brief/result/"],
      [/^\/vendor-handoffs\/opening-protection\/[^/]+\/$/, "/vendor-handoffs/opening-protection/"],
      [/^\/tools\/opening-protection\/quote-prep-brief\/internal\/[^/]+\/$/, "/tools/opening-protection/quote-prep-brief/internal/"],
      [/^\/vendor-handoffs\/opening-protection\/record\/[^/]+\/$/, "/vendor-handoffs/opening-protection/record/"],
      [/^\/tools\/opening-protection\/quote-prep-brief\/share\/[^/]+\/export\/pdf\/$/, "/tools/opening-protection/quote-prep-brief/share/export/pdf/"],
      [/^\/vendor-handoffs\/opening-protection\/brief\/[^/]+\/export\/pdf\/$/, "/vendor-handoffs/opening-protection/brief/export/pdf/"],
      [/^\/tools\/opening-protection\/quote-prep-brief\/share\/[^/]+\/$/, "/tools/opening-protection/quote-prep-brief/share/"],
      [/^\/vendor-handoffs\/opening-protection\/brief\/[^/]+\/$/, "/vendor-handoffs/opening-protection/brief/"]
    ].forEach(function (entry) {
      normalized = normalized.replace(entry[0], entry[1]);
    });

    return normalized;
  }

  function currentPagePath() {
    return normalizePath(window.location.pathname || "/");
  }

  function derivePageFamily(path) {
    if (path === "/") {
      return "home";
    }
    if (path.indexOf("/program/") === 0) {
      return "program";
    }
    if (path.indexOf("/improvements/") === 0) {
      return "improvement";
    }
    if (path.indexOf("/guides/") === 0) {
      return "guide";
    }
    if (path.indexOf("/about/") === 0
        || path.indexOf("/methodology/") === 0
        || path.indexOf("/contact/") === 0
        || path.indexOf("/not-government-affiliated/") === 0
        || path.indexOf("/privacy/") === 0
        || path.indexOf("/terms/") === 0) {
      return "trust";
    }
    if (path.indexOf("/tools/opening-protection/quote-prep-brief/") === 0
        || path.indexOf("/vendor-packets/") === 0
        || path.indexOf("/vendor-handoffs/") === 0) {
      return "vendor-handoff";
    }
    if (path.indexOf("/admin/") === 0) {
      return "admin";
    }
    return "unknown";
  }

  function firstAttribute(attributeName) {
    var node = document.querySelector("[" + attributeName + "]");
    return node ? (node.getAttribute(attributeName) || "") : "";
  }

  function detectScenario(path) {
    var attributeValue = firstAttribute("data-scenario");
    if (attributeValue) {
      return attributeValue;
    }
    if (derivePageFamily(path) === "vendor-handoff") {
      return "opening-protection";
    }
    if (path === "/contact/" && document.querySelector("[data-partner-form='true']")) {
      return "partner_pilot";
    }
    return "";
  }

  function detectImprovementType(path) {
    var attributeValue = firstAttribute("data-improvement-type");
    if (attributeValue) {
      return attributeValue;
    }
    if (path.indexOf("/improvements/") === 0) {
      var segments = path.split("/");
      return segments.length > 2 ? segments[2] : "";
    }
    if (path.indexOf("opening-protection") !== -1
        || path.indexOf("/vendor-packets/") === 0
        || path.indexOf("/vendor-handoffs/") === 0) {
      return "opening-protection";
    }
    return "";
  }

  function deriveToolName(eventName, path) {
    if (eventName === "decision_tool_submit" || path === "/") {
      return "home_decision_tool";
    }
    if (derivePageFamily(path) === "vendor-handoff") {
      return "opening_protection_quote_prep";
    }
    if (path === "/contact/" && document.querySelector("[data-partner-form='true']")) {
      return "partner_pilot_form";
    }
    if (document.querySelector("[data-lead-form='true']")) {
      return "next_step_lead_form";
    }
    return "";
  }

  function setPending(name, payload) {
    try {
      window.sessionStorage.setItem(pendingPrefix + name, JSON.stringify(payload));
    } catch (error) {
      return;
    }
  }

  function consumePending(name) {
    var raw;
    try {
      raw = window.sessionStorage.getItem(pendingPrefix + name);
      window.sessionStorage.removeItem(pendingPrefix + name);
    } catch (error) {
      return null;
    }

    if (!raw) {
      return null;
    }

    try {
      return JSON.parse(raw);
    } catch (error) {
      return null;
    }
  }

  function compactParams(params) {
    var compact = {};
    Object.keys(params).forEach(function (key) {
      var value = params[key];
      if (typeof value === "string") {
        value = value.trim();
      }
      if (value === "" || value === null || value === undefined) {
        return;
      }
      compact[key] = value;
    });
    return compact;
  }

  function buildEventParams(payload, overrides) {
    var pagePath = currentPagePath();
    var routePath = normalizePath(payload && payload.routePath ? payload.routePath : pagePath);
    var params = {
      page_title: document.title,
      page_path: pagePath,
      page_location: window.location.origin + pagePath,
      page_family: derivePageFamily(pagePath),
      page_group: derivePageFamily(pagePath),
      route_path: routePath,
      route_family: (payload && payload.routeFamily ? payload.routeFamily : "").trim() || derivePageFamily(routePath),
      scenario: (payload && payload.scenario ? payload.scenario : "").trim() || detectScenario(routePath),
      improvement_type: (payload && payload.improvementType ? payload.improvementType : "").trim() || detectImprovementType(routePath),
      tool_name: deriveToolName(payload ? payload.eventType : "", pagePath)
    };

    Object.assign(params, overrides || {});
    return compactParams(params);
  }

  function sendGaEvent(eventName, payload, overrides) {
    if (!eventName || !hasGtag()) {
      return;
    }
    window.gtag("event", eventName, buildEventParams(payload, overrides));
  }

  function handleSubmissionCompletionEvents() {
    var query = new URLSearchParams(window.location.search);

    if (query.get("lead") === "success") {
      var leadSuccessPayload = consumePending("lead");
      if (leadSuccessPayload) {
        sendGaEvent("lead_submit_success", leadSuccessPayload, { form_name: "next_step_lead_form" });
      }
    } else if (query.get("lead") === "error") {
      var leadErrorPayload = consumePending("lead");
      if (leadErrorPayload) {
        sendGaEvent("lead_submit_error", leadErrorPayload, { form_name: "next_step_lead_form" });
      }
    }

    if (query.get("partner") === "success") {
      var partnerSuccessPayload = consumePending("partner");
      if (partnerSuccessPayload) {
        sendGaEvent("partner_inquiry_submit_success", partnerSuccessPayload, { form_name: "partner_pilot_form" });
      }
    } else if (query.get("partner") === "error") {
      var partnerErrorPayload = consumePending("partner");
      if (partnerErrorPayload) {
        sendGaEvent("partner_inquiry_submit_error", partnerErrorPayload, { form_name: "partner_pilot_form" });
      }
    }

    if (document.querySelector("[data-track-view='vendor_handoff_result_open']")) {
      var vendorCreatedPayload = consumePending("vendor_handoff");
      if (vendorCreatedPayload) {
        sendGaEvent("vendor_handoff_created", vendorCreatedPayload, { tool_name: "opening_protection_quote_prep" });
      }
    }
  }

  function attachTrackedElementMirrors() {
    document.querySelectorAll("[data-track-event]").forEach(function (element) {
      element.addEventListener("click", function () {
        sendGaEvent(element.dataset.trackEvent, payloadFromElement(element, element.dataset.trackEvent));
      });
    });

    document.querySelectorAll("[data-track-view]").forEach(function (element) {
      sendGaEvent(element.dataset.trackView, payloadFromElement(element, element.dataset.trackView));
    });
  }

  function attachLeadFormMirrors() {
    document.querySelectorAll("[data-lead-form='true']").forEach(function (form) {
      form.addEventListener("submit", function () {
        var payload = payloadFromElement(form, "lead_submit_attempt");
        setPending("lead", payload);
        sendGaEvent("lead_submit_attempt", payload, { form_name: "next_step_lead_form" });
      });
    });

    document.querySelectorAll("[data-partner-form='true']").forEach(function (form) {
      form.addEventListener("submit", function () {
        var payload = payloadFromElement(form, "partner_inquiry_submit_attempt");
        setPending("partner", payload);
        sendGaEvent("partner_inquiry_submit_attempt", payload, { form_name: "partner_pilot_form" });
      });
    });
  }

  function fieldValue(form, name) {
    var field = form.elements.namedItem(name);
    if (!field) {
      return "";
    }
    if (field.type === "checkbox") {
      return field.checked ? "true" : "false";
    }
    return field.value || "";
  }

  function attachDecisionToolMirror() {
    document.querySelectorAll("[data-home-tool='true']").forEach(function (form) {
      form.addEventListener("submit", function () {
        var reportState = fieldValue(form, "reportState");
        var recommendationType = fieldValue(form, "recommendationType");
        var homeType = fieldValue(form, "homeType");
        var priority = fieldValue(form, "priority");
        var payload = {
          eventType: "decision_tool_submit",
          routePath: "/",
          routeFamily: "home",
          scenario: "decision_tool",
          improvementType: recommendationType === "not-sure" ? "" : recommendationType,
          detail: "report_state=" + reportState + ";home_type=" + homeType + ";priority=" + priority
        };

        sendInternalEvent(payload);
        sendGaEvent("decision_tool_submit", payload, {
          report_state: reportState,
          recommendation_type: recommendationType,
          home_type: homeType,
          priority: priority,
          tool_name: "home_decision_tool"
        });
      });
    });
  }

  function attachVendorFlowMirror() {
    document.querySelectorAll("[data-vendor-prequote-form='true']").forEach(function (form) {
      form.addEventListener("submit", function () {
        setPending("vendor_handoff", {
          eventType: "vendor_handoff_created",
          routePath: "/tools/opening-protection/quote-prep-brief/result/",
          routeFamily: "vendor-handoff",
          scenario: "opening-protection",
          improvementType: "opening-protection"
        });

        sendGaEvent("vendor_handoff_submit_attempt", {
          eventType: "vendor_handoff_submit_attempt",
          routePath: currentPagePath(),
          routeFamily: "vendor-handoff",
          scenario: "opening-protection",
          improvementType: "opening-protection"
        }, {
          tool_name: "opening_protection_quote_prep"
        });
      });
    });
  }

  sendGaEvent("page_view", null);
  attachTrackedElementMirrors();
  attachLeadFormMirrors();
  attachDecisionToolMirror();
  attachVendorFlowMirror();
  handleSubmissionCompletionEvents();
})();
