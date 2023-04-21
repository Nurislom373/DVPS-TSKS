package org.khasanof.ratelimitingwithspring.core.common.load.check;

import lombok.extern.slf4j.Slf4j;
import org.khasanof.ratelimitingwithspring.core.repository.TariffRepository;
import org.springframework.stereotype.Service;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/21/2023
 * <br/>
 * Time: 7:01 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.load.check
 */
@Slf4j
@Service
public class TariffCheckRepository extends AbstractCheckRepository<TariffRepository> {

    public TariffCheckRepository(TariffRepository repository) {
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
