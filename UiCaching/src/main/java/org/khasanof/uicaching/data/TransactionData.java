package org.khasanof.uicaching.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.khasanof.uicaching.config.CacheHelper;
import org.khasanof.uicaching.domain.TransactionEntity;
import org.khasanof.uicaching.enums.Status;
import org.khasanof.uicaching.repository.TransactionRepository;
import org.khasanof.uicaching.utils.BaseUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 2/28/2023
 * <br/>
 * Time: 5:45 PM
 * <br/>
 * Package: org.khasanof.uicaching.data
 */
//@Component
@RequiredArgsConstructor
public class TransactionData implements CommandLineRunner {

    private final TransactionRepository repository;
    private final CacheHelper cacheHelper;

    @Override
    public void run(String... args) throws Exception {
        saveDatabase();
    }

    private void saveDatabase() {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<TransactionEntity>> reference = new TypeReference<>() {};

        try (InputStream inputStream = getClass().getResourceAsStream("/data/cache_entity.json")) {
            List<TransactionEntity> list = objectMapper.readValue(inputStream, reference);

            list.forEach(obj -> {
                obj.setStatus(Status.findAny());
                obj.setCreatedAt(BaseUtils.getRandomLocalDateToBetween(1, 10));
                cacheHelper.addCache(obj);
                repository.save(obj);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Need to once run!
    }

}
