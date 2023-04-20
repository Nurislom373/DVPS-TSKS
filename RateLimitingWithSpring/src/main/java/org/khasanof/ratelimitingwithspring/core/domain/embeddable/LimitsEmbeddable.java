package org.khasanof.ratelimitingwithspring.core.domain.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.khasanof.ratelimitingwithspring.core.domain.enums.RequestType;
import org.khasanof.ratelimitingwithspring.core.domain.enums.TimeType;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/18/2023
 * <br/>
 * Time: 2:45 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.domain
 */
@Getter
@Setter
@Builder
@ToString
@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class LimitsEmbeddable {

    @Column(name = "undiminished_count", nullable = false)
    private Long undiminishedCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", nullable = false)
    private RequestType requestType;

    @Column(name = "request_count", nullable = false)
    private Long requestCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "time_type", nullable = false)
    private TimeType timeType;

    @Column(name = "time_count", nullable = false)
    private Long timeCount;
}
