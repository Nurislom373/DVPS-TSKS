package org.khasanof.ratelimitingwithspring.core.limiting;

import io.github.bucket4j.Bucket;
import lombok.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/14/2023
 * <br/>
 * Time: 8:20 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.limiting
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class LocalRateLimiting {

    private Long tokenCount;

    private Long token;

    private Duration duration;

    private boolean noLimit;

    private Long refillCount;

    private Bucket bucket;

    private Instant createdAt;


}
