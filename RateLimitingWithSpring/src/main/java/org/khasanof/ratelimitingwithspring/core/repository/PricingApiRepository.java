package org.khasanof.ratelimitingwithspring.core.repository;

import org.khasanof.ratelimitingwithspring.core.domain.PricingApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
public interface PricingApiRepository extends JpaRepository<PricingApi, Long> {
}
