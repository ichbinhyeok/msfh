(function () {
  function payloadFrom(element, eventType) {
    return {
      eventType,
      routePath: element.dataset.routePath || "/",
      routeFamily: element.dataset.routeFamily || "unknown",
      scenario: element.dataset.scenario || "",
      improvementType: element.dataset.improvementType || "",
      detail: element.dataset.detail || ""
    };
  }

  function sendEvent(payload) {
    fetch("/api/leads/event", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(payload),
      keepalive: true
    }).catch(function () {
      return null;
    });
  }

  document.querySelectorAll("[data-track-event]").forEach(function (element) {
    element.addEventListener("click", function () {
      sendEvent(payloadFrom(element, element.dataset.trackEvent));
    });
  });

  document.querySelectorAll("[data-lead-form='true']").forEach(function (form) {
    form.addEventListener("submit", function () {
      sendEvent(payloadFrom(form, "lead_submit_attempt"));
    });
  });

  document.querySelectorAll("[data-partner-form='true']").forEach(function (form) {
    form.addEventListener("submit", function () {
      sendEvent(payloadFrom(form, "partner_inquiry_submit_attempt"));
    });
  });

  document.querySelectorAll("[data-home-tool='true']").forEach(function (form) {
    var reportState = form.querySelector("select[name='reportState']");
    var recommendationType = form.querySelector("select[name='recommendationType']");
    var priority = form.querySelector("select[name='priority']");
    if (!reportState || !recommendationType || !priority) {
      return;
    }

    function syncHomeToolState() {
      var state = reportState.value;
      var rec = recommendationType.value;
      var recommendationDisabled = state === "not-yet" || state === "status-problem" || state === "received-no-recommendations";
      recommendationType.disabled = recommendationDisabled;

      if (recommendationDisabled) {
        recommendationType.value = "not-sure";
      }

      Array.prototype.forEach.call(priority.options, function (option) {
        if (option.value === "compare-quotes") {
          option.disabled = recommendationType.value === "not-sure" || recommendationDisabled;
        }
      });

      if (priority.value === "compare-quotes" && (recommendationType.value === "not-sure" || recommendationDisabled)) {
        priority.value = state === "received-recommendation" ? "understand-report" : "choose-project";
      }
    }

    reportState.addEventListener("change", syncHomeToolState);
    recommendationType.addEventListener("change", syncHomeToolState);
    syncHomeToolState();
  });
})();
