package org.khasanof.ratelimitingwithspring.core.common.update;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.utils.ConcurrentMapUtility;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/20/2023
 * <br/>
 * Time: 3:42 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.updateOnRuntime
 */
@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerUpdateEveryMinute {

    private final UpdateOnRuntime updateOnRuntime;
    private final ConcurrentMapUtility concurrentMapUtility;
    private final HikariDataSource hikariDataSource;

    @Scheduled(fixedDelay = 30000)
    void run() {
        log.info("Start SchedulerUpdateEveryMinute");
        concurrentMapUtility.getAll().forEach(updateOnRuntime::updateWithKey);
        // write logic db connection failed write object with file.
    }
}
