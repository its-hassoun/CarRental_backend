package com.projetmultimedia.main.controller;
import com.projetmultimedia.main.DTO__requests.CarDTO;
import com.projetmultimedia.main.DTO__requests.CarUpdateDTO;
import com.projetmultimedia.main.DTO__requests.DashboardDTO;
import com.projetmultimedia.main.model.Car;
import com.projetmultimedia.main.repository.CarRepository;
import com.projetmultimedia.main.service.AgenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200/", allowCredentials = "true")
@RequestMapping("/agency")
public class AgencyController {

	
	@Autowired
    private CarRepository carRepository;
	
    @Autowired
    private AgenceService carService;
    
    @CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
    @PostMapping(value = "/addcar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addCar(
            @RequestParam("brand") String brand,
            @RequestParam("model") String model,
            @RequestParam("releaseDate") String releaseDate,
            @RequestParam("pricePerDay") BigDecimal pricePerDay,
            @RequestParam("userId") Long userId,
            @RequestParam("imageUrls") MultipartFile[] imageFiles
    ) {
        try {
            // Directory to save images in the static folder
            String uploadDir = "src/main/resources/static/assets/images";  // Path to save images in the 'static' folder
            File uploadDirectory = new File(uploadDir);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs(); // Create directory if it doesn't exist
            }
            // Process image files and save them to disk
            List<String> imageNames = new ArrayList<>();
            for (MultipartFile imageFile : imageFiles) {
                String imageName = imageFile.getOriginalFilename();
                String imagePath = uploadDir + "/" + imageName;

                // Ensure you are using java.nio.file.Path
                Path path = Paths.get(imagePath);

                // Save the image file
                Files.write(path, imageFile.getBytes()); // Save the image to disk

                // Add the relative image URL (accessible from frontend)
                imageNames.add("assets/images/" + imageName);  // Relative path for the frontend
            }

            // Create CarDTO object and set car information
            CarDTO carDTO = new CarDTO();
            carDTO.setBrand(brand);
            carDTO.setModel(model);
            carDTO.setReleaseDate(releaseDate);
            carDTO.setPricePerDay(pricePerDay);
            carDTO.setImageUrls(imageNames);

            // Save the car entity to the database
            Car car = carService.addCarForUser(carDTO, userId);

            // Return response with car object
            return ResponseEntity.ok(car);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error adding car with images");
        }
    }    
    
    @CrossOrigin(origins = "http://localhost:4200/", allowCredentials = "true")
    @GetMapping("/{agencyId}/cars")
    public ResponseEntity<?> getCarsByAgencyId(@PathVariable Long agencyId) {
        try {
            List<Car> cars = carService.getCarsByAgencyId(agencyId);
            return ResponseEntity.ok(cars);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to fetch cars for agency");
        }
    }
    @CrossOrigin(origins = "http://localhost:4200/", allowCredentials = "true")
    @GetMapping("/car/{carId}")
    public ResponseEntity<?> getCarById(@PathVariable Long carId) {
        try {
            Car car = carService.getCarById(carId);
            return ResponseEntity.ok(car);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to fetch car details");
        }
    }
    
    
    @CrossOrigin(origins = "http://localhost:4200/", allowCredentials = "true")
    @GetMapping("/cars/governorate/{gov}")
    public ResponseEntity<?> getCarsByGovernorate(@PathVariable String gov) {
        try {
            List<Car> cars = carService.getCarsByGovernorate(gov);
            return ResponseEntity.ok(cars);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to fetch cars by governorate");
        }
    }
    
    
    
    @CrossOrigin(origins = "http://localhost:4200/", allowCredentials = "true")
    @PatchMapping("/car/{carId}")
    public Car updateCarPartially(@PathVariable Long carId,
            @ModelAttribute CarUpdateDTO dto) {
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
                        // Use original filename only
                        String imageName = file.getOriginalFilename();
                        Path savePath = Paths.get(uploadDir, imageName);

                        // Ensure the directory exists
                        Files.createDirectories(savePath.getParent());

                        // Save the file
                        Files.write(savePath, file.getBytes());

                        // Store relative path to access from frontend
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
    
    
    @CrossOrigin(origins = "http://localhost:4200/", allowCredentials = "true")
    @GetMapping("/cars/available")
    public ResponseEntity<?> getAvailableCarsWithFilters(
            @RequestParam(required = false) String gov,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice) {
        try {
            List<Car> cars = carService.getAvailableCarsByFilters(gov, brand, minPrice, maxPrice);
            return ResponseEntity.ok(cars);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to fetch filtered available cars");
        }
    }

    
    @CrossOrigin(origins = "http://localhost:4200/", allowCredentials = "true")
    @GetMapping("/{agencyId}/dashboard")
    public ResponseEntity<DashboardDTO> getDashboardData(@PathVariable Long agencyId) {
        DashboardDTO dashboard = carService.getDashboardData(agencyId);
        return ResponseEntity.ok(dashboard);
    }

    
}