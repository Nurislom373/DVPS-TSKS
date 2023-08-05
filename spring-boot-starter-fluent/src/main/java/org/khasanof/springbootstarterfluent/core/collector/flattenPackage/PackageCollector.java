package org.khasanof.springbootstarterfluent.core.collector.flattenPackage;

import java.util.Set;

/**
 * Author: Nurislom
 * <br/>
 * Date: 23.06.2023
 * <br/>
 * Time: 22:39
 * <br/>
 * Package: org.khasanof.core.collector.flattenPackage
 */
public interface PackageCollector {

    Set<String> getFolders(String basePackageName);

}
