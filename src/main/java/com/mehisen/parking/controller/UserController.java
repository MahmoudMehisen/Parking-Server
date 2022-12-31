package com.mehisen.parking.controller;

import com.mehisen.parking.payload.request.UserRequest;
import com.mehisen.parking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/userInfo")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
    public ResponseEntity<?> userInfo(@RequestBody UserRequest userRequest){
        return userService.userInfo(userRequest);
    }

    @GetMapping("/allUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> allUsers(){
        return userService.allUsers();
    }

    @DeleteMapping("/removeUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeUser(@PathVariable Long id){
        return userService.removeUser(id);
    }



    @PostMapping("/addVipUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addVipUser(@RequestBody UserRequest userRequest){
        return userService.addVipUser(userRequest);
    }

    @PostMapping("/removeVipUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeVipUser(@RequestBody UserRequest userRequest){
        return userService.removeVipUser(userRequest);
    }
}
