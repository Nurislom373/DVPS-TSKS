package org.khasanof.collector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.khasanof.core.collector.flattenPackage.ForkJoinFlattenPCL;

import java.util.Set;
import java.util.concurrent.ForkJoinPool;

/**
 * Author: Nurislom
 * <br/>
 * Date: 23.06.2023
 * <br/>
 * Time: 22:30
 * <br/>
 * Package: org.khasanof.collector
 */
public class FKPackageCollectorTest {

    @Test
    void test() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        ForkJoinFlattenPCL packageCollector = new ForkJoinFlattenPCL("org.khasanof");

        forkJoinPool.execute(packageCollector);

        Set<String> strings = packageCollector.join();

        strings.forEach(System.out::println);

        Assertions.assertFalse(strings.isEmpty());
        Assertions.assertEquals(strings.size(), 8);
    }

}
