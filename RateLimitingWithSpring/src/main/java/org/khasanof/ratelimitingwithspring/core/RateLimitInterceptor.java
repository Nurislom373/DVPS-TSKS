package org.khasanof.ratelimitingwithspring.core;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.common.CommonLimitsService;
import org.khasanof.ratelimitingwithspring.core.limiting.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    @Value("${api.limit.key}")
    private String KEY;
    private static final String HEADER_API_KEY = "X-api-key";
    private static final String HEADER_LIMIT_REMAINING = "X-Rate-Limit-Remaining";
    private static final String HEADER_RETRY_AFTER = "X-Rate-Limit-Retry-After-Seconds";
    private final CommonLimitsService commonLimitsService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String bearerToken = authorizationHeader.substring("Bearer".length());

                DecodedJWT jwt = JWTUtils.getVerifier().verify(bearerToken);
                String id = jwt.getClaim(KEY).as(String.class);

                if (Objects.isNull(id)) {
                    throw new RuntimeException("Invalid Token");
                }

                String requestRequestURI = request.getRequestURI();
                String method = request.getMethod();
                Map<String, String> requestAttribute = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

                RateLimiting rateLimiting = commonLimitsService.getKeyAndUrl(id, requestRequestURI, method);

                if (rateLimiting.consumeRequest()) {

                    log.info("available Tokens: {}", rateLimiting.availableToken());

                    response.addHeader(HEADER_LIMIT_REMAINING, String.valueOf(rateLimiting.availableToken()));
                    return true;

                } else {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.addHeader(HEADER_RETRY_AFTER, String.valueOf(TimeUnit.NANOSECONDS.toSeconds(
                            rateLimiting.getNanosToWaitForRefill()))); // new way
                    response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                            "You have exhausted your API Request"); // 429

                    return false;
                }

            } catch (Exception e) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
                return false;
            }
        } else {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            return false;
        }
    }


}
