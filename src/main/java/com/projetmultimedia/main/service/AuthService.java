package com.projetmultimedia.main.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projetmultimedia.main.DTO__requests.LoginRequest;
import com.projetmultimedia.main.DTO__requests.RegisterRequest;
import com.projetmultimedia.main.model.User;
import com.projetmultimedia.main.repository.UserRepository;
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<User> register(RegisterRequest request) {
        if (!request.getEmail().contains("@") || !request.getEmail().contains(".")) {
            return ResponseEntity.badRequest().build();
        }

        if (request.getPassword() == null || request.getPassword().length() < 8) {
            return ResponseEntity.badRequest().build();
        }

        if (request.getAge() < 21) {
            return ResponseEntity.badRequest().build();
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(409).build();
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setAge(request.getAge());
        user.setgovernorate(request.getGovernorate());
        user.setRole("CLIENT");

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    public ResponseEntity<User> login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(user);
    }
}
