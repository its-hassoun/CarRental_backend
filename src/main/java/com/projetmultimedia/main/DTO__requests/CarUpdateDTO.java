package com.projetmultimedia.main.DTO__requests;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class CarUpdateDTO {

    private String brand;
    private String model;
    private String releaseDate;
    private BigDecimal pricePerDay;
    private List<MultipartFile> imageFiles;

    // Getters and setters

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

    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }
    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public List<MultipartFile> getImageFiles() {
        return imageFiles;
    }
    public void setImageFiles(List<MultipartFile> imageFiles) {
        this.imageFiles = imageFiles;
    }
}
