//package org.khasanof.ratelimitingwithspring.cache;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import lombok.extern.slf4j.Slf4j;
//import org.ehcache.Cache;
//import org.ehcache.CacheManager;
//import org.ehcache.PersistentCacheManager;
//import org.ehcache.config.builders.CacheConfigurationBuilder;
//import org.ehcache.config.builders.CacheManagerBuilder;
//import org.ehcache.config.builders.ExpiryPolicyBuilder;
//import org.ehcache.config.builders.ResourcePoolsBuilder;
//import org.ehcache.config.units.EntryUnit;
//import org.ehcache.config.units.MemoryUnit;
//import org.khasanof.ratelimitingwithspring.core.RateLimiting;
//import org.khasanof.ratelimitingwithspring.core.common.search.classes.PTA;
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//import java.time.Duration;
//import java.util.Map;
//
///**
// * Author: Nurislom
// * <br/>
// * Date: 4/23/2023
// * <br/>
// * Time: 9:48 PM
// * <br/>
// * Package: org.khasanof.ratelimitingwithspring.cache
// */
//@Slf4j
//@Component
//public class EhCacheService {
//
//    private CacheManager cacheManager;
//
//    public EhCacheService() {
////        cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
////        cacheManager.init();
//
////        CacheManagerBuilder<PersistentCacheManager> managerBuilder = CacheManagerBuilder
////                .newCacheManagerBuilder()
////                .withSerializer(MergeValue.class, MergeValueSerializer.class)
////                .with(CacheManagerBuilder.persistence(new File("java.io.tmpdir/myDiskStore")))
////                .withCache("limits",
////                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, MergeValue.class,
////                                        ResourcePoolsBuilder.newResourcePoolsBuilder()
////                                                .heap(20, EntryUnit.ENTRIES)
////                                                .offheap(20, MemoryUnit.MB))
////                                .build());
////
////        PersistentCacheManager persistentCacheManager = managerBuilder.build();
////        persistentCacheManager.init();
////
////        entries = persistentCacheManager.getCache("limits", String.class, MergeValue.class);
//
////        entries = cacheManager.createCache("limits", CacheConfigurationBuilder
////                .newCacheConfigurationBuilder(String.class, Map.class, ResourcePoolsBuilder.heap(200))
////                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofHours(1)))
////                .build());
//    }
//
//    public void add(String key, Map<PTA, RateLimiting> map) {
//        entries.put(key, new MergeValue(key, map));
//    }
//
//    public Map<PTA, RateLimiting> show(String key) {
//        return entries.get(key).getMap();
//    }
//
//}
