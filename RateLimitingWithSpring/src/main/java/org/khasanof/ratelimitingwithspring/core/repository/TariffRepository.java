package org.khasanof.ratelimitingwithspring.core.repository;

import org.khasanof.ratelimitingwithspring.core.domain.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
public interface TariffRepository extends JpaRepository<Tariff, Long> {

    Optional<Tariff> findByName(String name);

}
