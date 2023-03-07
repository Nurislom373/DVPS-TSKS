package org.khasanof.uicachingstrategy.config;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.khasanof.uicachingstrategy.domain.TransactionEntity;
import org.khasanof.uicachingstrategy.enums.FromToEnum;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

/**
 * Author: Nurislom
 * <br/>
 * Date: 2/28/2023
 * <br/>
 * Time: 5:19 PM
 * <br/>
 * Package: org.khasanof.uicaching.config
 */
@Component
public class CacheHelper {

    private CacheManager cacheManager;
    private final Cache<Long, TransactionEntity> entries;

    public CacheHelper() {
        cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
        cacheManager.init();

        entries = cacheManager.createCache("caches", CacheConfigurationBuilder
                .newCacheConfigurationBuilder(Long.class, TransactionEntity.class, ResourcePoolsBuilder.heap(200))
                .withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofMinutes(30))).build());
    }

    public List<TransactionEntity> getAllTransactions(LocalDateTime from, LocalDateTime to) {
        return StreamSupport.stream(entries.spliterator(), false)
                .map(Cache.Entry::getValue).filter((f) -> f.getCreatedAt().isAfter(from)
                        && f.getCreatedAt().isBefore(to)).toList();
    }

    public void clearCache() {
        entries.clear();
    }

    public void addAllTransactionCache(List<TransactionEntity> list) {
        list.forEach(this::addCache);
    }

    public boolean isEmpty() {
        return StreamSupport.stream(entries.spliterator(), false).findAny().isEmpty();
    }

    public boolean cacheContainKey(Long key) {
        return entries.containsKey(key);
    }

    public boolean cacheContainsAllKey(List<Long> keys) {
        return keys.stream().allMatch(entries::containsKey);
    }

    public Cache<Long, TransactionEntity> getEntries() {
        return entries;
    }

    public Map<FromToEnum, LocalDateTime> getFromToDate() {
        List<TransactionEntity> list = StreamSupport.stream(entries.spliterator(), false)
                .map(Cache.Entry::getValue)
                .sorted(Comparator.comparing(TransactionEntity::getCreatedAt))
                .toList();
        return new HashMap<>(){{
           put(FromToEnum.FROM, list.get(0).getCreatedAt());
           put(FromToEnum.TO, list.get(list.size() - 1).getCreatedAt());
        }};
    }

    public TransactionEntity findById(Long id) {
        return entries.get(id);
    }

    public void addCache(TransactionEntity entity) {
        entries.putIfAbsent(entity.getId(), entity);
    }

    public void updateCache(TransactionEntity entity) {
        entries.replace(entity.getId(), entity);
    }
}
