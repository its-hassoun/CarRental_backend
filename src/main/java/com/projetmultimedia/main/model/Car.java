package com.projetmultimedia.main.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car {

    private static final Logger logger = LoggerFactory.getLogger(Car.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;
    
    @Column( columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean available;
    
    
    @Column
    private String gov;
    
    @ElementCollection
    @CollectionTable(
        name = "car_images",
        joinColumns = @JoinColumn(name = "car_id")
    )
    @Column(name = "image_url")
    @Size(max = 5) // Correct placement for collection size validation
    private List<String> imageUrls = new ArrayList<>();

    @Column(name = "agency_id", nullable = false)
    private Long agencyId;

    @Column(name = "price_per_day", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerDay;

    @PrePersist
    public void prePersist() {
        logger.debug("Persisting car with releaseDate: {}", releaseDate);

        if (releaseDate == null) {
            logger.error("Null releaseDate for car {}", id);
            throw new IllegalStateException("Release date cannot be null");
        }

        if (pricePerDay == null || pricePerDay.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Price must be positive");
        }
    }

    // Constructors
    public Car() {}

    public Car(String brand, String model, LocalDate releaseDate,
              BigDecimal pricePerDay, Long agencyId,boolean available,String gov) {
        this.brand = brand;
        this.model = model;
        this.releaseDate = releaseDate;
        this.pricePerDay = pricePerDay;
        this.agencyId = agencyId;
        this.available=available;
        this.gov=gov;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
    
    
    public String getGov() {
        return gov;
    }

    public void setGov(String gov) {
        this.gov = gov;
    }
    
    
    
    
    
    

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Long getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Long agencyId) {
        this.agencyId = agencyId;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }
    
    public boolean getAv() {
        return available;
    }
    
    
    public void setAv(boolean available) {
        this.available = available;}


    // Business methods
    public void addImage(String imageUrl) {
        if (this.imageUrls.size() >= 5) {
            throw new IllegalStateException("Cannot add more than 5 images");
        }
        this.imageUrls.add(imageUrl);
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", releaseDate=" + releaseDate +
                ", imageUrls=" + imageUrls +
                ", agencyId=" + agencyId +
                ", pricePerDay=" + pricePerDay +
                ", Governorate=" + gov +
                ", available=" + available + // Include the new attribute in toString
                '}';
    }
}
