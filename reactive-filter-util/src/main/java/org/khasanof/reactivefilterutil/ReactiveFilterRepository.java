package org.khasanof.reactivefilterutil;

import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;

/**
 * @author Nurislom
 * @see org.khasanof.reactivefilterutil
 * @since 31.07.2023 11:41
 */
@NoRepositoryBean
public interface ReactiveFilterRepository<T, ID> {

    Flux<T> findAll(Criteria criteria);

}
