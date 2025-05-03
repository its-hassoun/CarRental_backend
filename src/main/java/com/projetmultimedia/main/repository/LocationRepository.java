package com.projetmultimedia.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projetmultimedia.main.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findById(Long LocationId);
    List<Location> findByAgencyId(Long agencyId);

    List<Location> findByClientId(Long clientId);

}
