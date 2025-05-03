package com.projetmultimedia.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projetmultimedia.main.DTO__requests.LocationRequestDTO;
import com.projetmultimedia.main.DTO__requests.LocationResponseDTO;
import com.projetmultimedia.main.model.Location;
import com.projetmultimedia.main.service.LocationService;

@RestController

@RequestMapping("/locations")
@CrossOrigin(origins = "http://localhost:4200/", allowCredentials = "true")
public class LocationController {

    @Autowired
    private LocationService locationService;
    @CrossOrigin(origins = "http://localhost:4200/", allowCredentials = "true")
    @PostMapping
    public ResponseEntity<LocationResponseDTO> createLocation(@RequestBody LocationRequestDTO dto) {
        try {
            LocationResponseDTO response = locationService.createLocation(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    
        
        

    }
    @CrossOrigin(origins = "http://localhost:4200/", allowCredentials = "true")
    @GetMapping("/agency/{agencyId}")
    public ResponseEntity<List<Location>> getLocationsByAgencyId(@PathVariable Long agencyId) {
        return ResponseEntity.ok(locationService.getLocationsByAgencyId(agencyId));
    }
    @CrossOrigin(origins = "http://localhost:4200/", allowCredentials = "true")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Location>> getLocationsByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(locationService.getLocationsByClientId(clientId));
    }


}
