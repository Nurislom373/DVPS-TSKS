package org.khasanof.reactivefilterutil.core;

import org.khasanof.reactivefilterutil.ReactiveFilterRepository;
import org.springframework.data.relational.core.query.Criteria;
import reactor.core.publisher.Flux;

/**
 * @author Nurislom
 * @see org.khasanof.reactivefilterutil.core
 * @since 02.08.2023 8:21
 */
public class SimpleReactiveFilterRepository<T, ID> implements ReactiveFilterRepository<T, ID> {

    @Override
    public Flux<T> findAll(Criteria criteria) {
        return null;
    }

}
