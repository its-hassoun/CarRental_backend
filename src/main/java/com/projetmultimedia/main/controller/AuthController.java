package com.projetmultimedia.main.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projetmultimedia.main.DTO__requests.LoginRequest;
import com.projetmultimedia.main.DTO__requests.RegisterRequest;
import com.projetmultimedia.main.model.User;
import com.projetmultimedia.main.service.AuthService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200/", allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @CrossOrigin(origins = "http://localhost:4200/", allowCredentials = "true")
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }
    @CrossOrigin(origins = "http://localhost:4200/", allowCredentials = "true")
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
