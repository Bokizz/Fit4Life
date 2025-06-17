package com.example.fit4life.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fit4life.model.User;
import com.example.fit4life.model.enumeration.Role;
import com.example.fit4life.repository.UserRepository;
import com.example.fit4life.security.CustomUserDetailsService;
import com.example.fit4life.security.JwtUtil;
import com.example.fit4life.security.TokenBlacklistService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");

            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            final String jwtToken = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(Map.of("jwt", jwtToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }
        user.setRole(Role.USER);
        
        user.setPassword(CustomUserDetailsService.hashPassword(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody String token){
        String jwtToken = token.replace("Bearer","");
        if(tokenBlacklistService.isTokenBlackListed(token)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token in blacklist already!");
        }
        tokenBlacklistService.blacklistToken(jwtToken);
        return ResponseEntity.ok("Successfully logged out!");
    }
}
