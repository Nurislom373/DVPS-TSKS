package org.khasanof.springbootstarterfluent.core.collector.flattenPackage.impls;

import org.khasanof.springbootstarterfluent.core.collector.flattenPackage.ForkJoinFlattenPCL;
import org.khasanof.springbootstarterfluent.core.collector.flattenPackage.PackageCollector;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ForkJoinPool;

/**
 * Author: Nurislom
 * <br/>
 * Date: 23.06.2023
 * <br/>
 * Time: 22:42
 * <br/>
 * Package: org.khasanof.core.collector.flattenPackage
 */
@Component
public class RecursiveFlattenPackageCollector implements PackageCollector {

    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    @Override
    public Set<String> getFolders(String basePackageName) {
        ForkJoinFlattenPCL joinFlattenPCL = new ForkJoinFlattenPCL(basePackageName);
        forkJoinPool.execute(joinFlattenPCL);
        return joinFlattenPCL.join();
    }


}
