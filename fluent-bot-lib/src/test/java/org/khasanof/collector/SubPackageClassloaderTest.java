package org.khasanof.collector;

import org.junit.jupiter.api.Test;
import org.khasanof.core.collector.flattenPackage.impls.SimpleFlattenPackageCollector;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Nurislom
 * <br/>
 * Date: 22.06.2023
 * <br/>
 * Time: 21:57
 * <br/>
 * Package: org.khasanof.collector
 */
public class SubPackageClassloaderTest {

    SimpleFlattenPackageCollector subPackageClassCollector = new SimpleFlattenPackageCollector();

    @Test
    void getClassWithPackageNameTest() {
        Set<String> set = subPackageClassCollector.getFolders("org.khasanof");
        set.forEach(System.out::println);
        assertNotNull(set);
        assertEquals(set.size(), 8);
    }

}
