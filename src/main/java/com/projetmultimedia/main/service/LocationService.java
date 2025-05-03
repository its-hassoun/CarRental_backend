package com.projetmultimedia.main.service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projetmultimedia.main.DTO__requests.LocationRequestDTO;
import com.projetmultimedia.main.DTO__requests.LocationResponseDTO;
import com.projetmultimedia.main.model.Car;
import com.projetmultimedia.main.model.Location;
import com.projetmultimedia.main.repository.CarRepository;
import com.projetmultimedia.main.repository.LocationRepository;

@Service
public class LocationService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private LocationRepository locationRepository;

    public LocationResponseDTO createLocation(LocationRequestDTO dto) {
        Car car = carRepository.findById(dto.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found"));

        // Get price and agency ID from the car
        BigDecimal pricePerDay = car.getPricePerDay();
        Long agencyId = car.getAgencyId();

        // Calculate days between start and end
        long days = ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate())+1;
        if (days <= 0) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        BigDecimal totalAmount = pricePerDay.multiply(BigDecimal.valueOf(days));

        // Create Location entity
        Location location = new Location();
        location.setClientId(dto.getClientId());
        location.setCarId(dto.getCarId());
        location.setAgencyId(agencyId);
        location.setStartDate(dto.getStartDate());
        location.setEndDate(dto.getEndDate());
        car.setAv(false);
        
        // Save and return
        Location savedLocation = locationRepository.save(location);
        return new LocationResponseDTO(savedLocation, totalAmount);
    }
    	
    public List<Location> getLocationsByAgencyId(Long agencyId) {
        return locationRepository.findByAgencyId(agencyId);
    }

    public List<Location> getLocationsByClientId(Long clientId) {
        return locationRepository.findByClientId(clientId);
    }


}
