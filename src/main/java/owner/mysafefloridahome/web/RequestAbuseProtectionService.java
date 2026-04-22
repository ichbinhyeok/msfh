package owner.mysafefloridahome.web;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RequestAbuseProtectionService {

    private static final Duration WINDOW = Duration.ofMinutes(15);
    private static final int LEAD_FORM_LIMIT = 12;
    private static final int EVENT_LIMIT = 120;

    private final Map<String, Deque<Long>> requestBuckets = new ConcurrentHashMap<>();

    public boolean allowLeadForm(HttpServletRequest request) {
        return sameSiteRequest(request) && withinRateLimit(request, "lead-form", LEAD_FORM_LIMIT);
    }

    public boolean allowEventCapture(HttpServletRequest request) {
        return sameSiteRequest(request) && withinRateLimit(request, "event", EVENT_LIMIT);
    }

    private boolean sameSiteRequest(HttpServletRequest request) {
        String source = firstNonBlank(request.getHeader("Origin"), request.getHeader("Referer"));
        if (!StringUtils.hasText(source)) {
            return true;
        }
        try {
            URI uri = URI.create(source);
            if (!StringUtils.hasText(uri.getHost()) || !StringUtils.hasText(uri.getScheme())) {
                return false;
            }
            return request.getServerName().equalsIgnoreCase(uri.getHost())
                    && request.getScheme().equalsIgnoreCase(uri.getScheme())
                    && normalizePort(request.getScheme(), request.getServerPort())
                            == normalizePort(uri.getScheme(), uri.getPort());
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    private boolean withinRateLimit(HttpServletRequest request, String channel, int limit) {
        String bucketKey = clientKey(request) + "|" + channel;
        Deque<Long> bucket = requestBuckets.computeIfAbsent(bucketKey, ignored -> new ArrayDeque<>());
        long now = System.currentTimeMillis();
        synchronized (bucket) {
            long cutoff = now - WINDOW.toMillis();
            while (!bucket.isEmpty() && bucket.peekFirst() < cutoff) {
                bucket.removeFirst();
            }
            if (bucket.size() >= limit) {
                return false;
            }
            bucket.addLast(now);
            return true;
        }
    }

    private String clientKey(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(forwardedFor)) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (StringUtils.hasText(value)) {
                return value;
            }
        }
        return null;
    }

    private int normalizePort(String scheme, int port) {
        if (port > 0) {
            return port;
        }
        if ("https".equalsIgnoreCase(scheme)) {
            return 443;
        }
        return 80;
    }
}
