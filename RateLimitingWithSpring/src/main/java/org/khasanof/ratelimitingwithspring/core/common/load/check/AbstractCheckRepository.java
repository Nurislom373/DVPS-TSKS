package org.khasanof.ratelimitingwithspring.core.common.load.check;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/21/2023
 * <br/>
 * Time: 6:53 PM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.common.load
 */
@RequiredArgsConstructor
public abstract class AbstractCheckRepository<R> {

    protected final R repository;

    abstract public boolean check();

}
