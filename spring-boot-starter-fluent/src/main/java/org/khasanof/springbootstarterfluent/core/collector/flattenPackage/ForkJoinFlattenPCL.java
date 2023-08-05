package org.khasanof.springbootstarterfluent.core.collector.flattenPackage;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.RecursiveTask;

/**
 * Author: Nurislom
 * <br/>
 * Date: 23.06.2023
 * <br/>
 * Time: 21:34
 * <br/>
 * Package: org.khasanof.core.collector.flattenPackage
 */
public class ForkJoinFlattenPCL extends RecursiveTask<Set<String>> {

    private final String basePackage;
    private final List<ForkJoinFlattenPCL> forks = new ArrayList<>();

    public ForkJoinFlattenPCL(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    protected Set<String> compute() {
        final Set<String> folders = new HashSet<>();

        String replaced = basePackage.replaceAll("[.]", "/");
        folders.add(replaced);
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(replaced);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        List<String> findFolders = reader.lines()
                .filter(line -> !line.contains("."))
                .map(line -> replaced.concat("/" + line))
                .toList();

        if (!findFolders.isEmpty()) {
            findFolders.forEach(fl -> {
                ForkJoinFlattenPCL packageCollector = new ForkJoinFlattenPCL(fl);
                packageCollector.fork();
                forks.add(packageCollector);
            });
        } else {
            folders.add(basePackage);
        }

        addResultsFromForks(folders, forks);
        return folders;
    }

    private void addResultsFromForks(Set<String> folders, List<ForkJoinFlattenPCL> forks) {
        for (ForkJoinFlattenPCL fork : forks) {
            folders.addAll(fork.join());
        }
    }


}
