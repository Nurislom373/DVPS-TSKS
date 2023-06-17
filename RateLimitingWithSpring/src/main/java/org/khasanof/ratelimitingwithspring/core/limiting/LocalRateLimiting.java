package org.khasanof.ratelimitingwithspring.core.limiting;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

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
@RedisHash("local")
public class LocalRateLimiting implements Serializable {

    private Long undiminishedCount;

    private Long token;

    private Duration duration;

    private boolean noLimit;

    private Long refillCount;

    private Instant createdAt;

    private Instant refillUpdatedAt;

    private Instant updatedAt;

}
