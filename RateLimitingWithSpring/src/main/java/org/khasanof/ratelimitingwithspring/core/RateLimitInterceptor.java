package org.khasanof.ratelimitingwithspring.core;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.common.CommonLimitsService;
import org.khasanof.ratelimitingwithspring.core.common.update.CommonUpdateOnRuntime;
import org.khasanof.ratelimitingwithspring.core.limiting.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.utils.JWTUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final String HEADER_LIMIT_REMAINING = "X-Rate-Limit-Remaining";
    private static final String HEADER_RETRY_AFTER = "X-Rate-Limit-Retry-After-Seconds";
    private final CommonLimitsService commonLimitsService;
    private final CommonUpdateOnRuntime commonUpdateOnRuntime;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            String bearerToken = authorizationHeader.substring("Bearer ".length());
            log.info("Bearer Token : {}", bearerToken);

            DecodedJWT jwt = JWTUtils.getVerifier()
                    .verify(bearerToken);

            String key = jwt.getClaim("key").as(String.class);
            log.info("key - {}", key);

            if (Objects.isNull(key)) {
                log.error("key is null!");
                throw new RuntimeException("Invalid Token");
            }

            String requestRequestURI = request.getRequestURI();
            String method = request.getMethod();
            Map<String, String> requestAttribute = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            log.info("Show Request Attributes");
            System.out.println("requestAttribute = " + requestAttribute);

            RateLimiting rateLimiting = commonLimitsService.searchKeys(key, requestRequestURI, method, requestAttribute);

            if (rateLimiting.consumeRequest()) {

                Long availableToken = rateLimiting.availableToken();
                log.info("availableToken - {}", availableToken);

                response.addHeader(HEADER_LIMIT_REMAINING, String.valueOf(availableToken));
                return true;

                // ...

            } else {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.addHeader(HEADER_RETRY_AFTER, String.valueOf(TimeUnit.NANOSECONDS.toSeconds(
                        rateLimiting.getNanosToWaitForRefill()))); // new way
                response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                        "You have exhausted your API Request"); // 429

                return false;
            }
        } else {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            return false;
        }
    }


}
