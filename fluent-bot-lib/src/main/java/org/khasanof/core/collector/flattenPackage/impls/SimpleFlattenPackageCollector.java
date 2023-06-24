package org.khasanof.core.collector.flattenPackage.impls;

import org.khasanof.core.collector.flattenPackage.PackageCollector;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Author: Nurislom
 * <br/>
 * Date: 22.06.2023
 * <br/>
 * Time: 20:54
 * <br/>
 * Package: org.khasanof.core.collector
 */
public class SimpleFlattenPackageCollector implements PackageCollector {

    @Override
    public Set<String> getFolders(String packageName) {
        return execute(packageName);
    }

    private Set<String> execute(String packageName) {
        final Map<Boolean, Set<String>> folders = new ConcurrentHashMap<>() {{
            put(false, new HashSet<>() {{
                add(packageName.replaceAll("[.]", "/"));
            }});
        }};

        while (Objects.nonNull(folders.get(false)) && !folders.get(false).isEmpty()) {
            folders.get(false).stream()
                    .map(path -> {
                        fileProcessing(folders);
                        InputStream stream = ClassLoader.getSystemClassLoader()
                                .getResourceAsStream(path);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                        return reader.lines()
                                .filter(line -> !line.contains("."))
                                .map(line -> path.concat("/" + line))
                                .peek(out -> System.out.println("package : " + out))
                                .collect(Collectors.toSet());
                    }).forEach(set -> {
                        if (!set.isEmpty()) {
                            folders.put(false, set);
                        }
                    });
        }

        return folders.get(true);
    }

    private void fileProcessing(Map<Boolean, Set<String>> folders) {
        if (folders.containsKey(true)) {
            if (folders.containsKey(false)) {
                folders.get(true).addAll(folders.get(false));
            }
        } else {
            folders.put(true, folders.get(false));
        }
        folders.remove(false);
    }

}
