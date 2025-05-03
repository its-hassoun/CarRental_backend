package com.projetmultimedia.main.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Long carId;

    @Column(nullable = false)
    private Long agencyId; 

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    // Constructors
    public Location() {
    }

    public Location(Long id, Long clientId, Long carId, Long agencyId, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.clientId = clientId;
        this.carId = carId;
        this.agencyId = agencyId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getClientId() {
        return clientId;
    }

    public Long getCarId() {
        return carId;
    }

    public Long getAgencyId() {
        return agencyId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public void setAgencyId(Long agencyId) {
        this.agencyId = agencyId;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}