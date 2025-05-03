package com.projetmultimedia.main.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import com.projetmultimedia.main.DTO__requests.CarDTO;
import com.projetmultimedia.main.DTO__requests.CarUpdateDTO;
import com.projetmultimedia.main.DTO__requests.DashboardDTO;
import com.projetmultimedia.main.model.Car;
import com.projetmultimedia.main.model.Location;
import com.projetmultimedia.main.model.User;
import com.projetmultimedia.main.repository.CarRepository;
import com.projetmultimedia.main.repository.LocationRepository;
import com.projetmultimedia.main.repository.UserRepository;

import jakarta.validation.Valid;

@Service
@Validated
public class AgenceService {

	private final CarRepository carRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AgenceService.class);

    public AgenceService(CarRepository carRepository, UserRepository userRepository,LocationRepository locationRepository ) {
        this.carRepository = carRepository;
        this.locationRepository=locationRepository;
        this.userRepository = userRepository;
    }

    public Car addCarForUser(@Valid CarDTO carDTO, Long userId) {
        // Fetch user (who acts as the agency)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Long agencyId = user.getId();
        String userGov = user.getgovernorate();

        if (carDTO.getReleaseDate() == null || carDTO.getReleaseDate().isEmpty()) {
            throw new IllegalArgumentException("Release date is required");
        }

        try {
            LocalDate releaseDate = LocalDate.parse(carDTO.getReleaseDate());

            // Create Car object
            Car car = new Car();
            car.setBrand(carDTO.getBrand());
            car.setModel(carDTO.getModel());
            car.setPricePerDay(carDTO.getPricePerDay());
            car.setReleaseDate(releaseDate);
            car.setAgencyId(agencyId);
            car.setAv(true);
            car.setGov(userGov);

            // Handle image URLs
            List<String> imageUrls = new ArrayList<>();
            if (carDTO.getImageUrls() != null && !carDTO.getImageUrls().isEmpty()) {
                if (carDTO.getImageUrls().size() > 5) {
                    throw new IllegalArgumentException("Cannot upload more than 5 images");
                }
                imageUrls.addAll(carDTO.getImageUrls()); // Directly add the URLs from the DTO
                car.setImageUrls(imageUrls);
            }

            logger.debug("Saving car for agency {} with release date: {}", agencyId, releaseDate);
            return carRepository.save(car);

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-DD format");
        }
    }

    public List<Car> getCarsByAgencyId(Long agencyId) {
        return carRepository.findByAgencyId(agencyId);
    }
    
    public Car getCarById(Long carId) {
        // Fetch the car by its ID
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));

        
        return car;
    }
    
    public List<Car> getCarsByGovernorate(String governorate) {
        return carRepository.findByGov(governorate);
    }
    
    public Car updateCarPartially(Long carId, CarUpdateDTO dto) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));

        // Update simple fields
        if (dto.getBrand() != null) car.setBrand(dto.getBrand());
        if (dto.getModel() != null) car.setModel(dto.getModel());
        if (dto.getPricePerDay() != null) car.setPricePerDay(dto.getPricePerDay());

        if (dto.getReleaseDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            car.setReleaseDate(LocalDate.parse(dto.getReleaseDate(), formatter));
        }

        // Handle image replacement
        List<MultipartFile> newImages = dto.getImageFiles();
        if (newImages != null && !newImages.isEmpty()) {
            if (newImages.size() > 5) {
                throw new IllegalStateException("Cannot upload more than 5 images.");
            }

            String uploadDir = "src/main/resources/static/assets/images";

            // Delete old image files (optional - only if stored locally)
            for (String oldImage : car.getImageUrls()) {
                try {
                    Path oldPath = Paths.get("src/main/resources/static", oldImage);
                    Files.deleteIfExists(oldPath);
                } catch (IOException e) {
                    System.err.println("Failed to delete old image: " + oldImage);
                }
            }

            // Clear current images
            car.getImageUrls().clear();

            // Save new images
            List<String> savedImagePaths = new ArrayList<>();
            for (MultipartFile file : newImages) {
                if (!file.isEmpty()) {
                    try {
                        String imageName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                        Path savePath = Paths.get(uploadDir, imageName);
                        Files.createDirectories(savePath.getParent());
                        Files.write(savePath, file.getBytes());
                        savedImagePaths.add("assets/images/" + imageName);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to save image: " + file.getOriginalFilename(), e);
                    }
                }
            }

            car.setImageUrls(savedImagePaths);
        }

        return carRepository.save(car);
    }
    
    public List<Car> getAvailableCarsByFilters(String gov, String brand, BigDecimal minPrice, BigDecimal maxPrice) {
        return carRepository.findAvailableCarsByFilters(
            gov != null && !gov.isBlank() ? gov : null,
            brand != null && !brand.isBlank() ? brand : null,
            minPrice,
            maxPrice
        );
    }


    public DashboardDTO getDashboardData(Long agencyId) {
        List<Location> locations = locationRepository.findByAgencyId(agencyId);

        DashboardDTO dto = new DashboardDTO();
        Map<Integer, Long> ageHistogram = new HashMap<>();
        Map<String, Long> carBrandCount = new HashMap<>();
        Map<String, Long> locationsPerMonth = new HashMap<>();

        for (Location loc : locations) {
            // 1. Count per month
            if (loc.getStartDate() != null) {
                String monthKey = loc.getStartDate().getMonth() + "-" + loc.getStartDate().getYear();
                locationsPerMonth.merge(monthKey, 1L, Long::sum);
            }

            // 2. Age histogram
            userRepository.findById(loc.getClientId()).ifPresent(user ->
                ageHistogram.merge(user.getAge(), 1L, Long::sum)
            );

            // 3. Car brand count
            carRepository.findById(loc.getCarId()).ifPresent(car ->
                carBrandCount.merge(car.getBrand(), 1L, Long::sum)
            );
        }

        dto.setAgeHistogram(ageHistogram);
        dto.setCarBrandDistribution(carBrandCount);
        dto.setLocationsPerMonth(locationsPerMonth);
        return dto;
    }




}
