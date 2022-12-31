package com.mehisen.parking.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.mehisen.parking.entity.ERole;
import com.mehisen.parking.entity.RoleEntity;
import com.mehisen.parking.entity.UserEntity;
import com.mehisen.parking.payload.request.LoginRequest;
import com.mehisen.parking.payload.request.SignupRequest;
import com.mehisen.parking.payload.resposne.JwtResponse;
import com.mehisen.parking.payload.resposne.MessageResponse;
import com.mehisen.parking.repository.RoleRepository;
import com.mehisen.parking.repository.UserRepository;
import com.mehisen.parking.security.jwt.JwtUtils;
import com.mehisen.parking.security.service.UserDetailsImpl;
import com.mehisen.parking.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

}