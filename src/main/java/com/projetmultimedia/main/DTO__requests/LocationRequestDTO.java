package com.projetmultimedia.main.DTO__requests;

import java.time.LocalDate;

public class LocationRequestDTO {
    private Long clientId;
    private Long carId;
    private LocalDate startDate;
    private LocalDate endDate;
	public Long getClientId() {
		return clientId;
	}
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	public Long getCarId() {
		return carId;
	}
	public void setCarId(Long carId) {
		this.carId = carId;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

    // Getters and setters
}
