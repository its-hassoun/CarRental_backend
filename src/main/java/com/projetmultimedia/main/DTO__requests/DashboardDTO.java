package com.projetmultimedia.main.DTO__requests;

import java.util.Map;

public class DashboardDTO {
    private Map<Integer, Long> ageHistogram;
    private Map<String, Long> carBrandDistribution;
    private Map<String, Long> locationsPerMonth;
    public Map<Integer, Long> getAgeHistogram() {
        return ageHistogram;
    }
    public void setAgeHistogram(Map<Integer, Long> ageHistogram) {
        this.ageHistogram = ageHistogram;
    }
    public Map<String, Long> getCarBrandDistribution() {
        return carBrandDistribution;
    }
    public void setCarBrandDistribution(Map<String, Long> carBrandDistribution) {
        this.carBrandDistribution = carBrandDistribution;
    }
    public Map<String, Long> getLocationsPerMonth() {
        return locationsPerMonth;
    }
    public void setLocationsPerMonth(Map<String, Long> locationsPerMonth) {
        this.locationsPerMonth = locationsPerMonth;
    }

}
