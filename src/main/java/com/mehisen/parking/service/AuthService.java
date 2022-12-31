package com.mehisen.parking.service;

import com.mehisen.parking.entity.ERole;
import com.mehisen.parking.entity.RoleEntity;
import com.mehisen.parking.entity.UserEntity;
import com.mehisen.parking.payload.request.LoginRequest;
import com.mehisen.parking.payload.request.SignupRequest;
import com.mehisen.parking.payload.resposne.JwtResponse;
import com.mehisen.parking.payload.resposne.MessageResponse;
import com.mehisen.parking.payload.resposne.UserResponse;
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
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());


            UserEntity user = userService.userInfo(userDetails.getId());
            if(user==null) return  null;
            return getLoginUserInfo(jwt, user);

    }
    private JwtResponse getLoginUserInfo(String jwt, UserEntity user) {
        List<String> roles = user.getRoles().stream()
                .map(item -> item.getName().toString())
                .collect(Collectors.toList());
        return new JwtResponse(jwt, user.getId(), user.getUsername(), user.getEmail(), roles, 0, user.getIsVip(), user.getIsComing());
    }

    public void registerUser(SignupRequest signUpRequest) {

        // Create new user's account
        UserEntity user = new UserEntity();

        user.setEmail(signUpRequest.getEmail());
        user.setUsername(signUpRequest.getUsername());
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
    }

    public boolean checkUsernameExist(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean checkEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }
}
