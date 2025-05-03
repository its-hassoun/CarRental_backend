package com.projetmultimedia.main.DTO__requests;

import java.math.BigDecimal;

import com.projetmultimedia.main.model.Location;

public class LocationResponseDTO {
    private Location location;
    private BigDecimal amount;

    public LocationResponseDTO(Location location, BigDecimal amount) {
        this.location = location;
        this.amount = amount;
    }

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

 
}
