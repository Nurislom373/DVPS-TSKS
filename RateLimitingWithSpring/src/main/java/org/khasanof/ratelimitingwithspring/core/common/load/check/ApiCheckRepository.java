package org.khasanof.ratelimitingwithspring.core.common.load.check;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.repository.ApiRepository;
import org.springframework.stereotype.Service;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/21/2023
 * <br/>
 * Time: 6:55 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.load.check
 */
@Slf4j
@Service
public class ApiCheckRepository extends AbstractCheckRepository<ApiRepository> {

    public ApiCheckRepository(ApiRepository repository) {
        super(repository);
    }

    @Override
    public boolean check() {
        try {
            long count = repository.count();
            return count >= 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
