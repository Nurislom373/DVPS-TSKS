package org.khasanof.ratelimitingwithspring.cache.ehcache;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.khasanof.ratelimitingwithspring.cache.CacheOperations;
import org.khasanof.ratelimitingwithspring.cache.CacheUtilityGD;
import org.khasanof.ratelimitingwithspring.cache.CacheUtilityAUD;
import org.khasanof.ratelimitingwithspring.cache.MergeValue;
import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
import org.khasanof.ratelimitingwithspring.core.limiting.LocalRateLimiting;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/23/2023
 * <br/>
 * Time: 9:48 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.cache
 */
@Slf4j
@Component
public class EhCacheService implements CacheOperations {

    private CacheManager cacheManager;
    private final Cache<String, MergeValue> entries;

    public EhCacheService() {
        cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
        cacheManager.init();

        CacheManagerBuilder<PersistentCacheManager> managerBuilder = CacheManagerBuilder
                .newCacheManagerBuilder()
                .with(CacheManagerBuilder.persistence(new File("java.io.tmpdir/myDiskStore")))
                .withCache("limits",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, MergeValue.class,
                                        ResourcePoolsBuilder.newResourcePoolsBuilder()
                                                .heap(20, EntryUnit.ENTRIES)
                                                .offheap(20, MemoryUnit.MB))
                                .build());

        PersistentCacheManager persistentCacheManager = managerBuilder.build();
        persistentCacheManager.init();

        entries = persistentCacheManager.getCache("limits", String.class, MergeValue.class);
    }

    @Override
    public void addValues(String key, Map<PTA, LocalRateLimiting> limitingMap) {
        entries.put(key, new MergeValue(limitingMap));
    }

    @Override
    public void updateValues(String key, PTA pta, LocalRateLimiting limiting) {
        if (entries.containsKey(key)) {
            MergeValue value = entries.get(key);
            entries.remove(key);
            value.getMap().put(pta, limiting);
            entries.put(key, value);
        }
    }

    @Override
    public boolean delete(String key) {
        entries.remove(key);
        return true;
    }

    @Override
    public Optional<MergeValue> getValues(String key) {
        return Optional.of(entries.get(key));
    }

    @Override
    public Map<String, Map<PTA, LocalRateLimiting>> getAll() {
        return StreamSupport.stream(entries.spliterator(), true)
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue().getMap()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteAll() {
        entries.clear();
    }
}
