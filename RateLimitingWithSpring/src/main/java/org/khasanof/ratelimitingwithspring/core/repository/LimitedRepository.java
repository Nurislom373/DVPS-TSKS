package org.khasanof.ratelimitingwithspring.core.repository;

import org.khasanof.ratelimitingwithspring.core.common.save.REGSLimit;
import org.khasanof.ratelimitingwithspring.core.domain.Limited;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/19/2023
 * <br/>
 * Time: 12:48 AM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.repository
 */
@Repository
public interface LimitedRepository extends JpaRepository<Limited, Long> {

    @Query(value = "FROM Limited e where e.plan = ?1 and (e.api.url = ?2 and e.api.method = ?3)")
    List<Limited> findByREGSLimit(String plan, String url, RequestMethod method);

}
