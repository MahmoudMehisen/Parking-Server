package com.mehisen.parking.controller;

import com.mehisen.parking.entity.SlotEntity;
import com.mehisen.parking.entity.UserEntity;
import com.mehisen.parking.payload.request.UserRequest;
import com.mehisen.parking.payload.resposne.UserResponse;
import com.mehisen.parking.service.SlotService;
import com.mehisen.parking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final UserService userService;

    private final SlotService slotService;

    @GetMapping("/userInfo/{id}")
    @PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
    public ResponseEntity<?> userInfo(@PathVariable Long id) {
        try {
            UserEntity userInfo = userService.userInfo(id);
            if (userInfo == null) {
                ResponseEntity.status(400).body("Error user doesn't exist");
            }
            return ResponseEntity.ok(getUerFromEntity(userInfo));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when retrieve user info");
        }
    }

    @GetMapping("/allUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> allUsers() {
        try {
            List<UserEntity> userList = userService.allUsers();
            List<UserResponse> result = userList.stream().map(this::getUerFromEntity).toList();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when fetching all users");
        }
    }

    @DeleteMapping("/removeUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeUser(@PathVariable Long id) {
        try {
            userService.removeUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when remove user");
        }
    }

    @PostMapping("/addVipUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addVipUser(@Valid @RequestBody UserRequest userRequest) {
        try {
            SlotEntity slotEntity = slotService.findFirstByUserId(null);
            UserEntity userEntity = userService.userInfo(userRequest.getId());

            if (userEntity == null) {
                return ResponseEntity.status(400).body("User Not Found");
            }

            if (userEntity.getIsVip()) {
                return ResponseEntity.status(400).body("User Already VIP");
            }

            if (slotEntity == null) {
                return ResponseEntity.status(400).body("No Available Slot");
            }
            slotService.addVipUser(slotEntity,userEntity);
            UserEntity updatedUser = userService.addVipUser(userEntity, slotEntity);

            return ResponseEntity.ok(getUerFromEntity(updatedUser));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when adding user to vip");
        }
    }

    @PostMapping("/removeVipUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeVipUser(@Valid @RequestBody UserRequest userRequest) {
        try {
            UserEntity userEntity = userService.userInfo(userRequest.getId());
            SlotEntity slotEntity = slotService.findFirstByUserId(null);

            if (userEntity == null) {
                return ResponseEntity.status(400).body("User Not Found");
            }

            if (!userEntity.getIsVip() || userEntity.getSlot() == null) {
                return ResponseEntity.status(400).body("User Already VIP");
            }

            if (slotEntity == null) {
                return ResponseEntity.status(400).body("No Available Slot");
            }

            slotService.removeVipUser(slotEntity);
            UserEntity updatedUser = userService.removeVipUser(userEntity, slotEntity);

            return ResponseEntity.ok(getUerFromEntity(updatedUser));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(400).body("Error when remove user from vip");
        }
    }

    private UserResponse getUerFromEntity(UserEntity userEntity) {
        List<String> roles = userEntity.getRoles().stream().map(roleEntity -> roleEntity.getName().toString()).toList();
        UserResponse userResponse = new UserResponse(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(), 0, userEntity.getIsVip(), userEntity.getIsComing(), roles);
        if (userEntity.getSlot() != null) {
            userResponse.setSlotId(userEntity.getSlot().getId());
        }
        return userResponse;
    }
}
