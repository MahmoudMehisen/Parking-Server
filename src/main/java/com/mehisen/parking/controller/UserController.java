package com.mehisen.parking.controller;

import com.mehisen.parking.model.request.UserRequest;
import com.mehisen.parking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @PostMapping("/createNewUser")
    public ResponseEntity<?> createNewUser(@RequestBody UserRequest userRequest){
        return userService.createNewUser(userRequest);
    }

    @GetMapping("/allUsers")
    public ResponseEntity<?> allUsers(){
        return userService.allUsers();
    }


    @PostMapping("/addVipUser")
    public ResponseEntity<?> addVipUser(@RequestBody UserRequest userRequest){
        return userService.addVipUser(userRequest);
    }

    @PostMapping("/removeVipUser")
    public ResponseEntity<?> removeVipUser(@RequestBody UserRequest userRequest){
        return userService.removeVipUser(userRequest);
    }
}
