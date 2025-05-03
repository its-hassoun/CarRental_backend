package com.projetmultimedia.main.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.projetmultimedia.main.model.Car;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByAgencyId(Long agencyId);
    List<Car> findByGov(String gov);
    
    
    @Query("SELECT c FROM Car c WHERE c.available = true " +
    	       "AND (:gov IS NULL OR LOWER(c.gov) = LOWER(:gov)) " +
    	       "AND (:brand IS NULL OR LOWER(c.brand) = LOWER(:brand)) " +
    	       "AND (:minPrice IS NULL OR c.pricePerDay >= :minPrice) " +
    	       "AND (:maxPrice IS NULL OR c.pricePerDay <= :maxPrice)")
    	List<Car> findAvailableCarsByFilters(
    	    @Param("gov") String gov,
    	    @Param("brand") String brand,
    	    @Param("minPrice") BigDecimal minPrice,
    	    @Param("maxPrice") BigDecimal maxPrice
    	);

}
