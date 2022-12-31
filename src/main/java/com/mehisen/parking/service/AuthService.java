package com.mehisen.parking.service;

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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            Optional<UserEntity> userEntity = userRepository.findById(userDetails.getId());

            if (userEntity.isEmpty()) {
                return ResponseEntity.status(400).body("User Not Found");
            }

            UserEntity user = userEntity.get();
            log.info(user.toString());

            return ResponseEntity.ok(getLoginUserInfo(jwt,roles,user));
        }catch (Exception e){
            return ResponseEntity.status(400).body("Error when signin");
        }

    }

    private JwtResponse getLoginUserInfo(String jwt, List<String> roles, UserEntity user) {
        return new JwtResponse(jwt, user.getId(),user.getUsername(),user.getEmail(),roles,0,user.getIsVip(),user.getIsComing());
    }

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        try{
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Username is already taken!"));
            }

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already in use!"));
            }

            // Create new user's account
            UserEntity user = new UserEntity();

            user.setEmail( signUpRequest.getEmail());
            user.setUsername( signUpRequest.getUsername());
            user.setPassword(encoder.encode(signUpRequest.getPassword()));

            Set<String> strRoles = signUpRequest.getRole();
            Set<RoleEntity> roles = new HashSet<>();

            if (strRoles == null) {
                RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);

            } else {
                strRoles.forEach(role -> {
                    if ("admin".equals(role)) {
                        RoleEntity adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    } else {
                        RoleEntity userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                });
            }

            user.setRoles(roles);
            userRepository.save(user);

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        }catch (Exception e){
            return ResponseEntity.status(400).body("Error when signup");
        }

    }
}
