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

  document.querySelectorAll("[data-track-view]").forEach(function (element) {
    sendEvent(payloadFrom(element, element.dataset.trackView));
  });

  document.querySelectorAll("[data-print-page='true']").forEach(function (button) {
    button.addEventListener("click", function () {
      window.print();
    });
  });

  var globalSearchParams = new URLSearchParams(window.location.search);
  if (globalSearchParams.get("print") === "1") {
    window.setTimeout(function () {
      window.print();
    }, 150);
  }

  function copyText(text) {
    if (navigator.clipboard && navigator.clipboard.writeText) {
      return navigator.clipboard.writeText(text).catch(function () {
        return null;
      });
    }
    return null;
  }

  document.querySelectorAll("[data-copy-target]").forEach(function (button) {
    button.addEventListener("click", function () {
      var key = button.dataset.copyTarget;
      var output = document.querySelector("[data-" + key + "='true']");
      if (!output) {
        return;
      }
      copyText(output.value || output.textContent || "");
    });
  });

  document.querySelectorAll("[data-signal-increment]").forEach(function (button) {
    button.addEventListener("click", function () {
      var key = button.dataset.signalIncrement;
      if (key) {
        document.querySelectorAll("[data-signal-counter='" + key + "']").forEach(function (counter) {
          var current = parseInt((counter.textContent || "0").trim(), 10);
          counter.textContent = Number.isNaN(current) ? "1" : String(current + 1);
        });
      }
      var status = document.querySelector("[data-signal-status='true']");
      if (status && button.dataset.signalStatusMessage) {
        status.textContent = button.dataset.signalStatusMessage;
      }
    });
  });

  function applyPrefillParams(form, params, names) {
    names.forEach(function (name) {
      if (!params.has(name)) {
        return;
      }
      var field = form.elements.namedItem(name);
      if (!field) {
        return;
      }
      if (field.type === "checkbox") {
        field.checked = params.get(name) === "true";
        return;
      }
      field.value = params.get(name);
    });
  }

  function ensureSentence(value) {
    if (!value) {
      return "";
    }
    var normalized = value.trim();
    if (!normalized) {
      return normalized;
    }
    var lastCharacter = normalized.charAt(normalized.length - 1);
    if (lastCharacter === "." || lastCharacter === "!" || lastCharacter === "?") {
      return normalized;
    }
    return normalized + ".";
  }

  function joinList(items) {
    if (!items.length) {
      return "";
    }
    if (items.length === 1) {
      return items[0];
    }
    if (items.length === 2) {
      return items[0] + " and " + items[1];
    }
    return items.slice(0, -1).join(", ") + ", and " + items[items.length - 1];
  }

  function scopeLaneNeedsNarrowing(value) {
    return !value || value === "broad";
  }

  function scopeLaneLabel(value) {
    if (value === "windows") {
      return "Windows only";
    }
    if (value === "shutters") {
      return "Shutters only";
    }
    if (value === "doors") {
      return "Doors or garage-door protection only";
    }
    if (value === "mixed") {
      return "A small mixed openings quote";
    }
    if (value === "broad") {
      return "Still too broad or unclear";
    }
    return "Still needs a narrower focus";
  }

  function scopeLaneSummary(value) {
    if (value === "windows") {
      return "This first quote should stay focused on windows only unless the scope is restated more broadly in writing.";
    }
    if (value === "shutters") {
      return "This first quote should stay focused on shutters only unless the scope is restated more broadly in writing.";
    }
    if (value === "doors") {
      return "This first quote should stay focused on doors or garage-door protection only unless the scope is restated more broadly in writing.";
    }
    if (value === "mixed") {
      return "A small mixed openings quote is in bounds, but it should stay narrower than a broad whole-house package.";
    }
    if (value === "broad") {
      return "The request is still at a broad or unclear package level, so narrow this to windows, shutters, doors, or a small mixed openings quote before pricing.";
    }
    return "This brief still needs to name whether the first quote is for windows, shutters, doors, or a small mixed openings quote.";
  }

  function scopeLaneReplyPhrase(value) {
    if (value === "windows") {
      return "windows only";
    }
    if (value === "shutters") {
      return "shutters only";
    }
    if (value === "doors") {
      return "doors or garage-door opening protection only";
    }
    if (value === "mixed") {
      return "a small mixed openings quote";
    }
    return "the narrowest first quote focus";
  }

  function openingsReplyPhrase(value) {
    return value || "the exact openings you want quoted first";
  }

  function replyExample(scopeLane, scopeOpenings) {
    if (scopeLaneNeedsNarrowing(scopeLane)) {
      return "I can review this, but please send the report page, clear opening photos, and the narrowest first quote focus for " + openingsReplyPhrase(scopeOpenings) + ". I am not quoting a broad whole-house package yet.";
    }
    return "I can keep this " + scopeLaneReplyPhrase(scopeLane) + " for " + openingsReplyPhrase(scopeOpenings) + ". Please tell me if the report page or clearer opening photos are still needed before pricing.";
  }

  function reportReplyChecklistItem(reportPageReceived) {
    return reportPageReceived
      ? "Confirm the report page already shared matches this first quote, or say if you need the correct page."
      : "Say whether you need the report page that shows the opening-protection recommendation.";
  }

  function photoReplyChecklistItem(photosReceived) {
    return photosReceived
      ? "Confirm the listed openings match the photos already shared, or say if clearer photos are still needed."
      : "Say whether clear photos of the openings are still needed before pricing.";
  }

  function preQuoteReplyLine(reportPageReceived, photosReceived, scopeLane, missing) {
    var requestItems = [
      reportPageReceived
        ? "confirm the report page already shared"
        : "send the report page that shows the opening-protection recommendation",
      photosReceived
        ? "confirm the listed openings match the photos already shared"
        : "send clear photos of only the openings inside this first quote"
    ];
    if (scopeLaneNeedsNarrowing(scopeLane)) {
      requestItems.push("name the narrowest focus you want quoted first");
      return "Please " + joinList(requestItems) + ".";
    }
    if (missing.length) {
      requestItems.push("send one short line confirming the first quote focus and exact openings");
      return "Please " + joinList(requestItems) + ".";
    }
    return "Please review the listed openings and reply before scheduling if anything is wrong.";
  }

  document.querySelectorAll("[data-quote-prep-form='true']").forEach(function (form) {
    var tool = form.closest(".packet-tool-grid");
    var previewTitle = tool ? tool.querySelector("[data-prequote-preview-title='true']") : null;
    var previewSummary = tool ? tool.querySelector("[data-prequote-preview-summary='true']") : null;
    var previewList = tool ? tool.querySelector("[data-prequote-preview-list='true']") : null;
    var blockingMessage = tool ? tool.querySelector("[data-prequote-blocking-message='true']") : null;
    var submitButton = form.querySelector("[data-prequote-submit='true']");
    var returnReportItem = tool ? tool.querySelector("[data-prequote-return-report='true']") : null;
    var returnPhotosItem = tool ? tool.querySelector("[data-prequote-return-photos='true']") : null;
    var returnFocusItem = tool ? tool.querySelector("[data-prequote-return-focus='true']") : null;
    var prefillParams = new URLSearchParams(window.location.search);
    if (!previewTitle || !previewSummary || !previewList) {
      return;
    }

    function value(name) {
      var field = form.elements.namedItem(name);
      if (!field) {
        return "";
      }
      if (field.type === "checkbox") {
        return field.checked;
      }
      return field.value.trim();
    }

    function setPreviewItems(items) {
      previewList.innerHTML = "";
      items.filter(function (item) {
        return !!item;
      }).forEach(function (item) {
        var li = document.createElement("li");
        li.textContent = item;
        previewList.appendChild(li);
      });
    }

    applyPrefillParams(form, prefillParams, [
      "siteLabel",
      "countyZip",
      "homeType",
      "scopeLane",
      "recommendationLine",
      "scopeOpenings",
      "reportPageReceived",
      "photosReceived",
      "broadPackageRequested",
      "compareQuotesRequested",
      "reimbursementAssumed",
      "hoaReviewLikely",
    ]);

    function syncPreQuoteTool() {
      var siteLabel = value("siteLabel") || "this home";
      var countyZip = value("countyZip");
      var homeType = value("homeType");
      var scopeLane = value("scopeLane");
      var recommendationLine = value("recommendationLine");
      var scopeOpenings = value("scopeOpenings");
      var reportPageReceived = value("reportPageReceived");
      var photosReceived = value("photosReceived");
      var broadPackageRequested = value("broadPackageRequested");
      var compareQuotesRequested = value("compareQuotesRequested");
      var reimbursementAssumed = value("reimbursementAssumed");
      var hoaReviewLikely = value("hoaReviewLikely");
      var validHomeType = homeType === "detached" || homeType === "attached";

      var blockingItems = [];
      var missing = [];
      var watchouts = [
        "This quote-prep brief does not mean the job is approved, reimbursed, or expanded to every opening in the home."
      ];

      if (!reportPageReceived) {
        missing.push("the report page that shows the opening-protection recommendation");
      }

      if (!photosReceived) {
        missing.push("clear photos of the openings for this first quote");
      }

      if (!countyZip) {
        missing.push("the county or ZIP code for the property");
      }

      if (!validHomeType) {
        blockingItems.push("the home type");
      }

      if (!scopeLane) {
        missing.push("the first quote focus: windows, shutters, doors, or a smaller opening-protection mix");
      }

      if (!recommendationLine) {
        blockingItems.push("the recommendation wording from the report");
        missing.push("the recommendation wording from the report");
      }

      if (!scopeLane && !scopeOpenings) {
        blockingItems.push("the first quote focus or the exact openings for this first quote");
      }

      if (!scopeOpenings) {
        missing.push("the exact windows or doors that belong in this first quote");
      }

      if (homeType === "attached") {
        watchouts.push("Because this home may be attached or townhouse-like, this first quote should stay inside the narrower attached-home path until the scope is confirmed.");
      }

      if (!blockingItems.length && (scopeLaneNeedsNarrowing(scopeLane) || broadPackageRequested)) {
        watchouts.push("This brief is meant to keep the first quote from widening into a broad whole-house package before the scope is restated clearly.");
      }
      if (reimbursementAssumed) {
        watchouts.push("Do not treat this brief as proof that approval or reimbursement is already confirmed.");
      }

      watchouts[0] = "This summary does not mean the job is approved, reimbursed, or expanded to every opening in the home.";
      watchouts.push("Any window or door not named for this first quote should stay outside scope until the scope is restated clearly in writing.");

      var scenarioItems = [];
      if (homeType === "attached") {
        scenarioItems.push("Attached-home caution: keep this quote inside the attached-home scope until the scope is confirmed.");
      }
      if (compareQuotesRequested) {
        scenarioItems.push("Quote comparison: compare only contractors quoting the same named openings, path, and exclusions.");
      }
      if (reimbursementAssumed) {
        scenarioItems.push("Program caution: this brief does not confirm grant approval or reimbursement.");
      }
      if (hoaReviewLikely) {
        scenarioItems.push("Community review: HOA or condo review may still need separate confirmation outside this first quote.");
      }

      if (scopeLaneNeedsNarrowing(scopeLane)) {
        scenarioItems.push("Scope narrowing: narrow this to windows, shutters, doors, or a smaller opening-protection mix before pricing, comparing bids, or scheduling.");
      }

      var requestItems = missing.slice();
      if (scopeLaneNeedsNarrowing(scopeLane) && requestItems.indexOf("the first quote focus: windows, shutters, doors, or a smaller opening-protection mix") === -1) {
        requestItems.push("the narrowest focus you want quoted first");
      }
      var requestLine = requestItems.length
              ? "If more is needed before pricing or scheduling, keep it limited to " + joinList(requestItems) + "."
              : "This brief should be enough to start one narrow first-quote conversation.";
      var openingsLine = scopeOpenings
              ? "The first quote should only cover " + ensureSentence(scopeOpenings)
              : "The first quote should only cover the openings supported by the report and the photos already shared.";
      var boundaryLine = "Any window or door not named for this first quote stays outside scope until the scope is restated clearly in writing.";
      var reportReplyItem = reportReplyChecklistItem(reportPageReceived);
      var photosReplyItem = photoReplyChecklistItem(photosReceived);
      var replyChecklistItems = [
        reportReplyItem,
        photosReplyItem,
        scopeLaneNeedsNarrowing(scopeLane)
          ? "Tell me the narrowest focus you can quote first, and whether this request is still too broad."
          : "Confirm the first quote can stay " + scopeLaneReplyPhrase(scopeLane) + " and limited to " + openingsReplyPhrase(scopeOpenings) + "."
      ];

      if (blockingMessage) {
        blockingMessage.hidden = blockingItems.length === 0;
      }
      if (submitButton) {
        submitButton.disabled = blockingItems.length > 0;
        submitButton.textContent = blockingItems.length
          ? "Fill Scope Anchors First"
          : ((scopeLaneNeedsNarrowing(scopeLane) || broadPackageRequested || missing.length)
            ? "Create Clarification Brief"
            : "Create Quote-Prep Brief");
      }
      if (returnReportItem) {
        returnReportItem.textContent = replyChecklistItems[0];
      }
      if (returnPhotosItem) {
        returnPhotosItem.textContent = replyChecklistItems[1];
      }
      if (returnFocusItem) {
        returnFocusItem.textContent = replyChecklistItems[2];
      }

      previewTitle.textContent = "Opening protection quote-prep brief for " + siteLabel;
      previewSummary.textContent = blockingItems.length
        ? "This brief stays blocked until the first-send anchors are filled."
        : ((scopeLaneNeedsNarrowing(scopeLane) || broadPackageRequested)
          ? "This brief narrows the reply before pricing or scheduling."
          : "This brief states the first quote focus, the named openings, and what stays outside it.");
      var previewReplyLine = blockingItems.length
        ? "Add the report recommendation plus a narrow first quote focus before sharing this brief."
        : requestLine;
      setPreviewItems([
        "Focus now: " + scopeLaneSummary(scopeLane),
        "Inside this first quote: " + openingsLine,
        "Outside for now: " + boundaryLine,
        "Reply with: " + previewReplyLine
      ].concat(scenarioItems.slice(0, 1)));
    }

    form.addEventListener("input", syncPreQuoteTool);
    form.addEventListener("change", syncPreQuoteTool);
    syncPreQuoteTool();
  });

  document.querySelectorAll("[data-lead-form='true']").forEach(function (form) {
    form.addEventListener("submit", function () {
      sendEvent(payloadFrom(form, "lead_submit_attempt"));
    });
  });

  document.querySelectorAll("[data-home-tool='true']").forEach(function (form) {
    var reportState = form.querySelector("select[name='reportState']");
    var recommendationType = form.querySelector("select[name='recommendationType']");
    var homeType = form.querySelector("select[name='homeType']");
    var priority = form.querySelector("select[name='priority']");
    var steps = Array.prototype.slice.call(form.querySelectorAll("[data-home-step]"));
    var progressItems = Array.prototype.slice.call(form.querySelectorAll("[data-home-progress-item]"));
    var stepCopy = form.querySelector("[data-home-step-copy='true']");
    var nextButton = form.querySelector("[data-home-next='true']");
    var backButton = form.querySelector("[data-home-back='true']");
    var submitButton = form.querySelector(".console-submit");
    if (!reportState || !recommendationType || !homeType || !priority) {
      return;
    }
    var reviewMode = globalSearchParams.toString().length > 0;
    var currentStep = 0;

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

    function renderAssistantSteps() {
      if (!steps.length || !nextButton || !backButton || !submitButton) {
        return;
      }
      if (reviewMode) {
        steps.forEach(function (step) {
          step.hidden = false;
        });
        progressItems.forEach(function (item) {
          item.classList.add("is-complete");
          item.classList.remove("is-active");
        });
        nextButton.hidden = true;
        backButton.hidden = true;
        submitButton.hidden = false;
        if (stepCopy) {
          stepCopy.textContent = "Review and change any answer, then run the guide again.";
        }
        return;
      }

      steps.forEach(function (step, index) {
        step.hidden = index !== currentStep;
      });
      progressItems.forEach(function (item, index) {
        item.classList.toggle("is-active", index === currentStep);
        item.classList.toggle("is-complete", index < currentStep);
      });
      backButton.disabled = currentStep === 0;
      nextButton.hidden = currentStep === steps.length - 1;
      submitButton.hidden = currentStep !== steps.length - 1;
      if (stepCopy) {
        stepCopy.textContent = "Step " + (currentStep + 1) + " of " + steps.length;
      }
    }

    if (nextButton) {
      nextButton.addEventListener("click", function () {
        if (currentStep < steps.length - 1) {
          currentStep += 1;
          renderAssistantSteps();
        }
      });
    }

    if (backButton) {
      backButton.addEventListener("click", function () {
        if (currentStep > 0) {
          currentStep -= 1;
          renderAssistantSteps();
        }
      });
    }

    form.addEventListener("submit", function () {
      sendEvent(payloadFrom(form, "home_tool_submit"));
    });

    reportState.addEventListener("change", syncHomeToolState);
    recommendationType.addEventListener("change", syncHomeToolState);
    syncHomeToolState();
    renderAssistantSteps();
  });
})();
