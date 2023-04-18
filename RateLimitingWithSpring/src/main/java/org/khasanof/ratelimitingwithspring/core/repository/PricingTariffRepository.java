package org.khasanof.ratelimitingwithspring.core.repository;

import org.khasanof.ratelimitingwithspring.core.domain.PricingTariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: Nurislom
 * <br/>
 * Date: 4/19/2023
 * <br/>
 * Time: 12:49 AM
 * <br/>
 * Package: org.khasanof.ratelimitingwithspring.core.repository
 */
@Repository
public interface PricingTariffRepository extends JpaRepository<PricingTariff, Long> {
}
