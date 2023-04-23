package org.khasanof.ratelimitingwithspring.core.repository;

import org.khasanof.ratelimitingwithspring.core.domain.Api;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/19/2023
 * <br/>
 * Time: 12:47 AM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.repository
 */
@Repository
public interface ApiRepository extends JpaRepository<Api, Long> {

    Optional<Api> findByUrlAndMethodAndAttributes(String url, RequestMethod method, Map<String, String> attributes);

    List<Api> findByUrlAndMethod(String url, RequestMethod method);

    Optional<Api> findApiByUrlAndMethod(String url, RequestMethod method);

    List<Api> findAllByIdIsIn(Collection<Long> id);

}
