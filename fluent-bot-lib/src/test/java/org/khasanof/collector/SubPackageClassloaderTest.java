package org.khasanof.collector;

import org.junit.jupiter.api.Test;
import org.khasanof.core.collector.flattenPackage.FlattenPackageCollector;

import java.io.IOException;
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

    FlattenPackageCollector subPackageClassCollector = new FlattenPackageCollector();

    @Test
    void getClassWithPackageNameTest() throws IOException {
        Set<String> set = subPackageClassCollector.getFolder("org.khasanof");
        assertNotNull(set);
    }

}
