package com.mehisen.parking.controller;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.mehisen.parking.entity.ERole;
import com.mehisen.parking.entity.RoleEntity;
import com.mehisen.parking.entity.UserEntity;
import com.mehisen.parking.payload.request.ForgetRequest;
import com.mehisen.parking.payload.request.LoginRequest;
import com.mehisen.parking.payload.request.SignupRequest;
import com.mehisen.parking.payload.request.UpdatePasswordRequest;
import com.mehisen.parking.payload.resposne.JwtResponse;
import com.mehisen.parking.payload.resposne.MessageResponse;
import com.mehisen.parking.payload.resposne.UserResponse;
import com.mehisen.parking.repository.RoleRepository;
import com.mehisen.parking.repository.UserRepository;
import com.mehisen.parking.security.jwt.JwtUtils;
import com.mehisen.parking.security.service.UserDetailsImpl;
import com.mehisen.parking.service.AuthService;
import com.mehisen.parking.service.UserService;
import com.mehisen.parking.utilities.Helper;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    final private AuthService authService;

    final private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        try {
            JwtResponse userResponse = authService.authenticateUser(loginRequest);

            if (userResponse == null) {
                return ResponseEntity.status(400).body("Error username or password is wrong");
            }

            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when signin");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            if (authService.checkUsernameExist(signUpRequest.getUsername())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Username is already taken!"));
            }

            if (authService.checkEmailExist(signUpRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already in use!"));
            }

            authService.registerUser(signUpRequest);
            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when signup");
        }
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<?> forgetPassword(@Valid @RequestBody ForgetRequest forgetRequest) {
        try {
            UserEntity userEntity = userService.userByEmail(forgetRequest.getEmail());
            if (userEntity == null) {
                ResponseEntity.status(400).body("Error email not found");
            }

            userEntity = userService.addForgetToken(userEntity);
            authService.sendForgetEmail(userEntity);

            return ResponseEntity.ok(new MessageResponse("Forget email send successfully"));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when forget password");
        }
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        try {
            UserEntity userEntity = userService.userByEmail(updatePasswordRequest.getEmail());
            if (userEntity == null) {
                return ResponseEntity.status(400).body("Error email not found");
            }
            if (userEntity.getForgetToken() == null || !userEntity.getForgetToken().equals(updatePasswordRequest.getCode())) {
                return ResponseEntity.status(400).body("Error forget code is wrong");
            }

            if (authService.checkForgetToken(userEntity)) {
                return ResponseEntity.status(400).body("Error forget code was expired");
            }

            UserEntity updateUser = authService.updatePassword(userEntity, updatePasswordRequest.getNewPassword());
            userService.removeForgetToken(updateUser);

            return ResponseEntity.ok(new MessageResponse("Password Updated Successfully"));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when forget password");
        }
    }


}