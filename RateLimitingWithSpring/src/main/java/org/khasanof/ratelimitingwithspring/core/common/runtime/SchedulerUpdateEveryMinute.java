package org.khasanof.ratelimitingwithspring.core.common.runtime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.RateLimiting;
import org.khasanof.ratelimitingwithspring.core.common.runtime.delete.DeleteOnRuntime;
import org.khasanof.ratelimitingwithspring.core.common.runtime.update.UpdateOnRuntime;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.utils.ConcurrentMapUtility;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

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
    private final DeleteOnRuntime deleteOnRuntime;
    private final ConcurrentMapUtility concurrentMapUtility;

    @Scheduled(fixedDelay = 30000)
    void run() {
        log.info("Start SchedulerUpdateEveryMinute");

        Map<String, Map<PTA, RateLimiting>> utilityAll = concurrentMapUtility.getAll();
        utilityAll.entrySet().forEach(System.out::println);
        log.info("List that has not yet been modified. size => Key Count : {}, Values Count : {}", utilityAll.size(),
                utilityAll.values().size());

        Map<String, Map<PTA, RateLimiting>> clearMap = deleteOnRuntime.delete(utilityAll);
        clearMap.entrySet().forEach(System.out::println);
        log.info("List that has been modified. size => Key Count : {}, Values Count : {}", clearMap.size(),
                clearMap.values().size());

        clearMap.forEach(updateOnRuntime::updateWithKey);
        log.warn("End SchedulerUpdateEveryMinute");

        concurrentMapUtility.getAll().entrySet().forEach(System.out::println);
        // write logic db connection failed write object with file.
    }
}
